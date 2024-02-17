package mc.ultimatecore.skills.utils;

import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;

public class UniqueIdUtils {

    @SuppressWarnings("deprecation")
    public static UUID getUniqueId(String name) {
        final Player player = Bukkit.getPlayer(name);
        if (player == null) return Bukkit.getOfflinePlayer(name).getUniqueId();
        return player.getUniqueId();
    }
}
