package progiple.satellite.sateenderchest;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class SateEnderChest extends JavaPlugin implements Listener {

    private Economy economy;
    private PlayerPointsAPI ppapi;
    private File configFile;
    private FileConfiguration config;

    @Override
    public void onEnable() {

        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }

        // Загружаем конфигурацию
        config = YamlConfiguration.loadConfiguration(configFile);
        // Plugin startup logic
        this.economy = Objects.requireNonNull(Bukkit.getServicesManager().getRegistration(Economy.class)).getProvider();

        this.ppapi = PlayerPoints.getInstance().getAPI();

        saveDefaultConfig();
        reloadConfig();

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean onCommand(CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender.hasPermission("sateenderchest.reload")) {
            reloadConfig();
            sender.sendMessage(mess("reloadPlugin"));
        }
        else {
            sender.sendMessage(mess("noPermission"));
        }
        return true;
    }

    @EventHandler
    public void Interact(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        if (block != null && block.getType() == Material.ENDER_CHEST && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "cmi:ender " + player.getName() + " " + player.getName());
            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1, 1);
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory ender = player.getEnderChest();
        int size = ender.getSize();
        ItemStack itemt = new ItemStack(Material.valueOf(getConfig().getString("buy_slot.material")));
        ItemMeta meta = itemt.getItemMeta();

        player.updateInventory();

        List<String> loretext = new ArrayList<>();
        List<String> configLore = getConfig().getStringList("buy_slot.lore");

        meta.setCustomModelData(987651);

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("buy_slot.name"))));
        for (String line : configLore) {
            String coloredline = ChatColor.translateAlternateColorCodes('&', line);
            loretext.add(coloredline);
        }
        meta.setLore(loretext);
        itemt.setItemMeta(meta);
        if (size > 27) {
            for (int slots = getConfig().getInt("data." + player.getName()); slots < size; slots++) {
                ender.setItem(slots, itemt);
            }
            player.updateInventory();
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        Inventory ender = player.getEnderChest();
        ItemStack item = event.getCurrentItem();
        if (ender == inv) {
            if (item != null) {
                if (item.getType() == Material.valueOf(getConfig().getString("buy_slot.material")) && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getCustomModelData() == 987651) {
                    event.setCancelled(true);
                }
                if (item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == 987651 && item.getItemMeta().hasDisplayName() && item.getItemMeta().hasLore() && item.getType() == Material.valueOf(getConfig().getString("buy_slot.material"))) {
                    int slots = getConfig().getInt("data." + player.getName());
                    if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT) {
                        int money = getConfig().getInt("config.moneyCost");
                        if (economy.getBalance(player) >= money) {
                            economy.withdrawPlayer(player, money);
                            ender.close();
                            player.sendMessage(mess("slotBuying"));
                            int slot = slots + 1;
                            upd("data." + player.getName(), slot);
                            player.sendTitle(mess("title"), mess("subtitle"), 10, 30, 15);
                            player.playSound(player.getLocation(), Sound.valueOf(getConfig().getString("config.accessBuyingSound")), 1, 1);
                        }
                        else {
                            player.sendMessage(mess("noMoney"));
                            player.playSound(player.getLocation(), Sound.valueOf(getConfig().getString("config.errorBuyingSound")), 1, 1);
                        }
                    }
                    else if (event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT) {
                        int points = getConfig().getInt("config.pointsCost");
                        if (ppapi.look(player.getUniqueId()) >= points) {
                            ppapi.take(player.getUniqueId(), points);
                            player.sendMessage(mess("slotBuying"));
                            int slot = slots + 1;
                            ender.close();
                            upd("data." + player.getName(), slot);
                            player.sendTitle(mess("title"), mess("subtitle"), 10, 30, 15);
                            player.playSound(player.getLocation(), Sound.valueOf(getConfig().getString("config.accessBuyingSound")), 1, 1);
                        }
                        else {
                            player.sendMessage(mess("noPoints"));
                            player.playSound(player.getLocation(), Sound.valueOf(getConfig().getString("config.errorBuyingSound")), 1, 1);
                        }
                    }
                }
            }

        }
    }

    private String mess(String mess) {
        String path = "messages.";
        String messpath = getConfig().getString(path + "." + mess);
        assert messpath != null;
        return ChatColor.translateAlternateColorCodes('&', messpath);
    }

    private void upd(String path, Integer value) {
        // Записываем новое значение в конфигурацию;
        config.set(path, value);

        // Сохраняем обновленную конфигурацию
        try {
            config.save(configFile);
            reloadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
