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

public class BackPageButton extends Item implements Button {
    private final byte thisPage;
    public BackPageButton(ConfigurationSection section, byte slot, byte thisPage) {
        super(section, slot);
        this.getItemStack().setAmount(1);
        this.thisPage = thisPage;
    }

    @Override
    public boolean onClick(Player player, Inventory inventory) {
        Page page = Page.getPageMap().get((byte) (this.thisPage - 1));
        Utils.debug(page);
        if (page == null || this.thisPage <= 0) {
            player.sendMessage(Config.getMessage("pageIsMin"));
            Sound errorSound = Sound.valueOf(Config.getString("config.errorBuyingSound"));
            player.playSound(player.getLocation(), errorSound, 1, 1);
            return false;
        }

        MenuManager.openInventory(player, new ECMenu(player, page));
        return true;
    }
}
