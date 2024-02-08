package mc.ultimatecore.skills.managers;

import mc.ultimatecore.helper.implementations.object.*;
import mc.ultimatecore.skills.HyperSkills;
import mc.ultimatecore.skills.objects.abilities.Ability;
import mc.ultimatecore.skills.objects.abilities.PlayerAbilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

public class AbilitiesManager {
    private final HyperSkills plugin;
    private final Map<UUID, PlayerAbilities> abilitiesCache = new ConcurrentHashMap<>();

    public AbilitiesManager(HyperSkills plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getOnlinePlayers().forEach(this::loadPlayerAbilities);
    }

    public Double getAbility(UUID uuid, Ability key) {
        return this.getPlayerAbilities(uuid).getAbility(key);
    }

    public void resetData(UUID uuid) {
        if (abilitiesCache.containsKey(uuid)) {
            final PlayerAbilities abilities = new PlayerAbilities();
            abilitiesCache.put(uuid, abilities);
            this.plugin.getPluginDatabase().savePlayerAbilities(uuid, abilities);
        }
    }

    public void getUpdate(UUID uuid, Consumer<PlayerAbilities> abilities) {
        final PlayerAbilities playerAbilities = this.abilitiesCache.get(uuid);
        if (playerAbilities == null) {
            final PlayerAbilities loading = new PlayerAbilities();
            loading.addPendingTask(() -> {
                abilities.accept(loading);
                this.plugin.getPluginDatabase().savePlayerAbilities(uuid, loading);
            });
            this.plugin.getPluginDatabase().loadPlayerAbilities(uuid, loading);
            this.abilitiesCache.put(uuid, loading);
            return;
        }

        if (playerAbilities.isLoading()) {
            playerAbilities.addPendingTask(() -> {
                abilities.accept(playerAbilities);
                this.plugin.getPluginDatabase().savePlayerAbilities(uuid, playerAbilities);
            });
            return;
        }

        abilities.accept(playerAbilities);
        this.plugin.getPluginDatabase().savePlayerAbilities(uuid, playerAbilities);
    }

    public void loadPlayerAbilities(Player player) {
        this.getUpdate(player.getUniqueId(), DatabaseObject::connect);
    }

    public PlayerAbilities getPlayerAbilities(UUID uuid) {
        return abilitiesCache.computeIfAbsent(uuid, id -> {
            final PlayerAbilities loading = new PlayerAbilities();
            this.plugin.getPluginDatabase().loadPlayerAbilities(uuid, loading);
            return loading;
        });
    }

    public void remove(UUID uuid) {
        this.abilitiesCache.remove(uuid);
    }

    public void disconnect(Player player) {
        final PlayerAbilities abilities = this.abilitiesCache.get(player.getUniqueId());
        if (abilities == null) return;
        abilities.disconnect();
    }
}
