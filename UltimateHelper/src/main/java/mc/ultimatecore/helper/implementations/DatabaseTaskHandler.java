package mc.ultimatecore.helper.implementations;

import mc.ultimatecore.helper.implementations.object.*;
import mc.ultimatecore.helper.utils.*;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

public interface DatabaseTaskHandler<V extends DatabaseObject> {

    String loadStatement();

    String saveStatement();

    void save(UUID uuid, V object, PreparedStatement statement) throws SQLException;

    Tuple<Long, TimeUnit> saveInterval();

    void load(UUID uuid, V object, ResultSet resultSet) throws SQLException;

    Tuple<Long, TimeUnit> loadInterval();

    void autoRemove(UUID uuid);
}
