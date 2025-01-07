package progiple.satellite.sateenderchest.other.configs;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.Configuration.Configuration;
import progiple.satellite.sateenderchest.SateEnderChest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PlayerData {
    @Getter private static final Map<String, PlayerData> playerDataMap = new HashMap<>();

    public final Configuration config;
    public PlayerData(String nick) {
        File file = new File(SateEnderChest.getPlugin().getDataFolder(), String.format("data/%s.yml", nick));
        this.config = new Configuration(file);
        if (!file.exists()) this.load();

        playerDataMap.put(nick, this);
    }

    public void reload() {
        this.config.reload();
    }

    public byte getOpenedSlots(byte pageNum) {
        return (byte) this.config.getInt(String.format("pages.%s.openedSlots", String.valueOf(pageNum)));
    }

    public ConfigurationSection getSection(String path) {
        return this.config.getSection(path);
    }

    public void setItem(byte pageNum, byte slot, ItemStack item) {
        this.config.setItemStack(String.format("pages.%s.items.%s", String.valueOf(pageNum), String.valueOf(slot)), item);
    }

    public void removeItem(byte pageNum, byte slot) {
        this.setItem(pageNum, slot, null);
    }

    public void setSlots(byte pageNum, byte count) {
        String path = String.format("pages.%s.openedSlots", String.valueOf(pageNum));
        this.config.setInt(path, count);
        this.save();
    }

    public void save() {
        this.config.save();
    }

    private void load() {
        for (byte i = 0; i < PageManager.getMaxPageNum(); i++) {
            this.setSlots(i, (byte) 0);
        }
    }
}
