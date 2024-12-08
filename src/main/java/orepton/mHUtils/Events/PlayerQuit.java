package orepton.mHUtils.Events;

import orepton.mHUtils.Commands.Spawn.SpawnCommand;
import orepton.mHUtils.MHUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    private final MHUtils plugin;
    private final SpawnCommand spawnCmd;

    public PlayerQuit(MHUtils plugin, SpawnCommand spawnCmd) {
        this.plugin = plugin;
        this.spawnCmd = spawnCmd;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        spawnCmd.getSpawnplayers().remove(p.getUniqueId());
    }
}
