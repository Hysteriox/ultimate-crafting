package mc.ultimatecore.skills.database;

import mc.ultimatecore.helper.implementations.*;
import mc.ultimatecore.skills.HyperSkills;
import mc.ultimatecore.skills.database.handler.*;
import mc.ultimatecore.skills.objects.PlayerSkills;
import mc.ultimatecore.skills.objects.abilities.PlayerAbilities;
import mc.ultimatecore.skills.objects.perks.PlayerPerks;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class SQLDatabase extends Database {

	protected HyperSkills plugin;
    private final DatabaseTask<PlayerAbilities> abilitiesTask;
    private final DatabaseTask<PlayerSkills> skillsTask;
    private final DatabaseTask<PlayerPerks> perksTask;

	public SQLDatabase(HyperSkills plugin) {
		super(plugin);
		this.plugin = plugin;
        this.abilitiesTask = this.addTask(new AbilitiesHandler(plugin, this));
        this.skillsTask = this.addTask(new SkillsHandler(plugin, this));
        this.perksTask = this.addTask(new PerksHandler(plugin, this));
	}

	public void createTables() {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			execute("CREATE TABLE IF NOT EXISTS Skills (UUID varchar(36) NOT NULL UNIQUE, Data LONGTEXT, PRIMARY KEY (UUID))");
			execute("CREATE TABLE IF NOT EXISTS Perks (UUID varchar(36) NOT NULL UNIQUE, Data LONGTEXT, PRIMARY KEY (UUID))");
			execute("CREATE TABLE IF NOT EXISTS Abilities (UUID varchar(36) NOT NULL UNIQUE, Data LONGTEXT, PRIMARY KEY (UUID))");
		});
	}

	@Override
	public Set<UUID> getAllPlayers() {
		Set<UUID> uuids = new HashSet<>();
		try (Connection con = this.hikari.getConnection(); ResultSet set = con.prepareStatement("SELECT * FROM Skills").executeQuery()) {
			while (set.next())
				uuids.add(UUID.fromString(set.getString("UUID")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return uuids;
	}

    @Override
    public void loadPlayerAbilities(UUID uuid, PlayerAbilities playerAbilities) {
        this.abilitiesTask.load(uuid, playerAbilities);
    }

	@Override
	public void savePlayerAbilities(UUID uuid, PlayerAbilities playerAbilities) {
        this.abilitiesTask.save(uuid, playerAbilities);
	}

	@Override
	public String getPlayerSkills(OfflinePlayer p) {
		try (Connection con = this.hikari.getConnection(); PreparedStatement statement = con.prepareStatement("SELECT * FROM Skills WHERE UUID=?")) {
			statement.setString(1, p.getUniqueId().toString());
			try (ResultSet set = statement.executeQuery()) {
				if (set.next()) {
					return set.getString("Data");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

    @Override
    public void savePlayerSkills(UUID uuid, PlayerSkills playerSkills) {
        this.skillsTask.save(uuid, playerSkills);
    }

    @Override
    public void loadPlayerSkills(UUID uuid, PlayerSkills skills) {
        this.skillsTask.load(uuid, skills);
    }

    @Override
    public void saveSkillsDirectly() {
        this.skillsTask.saveQueueingData();
    }


    @Override
    public void loadPlayerPerks(UUID uuid, PlayerPerks playerPerks) {
        this.perksTask.load(uuid, playerPerks);
    }

    @Override
    public void savePlayerPerks(UUID uuid, PlayerPerks playerPerks) {
        this.perksTask.save(uuid, playerPerks);
    }
}
