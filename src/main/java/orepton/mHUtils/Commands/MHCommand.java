package orepton.mHUtils.Commands;

import orepton.mHUtils.MHUtils;
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

public class MHCommand implements TabExecutor {

    private final MHUtils plugin;

    public MHCommand(MHUtils plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        FileConfiguration config = this.plugin.getConfig();
        List<String> helpmsg = config.getStringList("help-message");
        Location plocation = p.getLocation();

        String reload_msg = config.getString("config-reload");
        String prefix = config.getString("prefix");

        String perm_error = config.getString("permission-error");
        String error_sound = config.getString("error-sound.sound");
        float error_volume = config.getLong("error-sound.volume");
        float error_pitch = config.getLong("error-sound.pitch");

        if (args.length == 0) {
            if (p.hasPermission("mhutils.use")) {
                for (String line : helpmsg) {
                    p.sendMessage(ccolor(line));
                }
            } else {
                MsgSound(p, perm_error, error_sound, error_volume, error_pitch);
            }
        } else if (args.length == 1) {
            if (p.hasPermission("mhutils.reload"))
            switch (args[0]) {
                case "reload":
                    this.plugin.reloadConfig();
                    if (reload_msg != null) {
                        p.sendMessage(ccolor(prefix) + ccolor(reload_msg));
                        p.playSound(plocation, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);
                    }
                    break;
                case "help":
                    for (String line : helpmsg) {
                        p.sendMessage(ccolor(line));
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        Player player = (Player) sender;

        if (player.hasPermission("mhutils.use")) {
            if (args.length == 1) {
                commands.add("reload");
                commands.add("help");
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        return completions;
    }

    private String ccolor(String parameter) {
        return ChatColor.translateAlternateColorCodes('&', parameter);
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
