package mc.ultimatecore.skills.addons;


import io.lumine.xikage.mythicmobs.api.bukkit.*;
import io.lumine.xikage.mythicmobs.mobs.*;
import mc.ultimatecore.skills.implementations.SoftDependImpl;
import org.bukkit.entity.Entity;

public class MythicMobsAPIManager extends SoftDependImpl {

    private final BukkitAPIHelper bukkitAPIHelper;

    public MythicMobsAPIManager(String displayName) {
        super(displayName);
        bukkitAPIHelper = new BukkitAPIHelper();
    }

    public MythicMob getMythicMobEntity(Entity entity) {
        try {
            ActiveMob activeMob = bukkitAPIHelper.getMythicMobInstance(entity);
            if(activeMob == null) return null;
            return activeMob.getType();
        }catch (NullPointerException e){
            return null;
        }
    }
}
