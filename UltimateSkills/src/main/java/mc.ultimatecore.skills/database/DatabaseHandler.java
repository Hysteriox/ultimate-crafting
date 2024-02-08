package mc.ultimatecore.skills.database;

import mc.ultimatecore.helper.implementations.*;
import mc.ultimatecore.helper.implementations.object.*;
import mc.ultimatecore.skills.*;

public abstract class DatabaseHandler<V extends DatabaseObject> implements DatabaseTaskHandler<V> {

    protected final HyperSkills plugin;
    protected final Database database;

    public DatabaseHandler(HyperSkills plugin, Database database) {
        this.plugin = plugin;
        this.database = database;
    }
}
