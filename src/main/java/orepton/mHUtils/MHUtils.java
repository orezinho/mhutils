package orepton.mHUtils;

import orepton.mHUtils.Commands.FlyCommand;
import orepton.mHUtils.Commands.MHCommand;
import orepton.mHUtils.Commands.Spawn.SetspawnCommand;
import orepton.mHUtils.Commands.Spawn.SpawnCommand;
import orepton.mHUtils.Events.PlayerJoin;
import orepton.mHUtils.Events.PlayerMove;
import orepton.mHUtils.Events.PlayerQuit;
import orepton.mHUtils.Events.ServerCommand;
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

        // PlaceholderAPI
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
        TabExecutor SetspawnCmd = new SetspawnCommand(this);
        TabExecutor SpawnCmd = new SpawnCommand(this);
        SpawnCommand SpawnRegister = new SpawnCommand(this);

        Objects.requireNonNull(this.getCommand("fly")).setTabCompleter(FlyCmd);
        Objects.requireNonNull(this.getCommand("fly")).setExecutor(FlyCmd);
        Objects.requireNonNull(this.getCommand("mhutils")).setTabCompleter(MHCmd);
        Objects.requireNonNull(this.getCommand("mhutils")).setExecutor(MHCmd);
        Objects.requireNonNull(this.getCommand("setspawn")).setExecutor(SetspawnCmd);
        Objects.requireNonNull(this.getCommand("setspawn")).setTabCompleter(SetspawnCmd);
        Objects.requireNonNull(this.getCommand("spawn")).setTabCompleter(SpawnCmd);
        Objects.requireNonNull(this.getCommand("spawn")).setExecutor(SpawnRegister);
        getServer().getPluginManager().registerEvents(new PlayerMove(this, SpawnRegister), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(this, SpawnRegister), this);
        getServer().getPluginManager().registerEvents(new ServerCommand(this), this);
    }

    public MessagesManager getMessagesManager() {
        return messagesManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
