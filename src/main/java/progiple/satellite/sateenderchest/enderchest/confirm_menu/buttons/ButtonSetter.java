package progiple.satellite.sateenderchest.enderchest.confirm_menu.buttons;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.Menus.Items.Item;
import progiple.satellite.sateenderchest.enderchest.confirm_menu.buttons.clickable.CancelButton;
import progiple.satellite.sateenderchest.enderchest.confirm_menu.buttons.clickable.ConfirmButton;
import progiple.satellite.sateenderchest.other.EconomyType;
import progiple.satellite.sateenderchest.other.configs.ConfirmMenuConfig;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ButtonSetter {
    private final List<Item> itemList = new ArrayList<>();
    public ButtonSetter(String icon, int cost, byte pageNum, EconomyType economyType, byte targetSlot) {
        ConfigurationSection section = ConfirmMenuConfig.getSection("menu.items.clickable");
        for (String key : section.getKeys(false)) {
            ConfigurationSection itemSection = section.getConfigurationSection(key);
            assert itemSection != null;

            if (key.equalsIgnoreCase("CONFIRM")) {
                for (int i : itemSection.getIntegerList("slots")) {
                    this.itemList.add(new ConfirmButton(itemSection, (byte) i, targetSlot, pageNum, icon, cost, economyType));
                }
            }
            else if (key.equalsIgnoreCase("CANCEL")) {
                for (int i : itemSection.getIntegerList("slots")) {
                    this.itemList.add(new CancelButton(itemSection, (byte) i, pageNum));
                }
            }
        }
    }
}
