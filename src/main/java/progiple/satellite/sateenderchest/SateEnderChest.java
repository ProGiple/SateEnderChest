package progiple.satellite.sateenderchest;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.novasparkle.lunaspring.Events.MenuHandler;
import progiple.satellite.sateenderchest.listeners.OnEnderChestOpen;
import progiple.satellite.sateenderchest.other.Utils;
import progiple.satellite.sateenderchest.other.configs.Config;
import progiple.satellite.sateenderchest.other.configs.PlayerData;
import progiple.satellite.sateenderchest.other.eco.SateJewels;
import progiple.satellite.sateenderchest.other.eco.Vault;

import java.io.File;
import java.util.Objects;

public final class SateEnderChest extends JavaPlugin {
    @Getter
    private static SateEnderChest plugin;

    @Override
    public void onEnable() {
        plugin = this;
        plugin.saveResource("pages.yml", false);
        plugin.saveResource("confirm_menu.yml", false);

        this.reg(new OnEnderChestOpen());
        this.reg(new MenuHandler());

        Command command = new Command();
        Objects.requireNonNull(getCommand("sateenderchest")).setTabCompleter(command);
        Objects.requireNonNull(getCommand("sateenderchest")).setExecutor(command);

        Utils.loadPages();

        if (!Vault.setupEconomy() && Config.getBool("config.moneyCost.enabled")) {
            for (byte i = 0; i < 12; i++) System.out.println("VAULT НЕ УСТАНОВЛЕН И ВКЛЮЧЁН В КОНФИГЕ! ВЫКЛЮЧИТЕ ЕГО");
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        if (Config.getBool("config.jewelsCost.enabled")) {
            if (!SateJewels.setup()) {
                for (byte i = 0; i < 12; i++)
                    System.out.println("SateJewels НЕ УСТАНОВЛЕН И ВКЛЮЧЁН В КОНФИГЕ! ВЫКЛЮЧИТЕ ЕГО");
                Bukkit.getServer().getPluginManager().disablePlugin(plugin);
                return;
            }
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

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(plugin);
    }

    private void reg(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
    }
}