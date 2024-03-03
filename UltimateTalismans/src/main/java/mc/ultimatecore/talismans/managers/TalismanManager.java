package mc.ultimatecore.talismans.managers;

import lombok.Getter;
import mc.ultimatecore.talismans.HyperTalismans;
import mc.ultimatecore.talismans.api.events.PlayerEnterEvent;
import mc.ultimatecore.talismans.objects.PlayerTalismans;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class TalismanManager implements Listener {
    @Getter
    private final Map<UUID, PlayerTalismans> playerTalismans = new HashMap<>();
    @Getter
    private final Set<UUID> usingRegion = new HashSet<>();

    public TalismanManager(HyperTalismans plugin) {
        plugin.registerListeners(this);
    }

    @EventHandler
    private void onJoin(PlayerEnterEvent e){
        UUID uuid = e.getPlayer().getUniqueId();
        playerTalismans.put(uuid, new PlayerTalismans(e.getPlayer()));
    }

    @EventHandler
    private void onLeave(PlayerQuitEvent e){
        this.removePlayerTalismans(e.getPlayer().getUniqueId()).ifPresent(PlayerTalismans::stop);
    }

    @EventHandler
    private void onLeave(PlayerKickEvent e){
        this.removePlayerTalismans(e.getPlayer().getUniqueId()).ifPresent(PlayerTalismans::stop);
    }

    private Optional<PlayerTalismans> removePlayerTalismans(UUID uuid) {
        return Optional.ofNullable(this.playerTalismans.remove(uuid));
    }
}
