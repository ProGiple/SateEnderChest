package progiple.satellite.sateenderchest.enderchest;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface Button {
    ItemStack getItemStack();
    byte getSlot();
    boolean onClick(Player player, Inventory inventory);
}
