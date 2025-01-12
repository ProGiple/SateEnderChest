package progiple.satellite.sateenderchest.other.eco;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.satellite.progiple.satejewels.api.SJAPI;

public class SateJewels {
    @Getter private static SJAPI sjapi;
    public static boolean setup() {
        if (Bukkit.getServer().getPluginManager().getPlugin("SateJewels") == null) {
            return false;
        }
        sjapi = org.satellite.progiple.satejewels.SateJewels.getPlugin().getSjapi();
        return true;
    }
}
