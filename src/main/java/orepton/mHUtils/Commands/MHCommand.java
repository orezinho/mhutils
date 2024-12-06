package orepton.mHUtils.Commands;

import orepton.mHUtils.Files.ConfigManager;
import orepton.mHUtils.Files.MessagesManager;
import orepton.mHUtils.MHUtils;
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

public class MHCommand implements TabExecutor {

    private final MHUtils plugin;
    private final ConfigManager configManager;

    public MHCommand(MHUtils plugin) {
        this.plugin = plugin;
        configManager = new ConfigManager(plugin);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        Player p = (Player) sender;
        Location plocation = p.getLocation();

        MessagesManager message;
        ConfigManager configFile = plugin.getConfigManager();

        configFile.LoadConfig();
        message = new MessagesManager(plugin, configManager.getLang() + ".yml");
        message.loadMessages();

        if (args.length == 0) {
            if (p.hasPermission("mhutils.use")) {
                for (String line : message.getHelp()) {
                    p.sendMessage(ccolor(line));
                }
            } else {
                MsgSound(p, message.getPerm_error(), message.getError_sound(), message.getError_volume(), message.getError_pitch());
            }
        } else if (args.length == 1) {
            if (p.hasPermission("mhutils.reload")) {
                switch (args[0]) {
                    case "reload":
                        message.loadMessages();
                        configFile.LoadConfig();

                        if (message.getReload() != null) {
                            p.sendMessage(ccolor(message.getPrefix()) + ccolor(message.getReload()));
                            p.playSound(plocation, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);
                        }
                        break;
                    case "help":
                        for (String line : message.getHelp()) {
                            p.sendMessage(ccolor(line));
                        }
                        break;
                }
            }

        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
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
        Location plocation = p.getLocation();
        MessagesManager message;
        ConfigManager configFile = plugin.getConfigManager();

        configFile.LoadConfig();
        message = new MessagesManager(plugin, configManager.getLang() + ".yml");
        message.loadMessages();

        if (msg != null) {
            p.sendMessage(ccolor(message.getPrefix()) + ccolor(msg));
        }

        if (sound != null) {
            p.playSound(plocation, Sound.valueOf(sound), volume, pitch);
        }
    }

}
