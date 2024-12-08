package orepton.mHUtils.Events;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import orepton.mHUtils.Files.ConfigManager;
import orepton.mHUtils.Files.CustomConfig;
import orepton.mHUtils.Files.MessagesManager;
import orepton.mHUtils.Files.UpdateChecker;
import orepton.mHUtils.MHUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

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
        Player p = e.getPlayer();
        MessagesManager messages;
        messages = new MessagesManager(plugin, configManager.getLang() + ".yml");
        messages.loadMessages();

        // Update Checker
        new BukkitRunnable() {
            @Override
            public void run() {
                TextComponent update = new TextComponent(TextComponent.fromLegacy(ccolor(messages.getPrefix() + "&eThere is a new update available!")));
                new UpdateChecker(plugin, 121149).getVersion(version -> {
                    update.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/mhutilities.121149/history"));
                    update.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ccolor("&8Click to open the link!"))));
                    if (!plugin.getDescription().getVersion().equals(version)) {
                        p.spigot().sendMessage(update);
                    }
                });
            }
        }.runTaskLater(plugin, 60);

        // Fly join Start
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
                    assert spawnLocation != null;
                    p.teleport(spawnLocation);
                }
            } else {
                assert spawnLocation != null;
                p.teleport(spawnLocation);
            }

        }
    }

    private String ccolor(String parameter) {
        return ChatColor.translateAlternateColorCodes('&', parameter);
    }
}
