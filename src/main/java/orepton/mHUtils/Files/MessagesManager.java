package orepton.mHUtils.Files;

import orepton.mHUtils.MHUtils;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.List;

public class MessagesManager {

    private final CustomConfig langFile;
    private final CustomConfig configFile;

    public MessagesManager(MHUtils plugin, String lang) {
        langFile = new CustomConfig(lang, "languages", plugin);
        configFile = new CustomConfig("config.yml", null, plugin);
        loadMessages();
    }

    String prefix;
    String perm_error ;
    String error_sound;
    String fly_alr_on;
    String fly_alr_off;
    String world_error;
    String lang;
    String fly_sound;
    String fly_on;
    String fly_off;
    String targetError;
    float fly_volume;
    float fly_pitch;
    String fly_alr_on_other;
    String fly_alr_off_other;
    String fly_on_other;
    String fly_off_other;
    String fly_from_on;
    String fly_from_off;
    float error_volume;
    float error_pitch;
    List<String> worlds;
    List<String> help;
    String reload;
    String spawn_set;
    Integer spawn_delay;
    String spawn_sound;
    String spawn_command_message;
    float spawn_volume;
    float spawn_pitch;
    String spawn_success;
    String spawn_alr_teleporting;
    String spawn_tp_cancelled;
    String spawn_not_set;
    boolean spawn_move_cancel;
    boolean join_spawn;

    public void loadMessages() {
        langFile.registerConfig();
        configFile.registerConfig();
        FileConfiguration messages = langFile.getConfig();
        FileConfiguration config = configFile.getConfig();
        worlds = config.getStringList("worlds-whitelist");
        prefix = messages.getString("prefix");
        spawn_move_cancel = config.getBoolean("spawn.cancel-on-move");
        spawn_set = messages.getString("spawn.spawn-set");
        spawn_delay = config.getInt("spawn.spawn-delay");
        spawn_sound = config.getString("spawn.spawn-sound.sound");
        spawn_volume = (float) config.getLong("spawn.spawn-sound.volume");
        spawn_pitch = (float) config.getLong("spawn.spawn-sound.pitch");
        spawn_command_message = messages.getString("spawn.spawn-command");
        spawn_alr_teleporting = messages.getString("spawn.spawn-already-teleporting");
        spawn_tp_cancelled = messages.getString("spawn.spawn-teleport-cancelled");
        spawn_success = messages.getString("spawn.spawn-success");
        spawn_not_set = messages.getString("spawn.spawn-not-set");
        reload = messages.getString("config-reload");
        perm_error = messages.getString("permission-error");
        error_sound = config.getString("error-sound.sound");
        lang = config.getString("languages");
        fly_alr_on = messages.getString("fly.fly-already-on");
        fly_alr_off = messages.getString("fly.fly-already-off");
        world_error = messages.getString("world-error");
        help = messages.getStringList("help-message");
        fly_sound = config.getString("fly-sound.sound");
        targetError = messages.getString("target-not-found");
        fly_volume = (float) config.getLong("fly-sound.volume");
        fly_pitch = (float) config.getLong("fly-sound.pitch");
        fly_on = messages.getString("fly.fly-on");
        fly_off = messages.getString("fly.fly-off");
        fly_alr_on_other = messages.getString("fly.fly-already-on-other");
        fly_alr_off_other = messages.getString("fly.fly-already-off-other");
        fly_on_other = messages.getString("fly.fly-on-other");
        fly_off_other = messages.getString("fly.fly-off-other");
        fly_from_on = messages.getString("fly.fly-on-from-other");
        fly_from_off = messages.getString("fly.fly-off-from-other");
        error_volume = config.getLong("error-sound.volume");
        error_pitch = config.getLong("error-sound.pitch");
        join_spawn = config.getBoolean("spawn.join-spawn");
    }

    public boolean isJoin_spawn() {
        return join_spawn;
    }

    public boolean isSpawn_move_cancel() {
        return spawn_move_cancel;
    }

    public String getSpawn_tp_cancelled() {
        return spawn_tp_cancelled;
    }

    public String getSpawn_not_set() {
        return spawn_not_set;
    }

    public String getSpawn_alr_teleporting() {
        return spawn_alr_teleporting;
    }

    public String getSpawn_set() {
        return spawn_set;
    }

    public String getSpawn_success() {
        return spawn_success;
    }

    public String getSpawn_sound() {
        return spawn_sound;
    }

    public float getSpawn_volume () {
        return spawn_volume;
    }

    public float getSpawn_pitch() {
        return spawn_pitch;
    }

    public Integer getSpawn_delay() {
        return spawn_delay;
    }

    public String getSpawn_command_message() {
        return spawn_command_message;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getReload() {
        return reload;
    }

    public String getPerm_error() {
        return perm_error;
    }

    public String getError_sound() {
        return error_sound;
    }

    public String getFly_alr_on() {
        return fly_alr_on;
    }

    public String getFly_alr_off() {
        return fly_alr_off;
    }

    public String getWorld_error() {
        return world_error;
    }

    public String getFly_sound() {
        return fly_sound;
    }

    public String getTargetError() {
        return targetError;
    }

    public float getFly_volume() {
        return fly_volume;
    }

    public float getFly_pitch() {
        return fly_pitch;
    }

    public float getError_volume() {
        return error_volume;
    }

    public float getError_pitch() {
        return error_pitch;
    }

    public String getFly_on() {
        return fly_on;
    }

    public String getFly_off() {
        return fly_off;
    }

    public String getFly_alr_on_other() {
        return fly_alr_on_other;
    }

    public String getFly_alr_off_other() {
        return fly_alr_off_other;
    }

    public String getFly_on_other() {
        return fly_on_other;
    }

    public String getFly_off_other() {
        return fly_off_other;
    }

    public String getFly_from_on() {
        return fly_from_on;
    }

    public String getFly_from_off() {
        return fly_from_off;
    }

    public List<String> getWorlds() {
        return worlds;
    }

    public List<String> getHelp() {
        return help;
    }
}
