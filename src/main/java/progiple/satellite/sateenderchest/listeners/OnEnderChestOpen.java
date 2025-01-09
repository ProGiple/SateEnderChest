package progiple.satellite.sateenderchest.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.novasparkle.lunaspring.Menus.MenuManager;
import progiple.satellite.sateenderchest.enderchest.enderchest_menu.ECMenu;
import progiple.satellite.sateenderchest.enderchest.enderchest_menu.Page;
import progiple.satellite.sateenderchest.other.configs.PlayerData;

public class OnEnderChestOpen implements Listener {
    @EventHandler
    public void onOpen(InventoryOpenEvent e) {
        if (e.getInventory().getType() == InventoryType.ENDER_CHEST) {
            Player player = (Player) e.getPlayer();
            player.closeInventory();

            if (!PlayerData.getPlayerDataMap().containsKey(player.getName())) new PlayerData(player.getName());
            MenuManager.openInventory(player, new ECMenu(player, Page.getPageMap().get((byte) 0)));
        }
    }
}
