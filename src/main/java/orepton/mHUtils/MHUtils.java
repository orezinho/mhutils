package orepton.mHUtils;

import orepton.mHUtils.Commands.FlyCommand;
import orepton.mHUtils.Commands.MHCommand;
import orepton.mHUtils.Events.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class MHUtils extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        FileConfiguration config = this.getConfig();

        // Config
        saveDefaultConfig();

        // Registering commands and events
        TabExecutor FlyCmd = new FlyCommand(this);
        TabExecutor MHCmd = new MHCommand(this);

        this.getCommand("mhutils").setTabCompleter(MHCmd);
        this.getCommand("mhutils").setExecutor(MHCmd);

        boolean fly_enabled = this.getConfig().getBoolean("fly-enabled");

        if (fly_enabled) {
            try {
                this.getCommand("fly").setTabCompleter(FlyCmd);
                this.getCommand("fly").setExecutor(FlyCmd);
            } catch (NullPointerException e) {
                e.fillInStackTrace();
            }

        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[MHUtils] You've disabled fly features.");
        }

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
}
