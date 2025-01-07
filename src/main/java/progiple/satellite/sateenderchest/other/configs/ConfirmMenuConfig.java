package progiple.satellite.sateenderchest.other.configs;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.Configuration.Configuration;
import progiple.satellite.sateenderchest.SateEnderChest;

import java.io.File;

@UtilityClass
public class ConfirmMenuConfig {
    private final Configuration config;
    static {
        config = new Configuration(new File(SateEnderChest.getPlugin().getDataFolder(), "confirm_menu.yml"));
    }

    public void reload() {
        config.reload();
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public ConfigurationSection getSection(String path) {
        return config.getSection(path);
    }
}
