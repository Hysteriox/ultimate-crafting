package mc.ultimatecore.talismans.managers;

import com.google.common.collect.*;
import mc.ultimatecore.talismans.HyperTalismans;
import mc.ultimatecore.talismans.api.events.PlayerEnterEvent;
import mc.ultimatecore.talismans.gui.TalismanBagGUI;
import mc.ultimatecore.talismans.objects.BagTalismans;
import mc.ultimatecore.talismans.objects.DebugType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.*;

public class UserManager {
    private final HyperTalismans plugin;
    private final Set<UUID> _currentlyLoading = Sets.newConcurrentHashSet();
    private final Map<UUID, BagTalismans> bagCache = new HashMap<>();
    private final Map<UUID, TalismanBagGUI> gui = new HashMap<>();
    private CountDownLatch savingCacheFuture;

    public UserManager(HyperTalismans plugin) {
        this.plugin = plugin;
        this.loadPlayerDataOnEnable();
    }

    public void disable() {
        savePlayerDataOnDisable();
    }

    public void savePlayerData(Player player, boolean removeFromCache, boolean async) {
        if(_currentlyLoading.contains(player.getUniqueId())) return;
        UUID uuid = player.getUniqueId();
        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                BagTalismans bagTalismans = this.bagCache.get(uuid);
                if (bagCache.containsKey(uuid))
                    this.plugin.getPluginDatabase().setBagTalismans(Bukkit.getOfflinePlayer(uuid), bagTalismans == null ? new ArrayList<>() : bagCache.get(uuid).getTalismans());
                if (removeFromCache)
                    bagCache.remove(player.getUniqueId());
                this.plugin.sendDebug(String.format("Saved data of player %s to database.", player.getName()), DebugType.LOG);
            });
        } else {
            if (bagCache.containsKey(uuid))
                this.plugin.getPluginDatabase().setBagTalismans(Bukkit.getOfflinePlayer(uuid), bagCache.get(uuid).getTalismans());
            if (removeFromCache) {
                bagCache.remove(player.getUniqueId());
            }
            this.plugin.sendDebug(String.format("Saved data of player %s to database.", player.getName()), DebugType.LOG);
        }
    }

    private void savePlayerDataOnDisable() {
        this.plugin.sendDebug("[PLUGIN DISABLE] Saving all player data", DebugType.LOG);
        if (!_currentlyLoading.isEmpty()) {
            this.plugin.sendDebug("[PLUGIN DISABLE] Waiting for loading players to end", DebugType.LOG);
            final CountDownLatch latch = new CountDownLatch(1);
            this.savingCacheFuture = latch;
            latch.countDown();
        }
        bagCache.values().forEach((bagTalismans) -> this.plugin.getPluginDatabase().setBagTalismans(Bukkit.getOfflinePlayer(bagTalismans.getUuid()), bagTalismans.getTalismans()));
        bagCache.clear();
        this.plugin.sendDebug("[PLUGIN DISABLE] Saved all player data to database - talismans", DebugType.LOG);
    }

    public void addIntoTable(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> this.plugin.getPluginDatabase().addIntoTalismansDatabase(player));
    }

    private void loadPlayerDataOnEnable() {
        Bukkit.getServer().getOnlinePlayers().forEach(this::loadPlayerData);
    }

    public void loadPlayerData(Player player) {
        if(_currentlyLoading.contains(player.getUniqueId())) return;
        _currentlyLoading.add(player.getUniqueId());
        this.plugin.sendDebug(String.format("Attempting to load Tailsmans of player %s from database", player.getName()), DebugType.LOG);
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            try {
                BagTalismans bagTalismans = this.getBagTalismans(player.getUniqueId());
                String talismans = this.plugin.getPluginDatabase().getBagTalismans(player);
                if (talismans != null)
                    bagTalismans.setTalismans(talismans);
                bagCache.put(player.getUniqueId(), bagTalismans);
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new PlayerEnterEvent(player)));
                this.plugin.sendDebug(String.format("Loaded talismans of player %s from database", player.getName()), DebugType.LOG);
            } finally {
                this.checkSave(player.getUniqueId());
            }
        }, plugin.getConfiguration().syncDelay);
    }

    private void checkSave(UUID uniqueId) {
        _currentlyLoading.remove(uniqueId);
        if (_currentlyLoading.isEmpty() && this.savingCacheFuture != null) {
            this.savingCacheFuture.countDown();
            this.savingCacheFuture = null;
        }
    }


    public BagTalismans getBagTalismans(UUID uuid) {
        if (uuid == null) throw new IllegalStateException("UUID SHOULD NOT BE NULL");
        return bagCache.computeIfAbsent(uuid, id -> new BagTalismans(uuid, new ArrayList<>()));
    }

    public TalismanBagGUI getGUI(UUID uuid) {
        if (!gui.containsKey(uuid)) gui.put(uuid, new TalismanBagGUI(this.plugin));
        return gui.get(uuid);
    }
}
