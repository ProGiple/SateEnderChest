package progiple.satellite.sateenderchest.enderchest.enderchest_menu.buttons.clickable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.novasparkle.lunaspring.Items.Item;
import progiple.satellite.sateenderchest.enderchest.Button;
import progiple.satellite.sateenderchest.other.configs.Config;

public class ClosedButton extends Item implements Button {
    public ClosedButton(ConfigurationSection section, byte slot) {
        super(section, slot);
        this.getItemStack().setAmount(1);

        this.getLore().replaceAll(line -> line
                .replace("$cost_jewels", String.valueOf(Config.getInt("config.jewelsCost.cost")))
                .replace("$cost_money", String.valueOf(Config.getInt("config.moneyCost.cost")))
                .replace("$jewels_icon", Config.getString("config.jewelsCost.icon"))
                .replace("$money_icon", Config.getString("config.moneyCost.icon")));
        this.setLore(this.getLore());
    }

    @Override
    public boolean onClick(Player player, Inventory inventory) {
        return false;
    }
}
