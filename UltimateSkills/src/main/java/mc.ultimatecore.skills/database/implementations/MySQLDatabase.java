package mc.ultimatecore.skills.database.implementations;

import mc.ultimatecore.helper.database.Credentials;
import mc.ultimatecore.skills.HyperSkills;
import mc.ultimatecore.skills.database.SQLDatabase;

public class MySQLDatabase extends SQLDatabase {

	public MySQLDatabase(HyperSkills parent, Credentials credentials) {
		super(parent);
		this.connect(credentials);
	}

    @Override
    public String saveAbilitiesStatement() {
        return "INSERT INTO Abilities (UUID, Data) VALUES (?,?) ON DUPLICATE KEY UPDATE Data=?";
    }

    @Override
    public String saveSkillsStatement() {
        return "INSERT INTO Skills (UUID, Data) VALUES (?,?) ON DUPLICATE KEY UPDATE Data=?";
    }

    @Override
    public String savePerksStatement() {
        return "INSERT INTO Perks (UUID, Data) VALUES (?,?) ON DUPLICATE KEY UPDATE Data=?";
    }
}