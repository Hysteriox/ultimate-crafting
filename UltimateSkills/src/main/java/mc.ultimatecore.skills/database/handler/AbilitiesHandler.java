package mc.ultimatecore.skills.database.handler;

import mc.ultimatecore.helper.utils.*;
import mc.ultimatecore.skills.*;
import mc.ultimatecore.skills.api.events.*;
import mc.ultimatecore.skills.database.*;
import mc.ultimatecore.skills.objects.*;
import mc.ultimatecore.skills.objects.abilities.*;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

public class AbilitiesHandler extends DatabaseHandler<PlayerAbilities> {

    public AbilitiesHandler(HyperSkills plugin, SQLDatabase database) {
        super(plugin, database);
    }

    @Override
    public String loadStatement() {
        return "SELECT Data FROM Abilities WHERE UUID=?";
    }

    @Override
    public String saveStatement() {
        return this.database.saveAbilitiesStatement();
    }

    @Override
    public void save(UUID uuid, PlayerAbilities object, PreparedStatement statement) throws SQLException {
        final String gson = this.plugin.getGson().toStringAbilities(object);
        statement.setString(1, uuid.toString());
        statement.setString(2, gson);
        statement.setString(3, gson);
        statement.execute();
    }

    @Override
    public Tuple<Long, TimeUnit> saveInterval() {
        return Tuple.from(5L, TimeUnit.MINUTES);
    }

    @Override
    public void load(UUID uuid, PlayerAbilities object, ResultSet resultSet) throws SQLException {
        final String data = resultSet.getString("Data");
        if (data != null) {
            PlayerAbilities fromString = plugin.getGson().fromStringAbilities(data);
            object.load(fromString);
        }
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new PlayerEnterEvent(player)));
        this.plugin.sendDebug(String.format("Loaded abilities of player %s from database", player.getName()), DebugType.LOG);
    }

    @Override
    public Tuple<Long, TimeUnit> loadInterval() {
        return Tuple.from(500L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void autoRemove(UUID uuid) {
        this.plugin.getAbilitiesManager().remove(uuid);
    }
}
