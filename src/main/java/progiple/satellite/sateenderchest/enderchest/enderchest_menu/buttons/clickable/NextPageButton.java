package progiple.satellite.sateenderchest.enderchest.enderchest_menu.buttons.clickable;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.novasparkle.lunaspring.Items.Item;
import org.novasparkle.lunaspring.Menus.MenuManager;
import progiple.satellite.sateenderchest.enderchest.enderchest_menu.ECMenu;
import progiple.satellite.sateenderchest.enderchest.enderchest_menu.Page;
import progiple.satellite.sateenderchest.enderchest.Button;
import progiple.satellite.sateenderchest.other.Utils;
import progiple.satellite.sateenderchest.other.configs.Config;
import progiple.satellite.sateenderchest.other.configs.PageManager;

public class NextPageButton extends Item implements Button {
    private final byte thisPage;
    private final boolean isEnded;
    public NextPageButton(ConfigurationSection section, byte slot, byte thisPage, boolean isEnded) {
        super(section, slot);
        this.getItemStack().setAmount(1);
        this.thisPage = thisPage;
        this.isEnded = isEnded;
    }

    @Override
    public boolean onClick(Player player, Inventory inventory) {
        byte newPage = (byte) (this.thisPage + 1);
        Page page = Page.getPageMap().get(newPage);

        Sound errorSound = Sound.valueOf(Config.getString("config.errorBuyingSound"));
        Utils.debug(errorSound);
        if (page == null || newPage > PageManager.getMaxPageNum()) {
            player.sendMessage(Config.getMessage("pageIsMax"));
            player.playSound(player.getLocation(), errorSound, 1, 1);
            return false;
        }

        if (!player.hasPermission("sateenderchest.viewpages") && !player.hasPermission("sateenderchest.bypass")) {
            if (!this.isEnded) {
                player.sendMessage(Config.getMessage("pageNotBought"));
                player.playSound(player.getLocation(), errorSound, 1, 1);
                return false;
            }
        }

        MenuManager.openInventory(player, new ECMenu(player, page));
        return true;
    }
}
