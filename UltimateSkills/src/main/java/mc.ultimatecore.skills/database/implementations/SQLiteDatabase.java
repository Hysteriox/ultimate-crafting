package mc.ultimatecore.skills.database.implementations;

import mc.ultimatecore.helper.database.Credentials;
import mc.ultimatecore.skills.HyperSkills;
import mc.ultimatecore.skills.database.SQLDatabase;
import org.bukkit.Bukkit;

public class SQLiteDatabase extends SQLDatabase {

	public SQLiteDatabase(HyperSkills plugin, Credentials credentials) {
		super(plugin);
		this.plugin.getLogger().info("Using SQLite (local) database.");
		this.connect(credentials);
	}

	@Override
	public void createTables() {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			execute("CREATE TABLE IF NOT EXISTS Skills (UUID varchar(36) primary key, Data LONGTEXT)");
			execute("CREATE TABLE IF NOT EXISTS Perks (UUID varchar(36) primary key, Data LONGTEXT)");
			execute("CREATE TABLE IF NOT EXISTS Abilities (UUID varchar(36) primary key, Data LONGTEXT)");
		});
	}

    @Override
    public String saveAbilitiesStatement() {
        return "INSERT INTO Abilities (UUID, Data) VALUES (?,?) ON CONFLICT(UUID) DO UPDATE SET Data=?";
    }

    @Override
    public String saveSkillsStatement() {
        return "INSERT INTO Skills (UUID, Data) VALUES (?,?) ON CONFLICT(UUID) DO UPDATE SET Data=?";
    }

    @Override
    public String savePerksStatement() {
        return "INSERT INTO Perks (UUID, Data) VALUES (?,?) ON CONFLICT(UUID) DO UPDATE SET Data=?";
    }
}