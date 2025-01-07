package progiple.satellite.sateenderchest.enderchest.enderchest_menu;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import progiple.satellite.sateenderchest.other.Utils;
import progiple.satellite.sateenderchest.other.configs.PageManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Getter
public class Page {
    @Getter private static final Map<Byte, Page> pageMap = new HashMap<>();

    private final byte rows;
    private final byte num;
    private String title;
    private final byte starterSlot;
    
    private final byte nextPageSlot;
    private final byte backPageSlot;
    public Page(byte num) {
        ConfigurationSection section = PageManager.getSection(String.format("pages.%s", String.valueOf(num)));
        this.num = num;
        this.rows = (byte) section.getInt("rows");
        this.title = Objects.requireNonNull(section.getString("unicalTitle")).isEmpty() ? PageManager.getString("title") : section.getString("unicalTitle");
        assert this.title != null;
        this.title = this.title
                .replace("$page", String.valueOf(num))
                .replace("$1+page", String.valueOf(num + 1))
                .replace("$max_pages", String.valueOf(PageManager.getMaxPageNum()))
                .replace("$1+max_pages", String.valueOf(PageManager.getMaxPageNum() + 1));
        this.starterSlot = (byte) section.getInt("slotsStartsFrom");

        ConfigurationSection buttonSection = section.getConfigurationSection("buttons");
        assert buttonSection != null;
        Set<String> stringSet = buttonSection.getKeys(false);
        this.nextPageSlot = stringSet.contains("nextPage") ? (byte) buttonSection.getInt("nextPage") : -1;
        this.backPageSlot = stringSet.contains("backPage") ? (byte) buttonSection.getInt("backPage") : -1;

        pageMap.put(num, this);
    }

    public Page(String id, byte num) {
        ConfigurationSection section = PageManager.getSection(String.format("pages.%s", id));
        Utils.debug(section);
        this.num = num;
        Utils.debug(num);
        this.rows = (byte) section.getInt("rows");
        Utils.debug(rows);
        this.title = Objects.requireNonNull(section.getString("unicalTitle")).isEmpty() ? PageManager.getString("title") : section.getString("unicalTitle");
        assert this.title != null;
        this.title = this.title
                .replace("$page", String.valueOf(num))
                .replace("$1+page", String.valueOf(num + 1))
                .replace("$max_pages", String.valueOf(PageManager.getMaxPageNum()))
                .replace("$1+max_pages", String.valueOf(PageManager.getMaxPageNum() + 1));
        Utils.debug(title);
        this.starterSlot = (byte) section.getInt("slotsStartsFrom");
        Utils.debug(starterSlot);

        ConfigurationSection buttonSection = section.getConfigurationSection("buttons");
        assert buttonSection != null;
        Set<String> stringSet = buttonSection.getKeys(false);
        this.nextPageSlot = stringSet.contains("nextPage") ? (byte) buttonSection.getInt("nextPage") : -1;
        this.backPageSlot = stringSet.contains("backPage") ? (byte) buttonSection.getInt("backPage") : -1;

        pageMap.put(num, this);
    }
}
