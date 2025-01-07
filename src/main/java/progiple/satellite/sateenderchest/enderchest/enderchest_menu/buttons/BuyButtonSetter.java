package progiple.satellite.sateenderchest.enderchest.enderchest_menu.buttons;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import progiple.satellite.sateenderchest.enderchest.enderchest_menu.Page;
import progiple.satellite.sateenderchest.enderchest.enderchest_menu.buttons.clickable.BuyButton;
import progiple.satellite.sateenderchest.enderchest.enderchest_menu.buttons.clickable.ClosedButton;
import progiple.satellite.sateenderchest.other.Utils;
import progiple.satellite.sateenderchest.other.configs.Config;
import progiple.satellite.sateenderchest.other.configs.PlayerData;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BuyButtonSetter {
    private final List<ClosedButton> closedButtons = new ArrayList<>();
    @Setter private BuyButton buyButton;
    public BuyButtonSetter(Player player, Page page) {
        if (player.hasPermission("sateenderchest.bypass")) return;

        PlayerData playerData = PlayerData.getPlayerDataMap().get(player.getName());
        Utils.debug(player);
        ConfigurationSection closedButtonSection = Config.getSection("items.closed_slot");
        Utils.debug(closedButtonSection);
        byte openedSlots = playerData.getOpenedSlots(page.getNum());
        Utils.debug(openedSlots);

        for (byte i = page.getStarterSlot(); i < page.getRows() * 9; i++) {
            Utils.debug(i);
            if ((i == page.getBackPageSlot() && page.getBackPageSlot() >= 0) ||
                    (i == page.getNextPageSlot() && page.getNextPageSlot() >= 0)) continue;

            if (openedSlots > 0) {
                openedSlots--;
                continue;
            }

            if (this.buyButton == null) {
                this.buyButton = new BuyButton(Config.getSection("items.buy_slot"), i, page.getNum());
                continue;
            }

            ClosedButton button = new ClosedButton(closedButtonSection, i);
            Utils.debug(button);
            this.closedButtons.add(button);
        }
    }
}
