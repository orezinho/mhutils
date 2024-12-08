package orepton.mHUtils.Events;

import orepton.mHUtils.Files.ConfigManager;
import orepton.mHUtils.Files.CustomConfig;
import orepton.mHUtils.Files.MessagesManager;
import orepton.mHUtils.MHUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;


public class PlayerJoin implements Listener {

    private final MHUtils plugin;
    private final ConfigManager configManager;

    public PlayerJoin(MHUtils plugin) {
        this.plugin = plugin;
        configManager = new ConfigManager(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        // Fly join Start

        Player p = e.getPlayer();
        String playerWorld = p.getWorld().getName();

        FileConfiguration config = this.plugin.getConfig();

        List<String> worlds = config.getStringList("worlds-whitelist");

        if (p.hasPermission("mhutils.fly.join")) {
            for (String world : worlds) {
                if (playerWorld.equalsIgnoreCase(world)) {
                    if (!p.isFlying()) {
                        p.setAllowFlight(true);
                        p.setFlying(true);
                    }
                }
            }
        } else if (p.hasPermission("mhutils.fly.use")) {
            for (String world : worlds) {
                if (playerWorld.equalsIgnoreCase(world)) {
                    if (!p.getAllowFlight()) {
                        p.setAllowFlight(true);
                    }
                }
            }
        }

        // Fly join end

        // Spawn join start
        CustomConfig spawnFile;
        MessagesManager message;

        spawnFile = new CustomConfig("spawn.yml", null, plugin);
        message = new MessagesManager(plugin, configManager.getLang() + ".yml");

        Location spawnLocation = spawnFile.getConfig().getLocation("spawn");
        Location spawnFlyLocation = spawnFile.getConfig().getLocation("spawn-fly");

        if (message.isJoin_spawn()) {
            if (p.hasPermission("mhutils.fly.join")) {
                if (spawnFlyLocation != null) {
                    p.teleport(spawnFlyLocation);
                } else {
                    p.teleport(spawnLocation);
                }
            } else {
                p.teleport(spawnLocation);
            }

        }
    }
}
