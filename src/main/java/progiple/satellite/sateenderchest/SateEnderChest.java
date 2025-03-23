package progiple.satellite.sateenderchest;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.novasparkle.lunaspring.other.LunaPlugin;
import progiple.satellite.sateenderchest.listeners.OnEnderChestOpen;
import progiple.satellite.sateenderchest.other.Utils;
import progiple.satellite.sateenderchest.other.configs.Config;
import progiple.satellite.sateenderchest.other.configs.PlayerData;
import progiple.satellite.sateenderchest.other.eco.SateJewels;
import progiple.satellite.sateenderchest.other.eco.Vault;

import java.io.File;
import java.util.Objects;

public final class SateEnderChest extends LunaPlugin {
    @Getter
    private static SateEnderChest plugin;

    @Override
    public void onEnable() {
        plugin = this;
        this.loadFiles(true, "pages.yml", "confirm_menu.yml");
        Utils.loadPages();

        this.initialize();
        this.registerListener(new OnEnderChestOpen());
        this.registerTabExecutor(new Command(), "sateenderchest");

        if (!Vault.setupEconomy() && Config.getBool("config.moneyCost.enabled")) {
            System.out.println("VAULT НЕ УСТАНОВЛЕН И ВКЛЮЧЁН В КОНФИГЕ! ВЫКЛЮЧИТЕ ЕГО");
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        if (Config.getBool("config.jewelsCost.enabled") && !SateJewels.setup()) {
            System.out.println("SateJewels НЕ УСТАНОВЛЕН И ВКЛЮЧЁН В КОНФИГЕ! ВЫКЛЮЧИТЕ ЕГО");
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        File dir = new File(plugin.getDataFolder(), "data");
        if (dir.exists() && dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (!file.isDirectory()) {
                    String name = file.getName();
                    if (name.contains(".yml")) {
                        String patch = name.replace(".yml", "");
                        new PlayerData(patch);
                    }
                }
            }
        }
    }
}