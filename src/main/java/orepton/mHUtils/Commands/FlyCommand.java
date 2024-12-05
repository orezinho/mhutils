package orepton.mHUtils.Commands;

import me.clip.placeholderapi.PlaceholderAPI;
import orepton.mHUtils.MHUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import java.util.ArrayList;
import java.util.List;

public class FlyCommand implements TabExecutor {

    public final MHUtils plugin;

    public FlyCommand(MHUtils plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;

        FileConfiguration config = this.plugin.getConfig();

        List<String> worlds = config.getStringList("worlds-whitelist");

        String playerWorld = p.getWorld().getName();
        String perm_error = config.getString("permission-error");
        String error_sound = config.getString("error-sound.sound");
        String fly_alr_on = config.getString("fly-already-on");
        fly_alr_on = PlaceholderAPI.setPlaceholders(p, fly_alr_on);
        String fly_alr_off = config.getString("fly-already-off");
        fly_alr_off = PlaceholderAPI.setPlaceholders(p, fly_alr_off);
        String world_error = config.getString("world-error");
        world_error = PlaceholderAPI.setPlaceholders(p, world_error);
        String fly_sound = config.getString("fly-sound.sound");
        String targetError = config.getString("target-not-found");
        targetError = PlaceholderAPI.setPlaceholders(p, targetError);
        float fly_volume = (float) config.getLong("fly-sound.volume");
        float fly_pitch = (float) config.getLong("fly-sound.pitch");

        String fly_alr_on_other = config.getString("fly-already-on-other");
        String fly_alr_off_other = config.getString("fly-already-off-other");

        String fly_on_other = config.getString("fly-on-other");
        String fly_off_other = config.getString("fly-off-other");

        String fly_from_on = config.getString("fly-on-from-other");
        String fly_from_off = config.getString("fly-off-from-other");

        float error_volume = config.getLong("error-sound.volume");
        float error_pitch = config.getLong("error-sound.pitch");

        // Fly main command with no args
        if (args.length == 0) {
            if (p.hasPermission("mhutils.fly.use")) {
                for (String world : worlds) {
                    if (playerWorld.equalsIgnoreCase(world)) {
                        if (!p.getAllowFlight()) {
                            flyOn(p);
                        } else {
                            flyOff(p);
                        }
                    } else {
                        MsgSound(p, world_error, error_sound, error_volume, error_pitch);
                    }

                }
            } else {
                MsgSound(p, perm_error, error_sound, error_volume, error_pitch);
            }
        } else if (args.length == 1){
            switch (args[0]) {
                case "on":
                    if (p.getAllowFlight()) {
                        MsgSound(p, fly_alr_on, error_sound, error_volume, error_pitch);

                    } else {
                        flyOn(p);
                    }
                    break;
                case "off":
                    if (!p.getAllowFlight()) {
                        MsgSound(p, fly_alr_off, error_sound, error_volume, error_pitch);
                    } else {
                        flyOff(p);
                    }
                    break;
            }
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);
            fly_on_other = PlaceholderAPI.setPlaceholders(target, fly_on_other);
            fly_off_other = PlaceholderAPI.setPlaceholders(target, fly_off_other);
            fly_alr_off = PlaceholderAPI.setPlaceholders(target, fly_alr_off);
            fly_alr_on = PlaceholderAPI.setPlaceholders(target, fly_alr_on);
            fly_from_on = PlaceholderAPI.setPlaceholders(p, fly_from_on);
            fly_alr_on_other = PlaceholderAPI.setPlaceholders(target, fly_alr_on_other);
            fly_alr_off_other = PlaceholderAPI.setPlaceholders(target, fly_alr_off_other);
            fly_from_on = PlaceholderAPI.setPlaceholders(p, fly_from_on);
            fly_from_off = PlaceholderAPI.setPlaceholders(p, fly_from_off);

            switch (args[0]) {
                case "on":
                    try {
                        if (target.getAllowFlight()) {
                            if (target == p) {
                                MsgSound(p, fly_alr_on, error_sound, error_volume, error_pitch);
                            } else {
                                MsgSound(p, fly_alr_on_other, error_sound, error_volume, error_pitch);
                            }
                        } else {
                            if (target == p) {
                                flyOn(p);
                            } else {
                                MsgSound(p, fly_on_other, fly_sound, fly_volume, fly_pitch);
                                MsgSound(target, fly_from_on, fly_sound, fly_volume, fly_pitch);
                                target.setAllowFlight(true);
                            }
                        }
                    } catch (NullPointerException e) {
                        MsgSound(p, targetError, error_sound, error_volume, error_pitch);
                    }
                    break;
                case "off":
                    try {
                        if (!target.getAllowFlight()) {
                            if (target == p) {
                                MsgSound(p, fly_alr_off, fly_sound, fly_volume, fly_pitch);
                            } else {
                                MsgSound(p, fly_alr_off_other, error_sound, error_volume, error_pitch);
                            }
                        } else {
                            if (target == p) {
                                flyOff(p);
                            } else {
                                MsgSound(p, fly_off_other, fly_sound, fly_volume, fly_pitch);
                                MsgSound(target, fly_from_off, fly_sound, fly_volume, fly_pitch);
                                target.setAllowFlight(false);
                            }
                        }
                    } catch (NullPointerException e) {
                        MsgSound(p, targetError, error_sound, error_volume, error_pitch);
                    }
            }
        }

        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        List<String> onlinePlayers = new ArrayList<>();

        Player player = (Player) sender;

        if (player.hasPermission("mhutils.fly.use")) {
            if (args.length == 1) {
                commands.add("on");
                commands.add("off");
            }

            else if (args.length == 2) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    onlinePlayers.add(p.getName());
                } return onlinePlayers;
            }

            StringUtil.copyPartialMatches(args[0], commands, completions);

        }
        return completions;

    }

    private String ccolor(String parameter) {
        return ChatColor.translateAlternateColorCodes('&', parameter);
    }

    private void flyOn(Player p) {
        FileConfiguration config = this.plugin.getConfig();

        //Fly
        String fly_on = config.getString("fly-on");
        String fly_sound = config.getString("fly-sound.sound");
        float fly_volume = (float) config.getLong("fly-sound.volume");
        float fly_pitch = (float) config.getLong("fly-sound.pitch");

        p.setAllowFlight(true);
        MsgSound(p, fly_on, fly_sound, fly_volume, fly_pitch);
    }

    private void flyOff(Player p) {
        FileConfiguration config = this.plugin.getConfig();

        //Fly
        String fly_off = config.getString("fly-off");
        String fly_sound = config.getString("fly-sound.sound");
        float fly_volume = (float) config.getLong("fly-sound.volume");
        float fly_pitch = (float) config.getLong("fly-sound.pitch");

        p.setAllowFlight(false);
        MsgSound(p, fly_off, fly_sound, fly_volume, fly_pitch);
    }

    private void MsgSound(Player p, String msg, String sound, float volume, float pitch) {
        FileConfiguration config = this.plugin.getConfig();

        String prefix = config.getString("prefix");
        Location plocation = p.getLocation();

        if (msg != null) {
            p.sendMessage(ccolor(prefix) + ccolor(msg));
        }

        if (sound != null) {
            p.playSound(plocation, Sound.valueOf(sound), volume, pitch);
        }
    }
}
