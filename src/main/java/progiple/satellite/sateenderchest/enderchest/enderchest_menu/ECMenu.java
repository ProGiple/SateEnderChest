package progiple.satellite.sateenderchest.enderchest.enderchest_menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.Menus.Items.Item;
import org.novasparkle.lunaspring.Menus.AMenu;
import progiple.satellite.sateenderchest.enderchest.Button;
import progiple.satellite.sateenderchest.enderchest.enderchest_menu.buttons.BuyButtonSetter;
import progiple.satellite.sateenderchest.enderchest.enderchest_menu.buttons.clickable.*;
import progiple.satellite.sateenderchest.other.configs.Config;
import progiple.satellite.sateenderchest.other.configs.PlayerData;

import java.util.ArrayList;
import java.util.List;

public class ECMenu extends AMenu {
    private NextPageButton nextPageButton = null;
    private BackPageButton backPageButton = null;
    private final BuyButtonSetter buttonSetter;
    private final ItemsSetter setter;
    private final Page page;
    public ECMenu(Player player, Page page) {
        super(player, page.getTitle(), (byte) (page.getRows() * 9));
        this.page = page;

        this.buttonSetter = new BuyButtonSetter(player, page);
        this.setter = new ItemsSetter(player.getName(), this.page.getNum(), (byte) (this.page.getRows() * 9));

        if (page.getNextPageSlot() >= 0) this.nextPageButton = new NextPageButton(Config.getSection("items.next_page"),
                page.getNextPageSlot(), this.page.getNum(), this.buttonSetter.getBuyButton() == null);
        if (page.getBackPageSlot() >= 0) this.backPageButton = new BackPageButton(Config.getSection("items.back_page"),
                page.getBackPageSlot(), this.page.getNum());
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        List<Item> newItems = new ArrayList<>();
        if (this.buttonSetter.getBuyButton() != null) newItems.add(this.buttonSetter.getBuyButton());
        if (this.nextPageButton != null) newItems.add(this.nextPageButton);
        if (this.backPageButton != null) newItems.add(this.backPageButton);

        this.addItems(newItems);
        this.buttonSetter.getClosedButtons().forEach(this::addItems);
        this.insertAllItems();

        this.setter.getItemStackMap().forEach((slot, item) -> this.getInventory().setItem(slot, item));
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        ItemStack itemStack = e.getCurrentItem();
        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        for (Item item : this.itemList) {
            if (item.getItemStack().equals(itemStack)) {
                e.setCancelled(true);
                if (item instanceof BuyButton) {
                    ((BuyButton) item).setClickType(e.getClick());
                }

                ((Button) item).onClick(this.getPlayer(), this.getInventory());
                break;
            }
        }
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        PlayerData playerData = PlayerData.getPlayerDataMap().get(this.getPlayer().getName());
        List<Byte> bytes = new ArrayList<>();
        this.buttonSetter.getClosedButtons().forEach(buyButton -> bytes.add(buyButton.getSlot()));

        for (byte i = 0; i < this.getInventory().getSize(); i++) {
            if ((this.nextPageButton != null && this.nextPageButton.getSlot() == i) ||
                    (this.backPageButton != null && this.backPageButton.getSlot() == i) ||
                        bytes.contains(i) || (this.buttonSetter.getBuyButton() != null
                            && this.buttonSetter.getBuyButton().getSlot() == i)) continue;

            ItemStack item = e.getInventory().getItem(i);
            if (item == null || item.getType() == Material.AIR) {
                playerData.removeItem(this.page.getNum(), i);
                continue;
            };

            playerData.setItem(this.page.getNum(), i, item);
        }
        playerData.save();
    }
}
