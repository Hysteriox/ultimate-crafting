package mc.ultimatecore.skills.database;

import mc.ultimatecore.helper.UltimatePlugin;
import mc.ultimatecore.helper.implementations.DatabaseImpl;
import mc.ultimatecore.skills.objects.PlayerSkills;
import mc.ultimatecore.skills.objects.abilities.PlayerAbilities;
import mc.ultimatecore.skills.objects.perks.PlayerPerks;
import org.bukkit.OfflinePlayer;

import java.util.Set;
import java.util.UUID;

public abstract class Database extends DatabaseImpl {

	public Database(UltimatePlugin plugin) {
		super(plugin);
	}

	public abstract Set<UUID> getAllPlayers();

	public abstract String getPlayerSkills(OfflinePlayer player);

    public abstract void loadPlayerSkills(UUID uuid, PlayerSkills skills);

	public abstract void savePlayerSkills(UUID uuid, PlayerSkills playerSkills);

    public abstract void loadPlayerAbilities(UUID uuid, PlayerAbilities playerAbilities);

    public abstract void savePlayerAbilities(UUID uuid, PlayerAbilities playerAbilities);

    public abstract void loadPlayerPerks(UUID uuid, PlayerPerks playerPerks);

    public abstract void savePlayerPerks(UUID uuid, PlayerPerks playerPerks);

    public abstract String saveAbilitiesStatement();

    public abstract String saveSkillsStatement();

    public abstract String savePerksStatement();

    public abstract void saveSkillsDirectly();
}
