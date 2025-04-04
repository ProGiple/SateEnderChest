package progiple.satellite.sateenderchest.other;

import lombok.experimental.UtilityClass;
import progiple.satellite.sateenderchest.enderchest.enderchest_menu.Page;
import progiple.satellite.sateenderchest.other.configs.Config;
import progiple.satellite.sateenderchest.other.configs.PageManager;

@UtilityClass
public class Utils {
    public void loadPages() {
        Page.getPageMap().clear();
        for (String pageId : PageManager.getSection("pages").getKeys(false)) {
            String[] split = pageId.split("-");
            if (split.length == 1) {
                new Page(Byte.parseByte(split[0]));
            }
            else if (split.length >= 2) {
                for (byte i = Byte.parseByte(split[0]); i <= Byte.parseByte(split[1]); i++) {
                    new Page(pageId, i);
                }
            }
        }
    }

    public void debug(Object object) {
        if (Config.getBool("debug")) System.out.println(object);
    }
}
