package orepton.mHUtils.Commands.Spawn;

import orepton.mHUtils.Files.ConfigManager;
import orepton.mHUtils.Files.CustomConfig;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SetspawnCommand implements TabExecutor {

    private final MHUtils plugin;
    private final ConfigManager configManager;

    public SetspawnCommand(MHUtils plugin) {
        this.plugin = plugin;
        configManager = new ConfigManager(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        Player p = (Player) sender;
        Location location = p.getLocation();
        MessagesManager message;
        CustomConfig spawnFile;

        message = new MessagesManager(plugin, configManager.getLang() + ".yml");
        message.loadMessages();
        spawnFile = new CustomConfig("spawn.yml", null, plugin);
        if (p.hasPermission("mhutils.setspawn")) {
            if (args.length == 0) {
                spawnFile.getConfig().set("spawn", location);
                spawnFile.saveConfig();
                spawnFile.reloadConfig();
                MsgSound(p, message.getSpawn_set(), message.getSpawn_sound(), message.getSpawn_volume(), message.getSpawn_pitch());
            } else if (args.length == 1) {
                switch (args[0]) {
                    case "normal":
                        spawnFile.getConfig().set("spawn", location);
                        spawnFile.saveConfig();
                        spawnFile.reloadConfig();
                        MsgSound(p, message.getSpawn_set(), message.getSpawn_sound(), message.getSpawn_volume(), message.getSpawn_pitch());
                        break;
                    case "fly":
                        spawnFile.getConfig().set("spawn-fly", location);
                        spawnFile.saveConfig();
                        spawnFile.reloadConfig();
                        MsgSound(p, message.getSpawn_set(), message.getSpawn_sound(), message.getSpawn_volume(), message.getSpawn_pitch());
                        break;
                }
            }
        } else {
            MsgSound(p, message.getPerm_error(), message.getError_sound(), message.getError_volume(), message.getError_pitch());
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;

        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        if (p.hasPermission("mhutils.setspawn")) {
            if (args.length == 1) {
                commands.add("normal");
                commands.add("fly");
            }
        }
        StringUtil.copyPartialMatches(args[0], commands, completions);

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
