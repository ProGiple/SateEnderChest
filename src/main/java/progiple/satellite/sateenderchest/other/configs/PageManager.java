package progiple.satellite.sateenderchest.other.configs;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.Configuration.Configuration;
import progiple.satellite.sateenderchest.SateEnderChest;

import java.io.File;

@UtilityClass
public class PageManager {
    private static final Configuration config;
    static {
        config = new Configuration(new File(SateEnderChest.getPlugin().getDataFolder(), "pages.yml"));
    }

    public void reload() {
        config.reload();
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public ConfigurationSection getSection(String path) {
        return config.getSection(path);
    }

    public byte getMaxPageNum() {
        byte max = 0;
        for (String pageId : PageManager.getSection("pages").getKeys(false)) {
            String[] split = pageId.split("-");
            if (split.length == 1) max = Byte.parseByte(split[0]);
            else if (split.length >= 2) max = Byte.parseByte(split[1]);
        }
        return max;
    }
}
