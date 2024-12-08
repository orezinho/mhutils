package orepton.mHUtils.Events;

import orepton.mHUtils.Files.ConfigManager;
import orepton.mHUtils.Files.MessagesManager;
import orepton.mHUtils.MHUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class ServerCommand implements Listener {

    private final MHUtils plugin;
    private final ConfigManager configManager;

    public ServerCommand(MHUtils plugin) {
        this.plugin = plugin;
        configManager = new ConfigManager(plugin);
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent e) {
        ConfigManager configFile = plugin.getConfigManager();
        if (e.getCommand().equalsIgnoreCase("spawn")) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "You must be a player to execute this command!");
            e.setCancelled(true);
        }

        if (e.getCommand().equalsIgnoreCase("setspawn")) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "You must be a player to execute this command!");
            e.setCancelled(true);
        }

        MessagesManager message;
        message = new MessagesManager(plugin, configManager.getLang() + ".yml");
        message.loadMessages();

        if (e.getCommand().equalsIgnoreCase("mhutils")) {
            for (String line : message.getHelp()) {
                e.getSender().sendMessage(ccolor(line));
            }
            e.setCancelled(true);
        }

        if (e.getCommand().equalsIgnoreCase("mhutils reload")) {
            message.loadMessages();
            configFile.LoadConfig();

            if (message.getReload() != null) {
                e.getSender().sendMessage(ccolor(message.getPrefix()) + ccolor(message.getReload()));
            }
            e.setCancelled(true);
        }

    }

    private String ccolor(String parameter) {
        return ChatColor.translateAlternateColorCodes('&', parameter);
    }
}
