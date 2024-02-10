package mc.ultimatecore.skills.listener;

import lombok.AllArgsConstructor;
import mc.ultimatecore.skills.HyperSkills;
import mc.ultimatecore.skills.TempUser;
import mc.ultimatecore.skills.utils.AttributeUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class PlayerJoinLeaveListener implements Listener {

    private final HyperSkills plugin;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        try {
            Player player = event.getPlayer();

            if(!plugin.getAddonsManager().isMMOItems())
                AttributeUtils.manageAttribute(player, 20, HyperSkills.getInstance());

            // Stats
            this.plugin.getAbilitiesManager().loadPlayerAbilities(player);
            this.plugin.getPerksManager().loadPlayerPerks(player);
            this.plugin.getSkillManager().loadPlayerSkills(player);

            TempUser user = TempUser.getUser(player);
            user.name = player.getName();
            if(HyperSkills.getInstance().getConfiguration().scaledHealth){
                player.setHealthScale(20);
                player.setHealthScaled(false);
            }
        } catch (Exception e) {
            HyperSkills.getInstance().sendErrorMessage(e);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        try {
            Player player = event.getPlayer();

            plugin.getPerksManager().disconnect(player);

            plugin.getSkillManager().disconnect(player);

            plugin.getAbilitiesManager().disconnect(player);
        } catch (Exception e) {
            HyperSkills.getInstance().sendErrorMessage(e);
        }
    }
}
