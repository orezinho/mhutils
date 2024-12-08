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
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpawnCommand implements TabExecutor {

    private final MHUtils plugin;
    private final ConfigManager configManager;

    private final ArrayList<UUID> spawnplayers = new ArrayList<>();
    private int countdownID;

    public SpawnCommand(MHUtils plugin) {
        this.plugin = plugin;
        configManager = new ConfigManager(plugin);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;

        MessagesManager message;
        CustomConfig spawnFile;
        message = new MessagesManager(plugin, configManager.getLang() + ".yml");
        message.loadMessages();

        spawnFile = new CustomConfig("spawn.yml", null, plugin);
        Location spawnLocation = spawnFile.getConfig().getLocation("spawn");
        Location spawnFlyLocation = spawnFile.getConfig().getLocation("spawn-fly");


        BukkitRunnable countdown = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    assert spawnLocation != null;
                    if (p.hasPermission("mhutils.fly.join")) {
                        if (spawnFile.getConfig().getLocation("spawn-fly") != null) {
                            assert spawnFlyLocation != null;
                            p.teleport(spawnFlyLocation);
                            p.setFlying(true);
                            p.setAllowFlight(true);
                        } else {
                            p.teleport(spawnLocation);
                        }
                    } else {
                        p.teleport(spawnLocation);
                    }
                    spawnplayers.remove(p.getUniqueId());
                } catch (NullPointerException e) {
                    e.fillInStackTrace();
                }
                p.sendMessage(ccolor(message.getPrefix()) + ccolor(message.getSpawn_success()));
            }
        };

        if (!spawnplayers.contains(p.getUniqueId())) {
            if (message.getSpawn_delay() > 0) {
                MsgSound(p, message.getSpawn_command_message().replace("%spawn_delay%", message.getSpawn_delay().toString()), message.getSpawn_sound(), message.getSpawn_volume(), message.getSpawn_pitch());
                if (message.isSpawn_move_cancel()) {
                    spawnplayers.add(p.getUniqueId());
                }
                countdown.runTaskLater(plugin, message.getSpawn_delay() * 20);
                countdownID = countdown.getTaskId();
            } else {
                try {
                    assert spawnLocation != null;
                    if (p.hasPermission("mhutils.fly.join")) {
                        if (spawnFile.getConfig().getLocation("spawn-fly") != null) {
                            assert spawnFlyLocation != null;
                            p.teleport(spawnFlyLocation);
                            p.setFlying(true);
                            p.setAllowFlight(true);
                        } else {
                            p.teleport(spawnLocation);
                        }
                    } else {
                        p.teleport(spawnLocation);
                    }
                } catch (NullPointerException e) {
                    e.fillInStackTrace();
                }
                MsgSound(p, message.getSpawn_success(), message.getSpawn_sound(), message.getSpawn_volume(), message.getSpawn_pitch());
            }
        } else {
            MsgSound(p, message.getSpawn_alr_teleporting(), message.getError_sound(), message.getError_volume(), message.getError_pitch());
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return List.of();
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

    public ArrayList<UUID> getSpawnplayers() {
        return spawnplayers;
    }

    public int getCountdownID() {
        return countdownID;
    }
}
