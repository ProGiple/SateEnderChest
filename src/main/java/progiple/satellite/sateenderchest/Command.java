package progiple.satellite.sateenderchest;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.Menus.MenuManager;
import progiple.satellite.sateenderchest.enderchest.enderchest_menu.ECMenu;
import progiple.satellite.sateenderchest.enderchest.enderchest_menu.Page;
import progiple.satellite.sateenderchest.other.Utils;
import progiple.satellite.sateenderchest.other.configs.Config;
import progiple.satellite.sateenderchest.other.configs.ConfirmMenuConfig;
import progiple.satellite.sateenderchest.other.configs.PageManager;
import progiple.satellite.sateenderchest.other.configs.PlayerData;

import java.util.ArrayList;
import java.util.List;

public class Command implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length >= 1) {
            switch (args[0]) {
                case "open" -> {
                    if (args.length >= 2) {
                        if (commandSender.hasPermission("sateenderchest.admin") && commandSender instanceof Player) {
                            Player player = Bukkit.getPlayerExact(args[1]);
                            if (player != null) {
                                commandSender.sendMessage(Config.getMessage("openEnderChest").replace("$player", args[1]));
                                MenuManager.openInventory(player, new ECMenu(player, Page.getPageMap().get((byte) 0)));
                            }
                        }
                        else commandSender.sendMessage(Config.getMessage("noPermission"));
                    }
                    else this.open(commandSender);
                }
                case "reload" -> {
                    if (commandSender.hasPermission("sateenderchest.admin")) {
                        Config.reload();
                        PageManager.reload();
                        Utils.loadPages();
                        ConfirmMenuConfig.reload();
                        PlayerData.getPlayerDataMap().forEach((id, data) -> data.reload());
                        commandSender.sendMessage(Config.getMessage("reloadPlugin"));
                    }
                    else commandSender.sendMessage(Config.getMessage("noPermission"));
                }
                case "add" -> {
                    if (commandSender.hasPermission("sateenderchest.admin")) {
                        if (args.length >= 4) {
                            PlayerData playerData = PlayerData.getPlayerDataMap().get(args[1]);
                            if (playerData == null) playerData = new PlayerData(args[1]);

                            byte pageNum = Byte.parseByte(args[2]);
                            byte count = Byte.parseByte(args[3]);

                            playerData.setSlots(pageNum, (byte) (playerData.getOpenedSlots(pageNum) + count));
                        }
                        else commandSender.sendMessage(Config.getMessage("noArgs"));
                    }
                    else commandSender.sendMessage(Config.getMessage("noPermission"));
                }
                case "set" -> {
                    if (commandSender.hasPermission("sateenderchest.admin")) {
                        if (args.length >= 4) {
                            PlayerData playerData = PlayerData.getPlayerDataMap().get(args[1]);
                            if (playerData == null) playerData = new PlayerData(args[1]);

                            byte pageNum = Byte.parseByte(args[2]);
                            byte count = Byte.parseByte(args[3]);

                            playerData.setSlots(pageNum, count);
                        }
                        else commandSender.sendMessage(Config.getMessage("noArgs"));
                    }
                    else commandSender.sendMessage(Config.getMessage("noPermission"));
                }
            }
        }
        else this.open(commandSender);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return List.of("open", "reload", "add", "set");
        }
        else if (strings.length == 2 && (strings[0].equals("open") || strings[0].equals("add") || strings[0].equals("set"))) {
            List<String> names = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player -> names.add(player.getName()));
            return names;
        }
        else if (strings.length == 3 && (strings[0].equals("add") || strings[0].equals("set"))) {
            List<String> pages = new ArrayList<>();
            for (byte i = 0; i <= PageManager.getMaxPageNum(); i++) pages.add(String.valueOf(i));
            pages.add("<page>");
            return pages;
        }
        else if (strings.length == 4 && (strings[0].equals("add") || strings[0].equals("set"))) {
            return List.of("<count>");
        }
        return List.of();
    }

    private void open(CommandSender sender) {
        if (sender instanceof Player) {
            this.open((Player) sender);
        }
    }

    private void open(Player player) {
        MenuManager.openInventory(player, new ECMenu(player, Page.getPageMap().get((byte) 0)));
    }
}
