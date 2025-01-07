package progiple.satellite.sateenderchest.enderchest.confirm_menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.Items.Item;
import org.novasparkle.lunaspring.Menus.AMenu;
import org.novasparkle.lunaspring.Menus.Decoration;
import progiple.satellite.sateenderchest.enderchest.Button;
import progiple.satellite.sateenderchest.enderchest.confirm_menu.buttons.ButtonSetter;
import progiple.satellite.sateenderchest.other.EconomyType;
import progiple.satellite.sateenderchest.other.configs.ConfirmMenuConfig;

public class ConfirmMenu extends AMenu {
    private final Decoration decoration;
    private final ButtonSetter buttonSetter;
    public ConfirmMenu(Player player, EconomyType economyType, String icon, int cost, byte pageNum, byte targetSlot) {
        super(player, ConfirmMenuConfig.getString("menu.title"), (byte) (ConfirmMenuConfig.getInt("menu.rows") * 9));
        this.decoration = new Decoration(ConfirmMenuConfig.getSection("menu.items.decorations"));
        this.buttonSetter = new ButtonSetter(icon, cost, pageNum, economyType, targetSlot);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        this.decoration.insert(this);
        this.addItems(this.buttonSetter.getItemList());
        this.insertAllItems();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        ItemStack itemStack = e.getCurrentItem();
        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        for (Item item : this.itemList) {
            if (item.getItemStack().equals(itemStack)) {
                ((Button) item).onClick(this.getPlayer(), this.getInventory());
                break;
            }
        }
    }

    @Override
    public void onClose(InventoryCloseEvent e) {

    }
}
