package dev.imlukas.partycrackersplugin;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class PluginSettings {

    private final FileConfiguration config;
    private String requestUrl;
    private String requestBearer;

    public PluginSettings(PartyCrackersPlugin plugin)  {
        this.config = plugin.getConfig();
        loadSettings();
    }

    public void loadSettings() {
        this.requestUrl = config.getString("http-request.url");
        this.requestBearer = config.getString("http-request.bearer-token");
    }
}
