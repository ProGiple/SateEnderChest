package progiple.satellite.sateenderchest.other.eco;

import lombok.Getter;
import org.bukkit.Bukkit;

public class SateJewels {
    @Getter private static boolean enabled = false;
    public static boolean setup() {
        if (Bukkit.getServer().getPluginManager().getPlugin("SateJewels") == null) {
            return false;
        }
        enabled = true;
        return true;
    }
}
