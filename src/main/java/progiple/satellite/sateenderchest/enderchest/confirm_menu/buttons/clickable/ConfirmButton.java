package progiple.satellite.sateenderchest.enderchest.confirm_menu.buttons.clickable;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.novasparkle.lunaspring.Items.Item;
import org.novasparkle.lunaspring.Menus.MenuManager;
import org.satellite.progiple.satejewels.api.SJAPI;
import progiple.satellite.sateenderchest.enderchest.Button;
import progiple.satellite.sateenderchest.enderchest.enderchest_menu.ECMenu;
import progiple.satellite.sateenderchest.enderchest.enderchest_menu.Page;
import progiple.satellite.sateenderchest.other.EconomyType;
import progiple.satellite.sateenderchest.other.Utils;
import progiple.satellite.sateenderchest.other.configs.Config;
import progiple.satellite.sateenderchest.other.configs.PlayerData;
import progiple.satellite.sateenderchest.other.eco.SateJewels;
import progiple.satellite.sateenderchest.other.eco.Vault;

public class ConfirmButton extends Item implements Button {
    private final byte pageNum;
    private final int cost;
    private final EconomyType economyType;
    public ConfirmButton(ConfigurationSection section, byte slot, byte targetSlot, byte pageNum, String icon, int cost, EconomyType economyType) {
        super(section, slot);
        this.pageNum = pageNum;
        this.cost = cost;
        this.economyType = economyType;

        this.getLore().replaceAll(line -> line
                .replace("$cost_jewels", String.valueOf(Config.getInt("config.jewelsCost.cost")))
                .replace("$cost_money", String.valueOf(Config.getInt("config.moneyCost.cost")))
                .replace("$jewels_icon", Config.getString("config.jewelsCost.icon"))
                .replace("$money_icon", Config.getString("config.moneyCost.icon"))
                .replace("$total_cost", String.valueOf(this.cost))
                .replace("$slot", String.valueOf(targetSlot))
                .replace("$1+slot", String.valueOf(targetSlot + 1))
                .replace("$icon_cost", Utils.color(icon)));
        this.setLore(this.getLore());
    }

    @Override
    public boolean onClick(Player player, Inventory inventory) {
        Utils.debug(this.economyType);
        Utils.debug(Vault.getEconomy());
        Utils.debug(SateJewels.isEnabled() ? SJAPI.isNotNull() : "false");
        if (this.economyType == EconomyType.VAULT && Vault.getEconomy() != null) {
            if (Vault.getEconomy().getBalance(player) < this.cost) {
                player.sendMessage(Config.getMessage("noMoney"));
                return false;
            }

            Vault.getEconomy().withdrawPlayer(player, this.cost);
        }
        else if (this.economyType == EconomyType.SATEJEWELS && SateJewels.isEnabled()) {
            boolean next = false;
            if (!SJAPI.isNotNull()) next = SJAPI.additiveLoad();

            if (next || SJAPI.isNotNull()) {
                if (SJAPI.getJewels(player) < this.cost) {
                    player.sendMessage(Config.getMessage("noJewels"));
                    return false;
                }

                SJAPI.removeJewels(player, this.cost);
            }
        }
        else return false;

        PlayerData playerData = PlayerData.getPlayerDataMap().get(player.getName());
        Utils.debug(this.pageNum);
        playerData.setSlots(this.pageNum, (byte) (playerData.getOpenedSlots(this.pageNum) + 1));

        player.sendMessage(Config.getMessage("slotBuying"));
        player.playSound(player.getLocation(), Sound.valueOf(Config.getString("config.successfulBuyingSound")), 1, 1);

        MenuManager.openInventory(player, new ECMenu(player, Page.getPageMap().get(this.pageNum)));
        return true;
    }
}
