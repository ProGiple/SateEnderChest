package progiple.satellite.sateenderchest.other;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import progiple.satellite.sateenderchest.enderchest.enderchest_menu.Page;
import progiple.satellite.sateenderchest.other.configs.Config;
import progiple.satellite.sateenderchest.other.configs.PageManager;

@UtilityClass
public class Utils {
    public String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public void loadPages() {
        Page.getPageMap().clear();
        for (String pageId : PageManager.getSection("pages").getKeys(false)) {
            System.out.println(pageId);
            String[] split = pageId.split("-");
            if (split.length == 1) {
                new Page(Byte.parseByte(split[0]));
            }
            else if (split.length >= 2) {
                for (byte i = Byte.parseByte(split[0]); i <= Byte.parseByte(split[1]); i++) {
                    System.out.println(i);
                    new Page(pageId, i);
                }
            }
        }
    }

    public void debug(Object object) {
        if (Config.getBool("debug")) System.out.println(object);
    }
}
