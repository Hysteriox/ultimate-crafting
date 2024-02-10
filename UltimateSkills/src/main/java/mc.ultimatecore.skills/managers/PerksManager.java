package mc.ultimatecore.skills.managers;

import mc.ultimatecore.helper.implementations.object.*;
import mc.ultimatecore.skills.HyperSkills;
import mc.ultimatecore.skills.objects.perks.Perk;
import mc.ultimatecore.skills.objects.perks.PlayerPerks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.*;

public class PerksManager {
    private final HyperSkills plugin;
    public final Map<UUID, PlayerPerks> perksCache = new HashMap<>();

    public PerksManager(HyperSkills plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getOnlinePlayers().forEach(this::loadPlayerPerks);
    }

    public void loadPlayerPerks(Player player) {
        this.getUpdate(player.getUniqueId(), DatabaseObject::connect);
    }

    public Double getPerk(UUID uuid, Perk key) {
        return perksCache.getOrDefault(uuid, new PlayerPerks()).getPerk(key);
    }

    public void resetData(UUID uuid) {
        if (perksCache.containsKey(uuid)) {
            final PlayerPerks playerPerks = new PlayerPerks();
            perksCache.put(uuid, playerPerks);
            this.plugin.getPluginDatabase().savePlayerPerks(uuid, playerPerks);
        }
    }

    public void getUpdate(UUID uuid, Consumer<PlayerPerks> consumer) {
        final PlayerPerks playerPerks = this.perksCache.get(uuid);
        if (playerPerks == null) {
            final PlayerPerks loading = new PlayerPerks();
            loading.addPendingTask(() -> {
                consumer.accept(loading);
                this.plugin.getPluginDatabase().savePlayerPerks(uuid, loading);
            });
            this.plugin.getPluginDatabase().loadPlayerPerks(uuid, loading);
            this.perksCache.put(uuid, loading);
            return;
        }

        if (playerPerks.isLoading()) {
            playerPerks.addPendingTask(() -> {
                consumer.accept(playerPerks);
                this.plugin.getPluginDatabase().savePlayerPerks(uuid, playerPerks);
            });
            return;
        }

        consumer.accept(playerPerks);
        this.plugin.getPluginDatabase().savePlayerPerks(uuid, playerPerks);
    }

    public PlayerPerks getPlayerPerks(UUID uuid) {
        return perksCache.computeIfAbsent(uuid, id -> {
            final PlayerPerks loading = new PlayerPerks();
            this.plugin.getPluginDatabase().loadPlayerPerks(uuid, loading);
            return loading;
        });
    }

    public void remove(UUID uuid) {
        this.perksCache.remove(uuid);
    }

    public void disconnect(Player player) {
        this.getUpdate(player.getUniqueId(), DatabaseObject::disconnect);
    }
}
