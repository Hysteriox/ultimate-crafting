package mc.ultimatecore.helper.implementations;

import mc.ultimatecore.helper.implementations.object.*;
import mc.ultimatecore.helper.utils.*;
import org.bukkit.*;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

public class DatabaseTask<V extends DatabaseObject> {

    private final DatabaseImpl impl;
    private final ScheduledExecutorService scheduledTaskService;
    private final Queue<QueueingObject<V>> saveQueue = new LinkedBlockingQueue<>();
    private final Queue<QueueingObject<V>> loadQueue = new LinkedBlockingQueue<>();
    private final DatabaseTaskHandler<V> taskHandler;

    public DatabaseTask(DatabaseImpl impl, DatabaseTaskHandler<V> handler) {
        this.impl = impl;
        this.taskHandler = handler;
        this.scheduledTaskService = Executors.newScheduledThreadPool(2);
        final Tuple<Long, TimeUnit> saveInterval = handler.saveInterval();
        final Tuple<Long, TimeUnit> loadInterval = handler.loadInterval();
        this.scheduledTaskService.scheduleAtFixedRate(this::saveQueueingData, saveInterval.key(), saveInterval.key(), saveInterval.value());
        this.scheduledTaskService.scheduleAtFixedRate(this::loadQueueingData, loadInterval.key(), loadInterval.key(), loadInterval.value());
    }

    protected void close() {
        this.scheduledTaskService.shutdownNow().forEach(Runnable::run);
        this.saveQueueingData();
    }

    public synchronized void loadQueueingData() {
        if (this.loadQueue.isEmpty()) return;
        try (Connection c = this.impl.hikari.getConnection(); PreparedStatement statement = c.prepareStatement(this.taskHandler.loadStatement())) {
            QueueingObject<V> object;
            while ((object = this.loadQueue.poll()) != null) {
                final UUID uuid = object.uuid();
                final V data = object.data();
                data.releaseUpdate();
                statement.setString(1, uuid.toString());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    this.taskHandler.load(uuid, data, resultSet);
                    Runnable runnable;
                    while ((runnable = data.getPendingLoadTasks().poll()) != null) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(this.impl.plugin, runnable);
                    }
                    data.releaseLoading();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("An error occurred while loading data", e);
        }
    }

    public synchronized void saveQueueingData() {
        if (this.saveQueue.isEmpty()) return;
        try (Connection c = this.impl.hikari.getConnection(); PreparedStatement statement = c.prepareStatement(this.taskHandler.saveStatement())) {
            QueueingObject<V> object;
            while ((object = this.saveQueue.poll()) != null) {
                final UUID uuid = object.uuid();
                final V data = object.data();
                data.releaseUpdate();
                this.taskHandler.save(uuid, data, statement);
                if (data.isRemoving()) {
                    this.taskHandler.autoRemove(uuid);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("An error occurred while saving data", e);
        }
    }

    public void save(UUID uuid, V object) {
        if (object.hasUpdated()) return;
        object.setUpdated();
        this.saveQueue.add(new QueueingObject<>(uuid, object));
    }

    public void load(UUID uuid, V object) {
        if (object.isLoading()) return;
        object.setLoading();
        this.loadQueue.add(new QueueingObject<>(uuid, object));
    }
}
