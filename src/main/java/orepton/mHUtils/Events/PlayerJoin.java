package orepton.mHUtils.Events;

import orepton.mHUtils.MHUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;


public class PlayerJoin implements Listener {

    public final MHUtils plugin;

    public PlayerJoin(MHUtils plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
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
        }
    }
}
