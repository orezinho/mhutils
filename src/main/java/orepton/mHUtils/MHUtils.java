package orepton.mHUtils;

import orepton.mHUtils.Commands.FlyCommand;
import orepton.mHUtils.Commands.MHCommand;
import orepton.mHUtils.Events.PlayerJoin;
import orepton.mHUtils.Files.ConfigManager;
import orepton.mHUtils.Files.MessagesManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class MHUtils extends JavaPlugin {

    private MessagesManager messagesManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Registering commands and events
        registerCommands();

        configManager = new ConfigManager(this);
        messagesManager = new MessagesManager(this, configManager.getLang() + ".yml");

        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[MHUtils] PlaceholderAPI found!");
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[MHUtils] PlaceholderAPI couldn't be found. It's recommended to install this plugin.");
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[MHUtils] Plugin successfully activated!");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerCommands() {
        TabExecutor FlyCmd = new FlyCommand(this);
        TabExecutor MHCmd = new MHCommand(this);
        Objects.requireNonNull(this.getCommand("fly")).setTabCompleter(FlyCmd);
        Objects.requireNonNull(this.getCommand("fly")).setExecutor(FlyCmd);
        Objects.requireNonNull(this.getCommand("mhutils")).setTabCompleter(MHCmd);
        Objects.requireNonNull(this.getCommand("mhutils")).setExecutor(MHCmd);
    }

    public MessagesManager getMessagesManager() {
        return messagesManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

}
