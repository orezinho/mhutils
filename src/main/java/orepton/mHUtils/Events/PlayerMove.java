package orepton.mHUtils.Events;

import orepton.mHUtils.Commands.Spawn.SpawnCommand;
import orepton.mHUtils.Files.ConfigManager;
import orepton.mHUtils.Files.MessagesManager;
import orepton.mHUtils.MHUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {
    private final MHUtils plugin;
    private final ConfigManager configManager;
    private final SpawnCommand spawnCmd;

    public PlayerMove(MHUtils plugin, SpawnCommand spawnCmd) {
        this.plugin = plugin;
        configManager = new ConfigManager(plugin);
        this.spawnCmd = spawnCmd;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        MessagesManager message;
        message = new MessagesManager(plugin, configManager.getLang() + ".yml");
        message.loadMessages();


        if (spawnCmd.getSpawnplayers().contains(p.getUniqueId())) {
            Bukkit.getScheduler().cancelTask(spawnCmd.getCountdownID());
            spawnCmd.getSpawnplayers().remove(p.getUniqueId());
            MsgSound(p, message.getSpawn_tp_cancelled(), message.getError_sound(), message.getError_volume(), message.getError_pitch());
        }
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
