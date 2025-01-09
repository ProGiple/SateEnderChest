package progiple.satellite.sateenderchest.enderchest.confirm_menu.buttons.clickable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.novasparkle.lunaspring.Menus.Items.Item;
import org.novasparkle.lunaspring.Menus.MenuManager;
import progiple.satellite.sateenderchest.enderchest.Button;
import progiple.satellite.sateenderchest.enderchest.enderchest_menu.ECMenu;
import progiple.satellite.sateenderchest.enderchest.enderchest_menu.Page;

public class CancelButton extends Item implements Button {
    private final byte pageNum;
    public CancelButton(ConfigurationSection section, byte slot, byte pageNum) {
        super(section, slot);
        this.pageNum = pageNum;
    }

    @Override
    public void onClick(Player player, Inventory inventory) {
        MenuManager.openInventory(player, new ECMenu(player, Page.getPageMap().get(this.pageNum)));
    }
}
