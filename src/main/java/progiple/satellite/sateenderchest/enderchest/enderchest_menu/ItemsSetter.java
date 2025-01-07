package progiple.satellite.sateenderchest.enderchest.enderchest_menu;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import progiple.satellite.sateenderchest.other.configs.PlayerData;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ItemsSetter {
    private final Map<Byte, ItemStack> itemStackMap = new HashMap<>();
    public ItemsSetter(String nick, byte num, byte maxSlots) {
        PlayerData playerData = PlayerData.getPlayerDataMap().get(nick);
        ConfigurationSection section = playerData.getSection(String.format("pages.%s.items", String.valueOf(num)));
        if (section == null) return;

        section.getKeys(false).forEach(key -> {
            ItemStack itemStack = section.getItemStack(key);

            byte slot = Byte.parseByte(key);
            if (slot < maxSlots) this.itemStackMap.put(slot, itemStack);
        });
    }
}
