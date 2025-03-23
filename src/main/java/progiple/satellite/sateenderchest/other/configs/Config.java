package progiple.satellite.sateenderchest.other.configs;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.Configuration.IConfig;
import org.novasparkle.lunaspring.Util.Utils;
import progiple.satellite.sateenderchest.SateEnderChest;

@UtilityClass
public class Config {
    private final IConfig config;
    static {
        SateEnderChest.getPlugin().saveDefaultConfig();
        config = new IConfig(SateEnderChest.getPlugin());
    }

    public void reload() {
        config.reload(SateEnderChest.getPlugin());
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public ConfigurationSection getSection(String path) {
        return config.getSection(path);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public boolean getBool(String path) {
        return config.getBoolean(path);
    }

    public String getMessage(String id) {
        return Utils.color(getString(String.format("messages.%s", id)));
    }
}
