package progiple.satellite.sateenderchest.enderchest.enderchest_menu.buttons.clickable;

import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.novasparkle.lunaspring.Items.Item;
import org.novasparkle.lunaspring.Menus.MenuManager;
import progiple.satellite.sateenderchest.enderchest.Button;
import progiple.satellite.sateenderchest.enderchest.confirm_menu.ConfirmMenu;
import progiple.satellite.sateenderchest.other.EconomyType;
import progiple.satellite.sateenderchest.other.configs.Config;

public class BuyButton extends Item implements Button {
    @Setter private ClickType clickType;
    private final byte slot;
    private final byte pageNum;
    public BuyButton(ConfigurationSection section, byte slot, byte pageNum) {
        super(section, slot);
        this.slot = slot;
        this.pageNum = pageNum;
        this.clickType = ClickType.RIGHT;

        this.getLore().replaceAll(line -> line
                .replace("$cost_jewels", String.valueOf(Config.getInt("config.jewelsCost.cost")))
                .replace("$cost_money", String.valueOf(Config.getInt("config.moneyCost.cost")))
                .replace("$jewels_icon", Config.getString("config.jewelsCost.icon"))
                .replace("$money_icon", Config.getString("config.moneyCost.icon"))
                .replace("$slot", String.valueOf(this.slot)));
        this.setLore(this.getLore());
    }

    @Override
    public boolean onClick(Player player, Inventory inventory) {
        String type = this.clickType.isLeftClick() ? "jewelsCost" : "moneyCost";
        String reversedType = this.clickType.isLeftClick() ? "moneyCost" : "jewelsCost";

        EconomyType economyType = type.equals("moneyCost") ? EconomyType.VAULT : EconomyType.SATEJEWELS;
        int totalCost;
        String icon;

        if (Config.getBool(String.format("config.%s.enabled", type))) {
            totalCost = Config.getInt(String.format("config.%s.cost", type));
            icon = Config.getString(String.format("config.%s.icon", type));
        }
        else {
            if (Config.getBool(String.format("config.%s.enabled", reversedType))) {
                totalCost = Config.getInt(String.format("config.%s.cost", reversedType));
                icon = Config.getString(String.format("config.%s.icon", reversedType));
                economyType = reversedType.equals("moneyCost") ? EconomyType.VAULT : EconomyType.SATEJEWELS;
            }
            else {
                player.sendMessage(Config.getMessage("ecoError"));
                return false;
            }
        }

        ConfirmMenu confirmMenu = new ConfirmMenu(player, economyType, icon, totalCost, this.pageNum, this.slot);
        MenuManager.openInventory(player, confirmMenu);
        return false;
    }
}
