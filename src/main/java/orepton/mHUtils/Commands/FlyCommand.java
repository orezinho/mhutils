package orepton.mHUtils.Commands;

import me.clip.placeholderapi.PlaceholderAPI;
import orepton.mHUtils.Files.ConfigManager;
import orepton.mHUtils.Files.MessagesManager;
import orepton.mHUtils.MHUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FlyCommand implements TabExecutor {

    private final ConfigManager configManager;
    private final MHUtils plugin;

    public FlyCommand(MHUtils plugin) {
        configManager = new ConfigManager(plugin);
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player p = (Player) sender;

        MessagesManager message;
        ConfigManager configFile = plugin.getConfigManager();

        configFile.LoadConfig();
        message = new MessagesManager(plugin, configManager.getLang() + ".yml");
        message.loadMessages();
        String playerWorld = p.getWorld().getName();

        // Fly main command with no args
        if (args.length == 0) {
            if (p.hasPermission("mhutils.fly.use")) {
                for (String world : message.getWorlds()) {
                    if (playerWorld.equalsIgnoreCase(world)) {
                        if (!p.getAllowFlight()) {
                            flyOn(p);
                        } else {
                            flyOff(p);
                        }
                    } else {
                        MsgSound(p, message.getWorld_error(), message.getError_sound(), message.getError_volume(), message.getError_pitch());
                    }

                }
            } else {
                MsgSound(p, message.getPerm_error(), message.getError_sound(), message.getError_volume(), message.getError_pitch());
            }
        } else if (args.length == 1) {
            switch (args[0]) {
                case "on":
                    if (p.getAllowFlight()) {
                        MsgSound(p, message.getFly_alr_on(), message.getError_sound(), message.getError_volume(), message.getError_pitch());

                    } else {
                        flyOn(p);
                    }
                    break;
                case "off":
                    if (!p.getAllowFlight()) {
                        MsgSound(p, message.getFly_alr_off(), message.getError_sound(), message.getError_volume(), message.getError_pitch());
                    } else {
                        flyOff(p);
                    }
                    break;
            }
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);

            if (p.hasPermission("mhutils.fly.others")) {
                switch (args[0]) {
                    case "on":
                        try {
                            assert target != null;
                            if (target.getAllowFlight()) {
                                if (target == p) {
                                    MsgSound(p, message.getFly_alr_on(), message.getError_sound(), message.getError_volume(), message.getError_pitch());
                                } else {
                                    String Papi_fly_alr_on_other = PlaceholderAPI.setPlaceholders(target.getPlayer(), message.getFly_alr_on_other());
                                    MsgSound(p, Papi_fly_alr_on_other, message.getError_sound(), message.getError_volume(), message.getError_pitch());
                                }
                            } else {
                                if (target == p) {
                                    flyOn(p);
                                } else {
                                    String Papi_getFly_on_other = PlaceholderAPI.setPlaceholders(target.getPlayer(), message.getFly_on_other());
                                    MsgSound(p, Papi_getFly_on_other, message.getFly_sound(), message.getFly_volume(), message.getFly_pitch());
                                    String Papi_getFly_from_on = PlaceholderAPI.setPlaceholders(target.getPlayer(), message.getFly_from_on());
                                    MsgSound(target, Papi_getFly_from_on, message.getFly_sound(), message.getFly_volume(), message.getFly_pitch());
                                    target.setAllowFlight(true);
                                }
                            }
                        } catch (NullPointerException e) {
                            MsgSound(p, message.getTargetError().replace("%player_name%", args[1]), message.getError_sound(), message.getError_volume(), message.getError_pitch());
                        }
                        break;
                    case "off":
                        try {
                            assert target != null;
                            if (!target.getAllowFlight()) {
                                if (target == p) {
                                    String Papi_fly_alr_off = PlaceholderAPI.setPlaceholders(target.getPlayer(), message.getFly_alr_off());
                                    MsgSound(p, Papi_fly_alr_off, message.getError_sound(), message.getError_volume(), message.getError_pitch());
                                } else {
                                    String Papi_fly_alr_off_other = PlaceholderAPI.setPlaceholders(target.getPlayer(), message.getFly_alr_off_other());
                                    MsgSound(p, Papi_fly_alr_off_other, message.getError_sound(), message.getError_volume(), message.getError_pitch());
                                }
                            } else {
                                if (target == p) {
                                    flyOff(p);
                                } else {
                                    String Papi_getFly_off_other = PlaceholderAPI.setPlaceholders(target.getPlayer(), message.getFly_off_other());
                                    MsgSound(p, Papi_getFly_off_other, message.getFly_sound(), message.getFly_volume(), message.getFly_pitch());
                                    String Papi_getFly_from_off = PlaceholderAPI.setPlaceholders(target.getPlayer(), message.getFly_from_off());
                                    MsgSound(target, Papi_getFly_from_off, message.getFly_sound(), message.getFly_volume(), message.getFly_pitch());
                                    target.setAllowFlight(false);
                                }
                            }
                        } catch (NullPointerException e) {
                            MsgSound(p, message.getTargetError().replace("%player_name%", args[1]), message.getError_sound(), message.getError_volume(), message.getError_pitch());
                        }
                }
            }

        }

        return true;

    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        List<String> onlinePlayers = new ArrayList<>();

        Player player = (Player) sender;

        if (player.hasPermission("mhutils.fly.use")) {
            if (args.length == 1) {
                commands.add("on");
                commands.add("off");
            } else if (args.length == 2) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    onlinePlayers.add(p.getName());
                }
                return onlinePlayers;
            }

            StringUtil.copyPartialMatches(args[0], commands, completions);

        }
        return completions;

    }

    private String ccolor(String parameter) {
        return ChatColor.translateAlternateColorCodes('&', parameter);
    }

    private void flyOn(Player p) {
        MessagesManager message;
        ConfigManager configFile = plugin.getConfigManager();

        configFile.LoadConfig();
        message = new MessagesManager(plugin, configManager.getLang() + ".yml");
        message.loadMessages();

        //Fly
        p.setAllowFlight(true);
        MsgSound(p, message.getFly_on(), message.getFly_sound(), message.getFly_volume(), message.getFly_pitch());
    }

    private void flyOff(Player p) {

        MessagesManager message;
        ConfigManager configFile = plugin.getConfigManager();

        configFile.LoadConfig();
        message = new MessagesManager(plugin, configManager.getLang() + ".yml");
        message.loadMessages();

        //Fly
        p.setAllowFlight(false);
        MsgSound(p, message.getFly_off(), message.getFly_sound(), message.getFly_volume(), message.getFly_pitch());
    }

    private void MsgSound(Player p, String msg, String sound, float volume, float pitch) {
        MessagesManager message;
        ConfigManager configFile = plugin.getConfigManager();

        configFile.LoadConfig();
        message = new MessagesManager(plugin, configManager.getLang() + ".yml");
        message.loadMessages();

        String prefix = message.getPrefix();
        Location plocation = p.getLocation();

        if (msg != null) {
            p.sendMessage(ccolor(prefix) + ccolor(msg));
        }

        if (sound != null) {
            p.playSound(plocation, Sound.valueOf(sound), volume, pitch);
        }
    }
}
