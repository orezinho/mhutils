package orepton.mHUtils.Files;

import orepton.mHUtils.MHUtils;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final CustomConfig configFile;
    private String lang;


    public ConfigManager(MHUtils plugin) {
        configFile = new CustomConfig("config.yml", null, plugin);
        LoadConfig();

    }
    public void LoadConfig(){

        configFile.registerConfig();
        FileConfiguration fileConfiguration = configFile.getConfig();
        lang = fileConfiguration.getString("lang");
    }

    public String getLang() {
        LoadConfig();
        return lang;
    }

}


