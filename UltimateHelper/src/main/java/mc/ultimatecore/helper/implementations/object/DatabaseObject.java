package mc.ultimatecore.helper.implementations.object;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public abstract class DatabaseObject {

    private final AtomicBoolean updated = new AtomicBoolean(false);
    private final AtomicBoolean remove = new AtomicBoolean(false);
    private final Queue<Runnable> pendingLoadTasks = new LinkedBlockingQueue<>();
    private boolean loading;

    public final boolean isRemoving() {
        return this.remove.get();
    }

    public final void connect() {
        this.remove.set(false);
    }

    public final void disconnect() {
        this.remove.set(true);
    }

    public final boolean hasUpdated() {
        return this.updated.get();
    }

    public final void releaseUpdate() {
        this.updated.set(false);
    }

    public final void setUpdated() {
        this.updated.set(true);
    }

    public final void addPendingTask(Runnable runnable) {
        this.pendingLoadTasks.add(runnable);
    }

    public Queue<Runnable> getPendingLoadTasks() {
        return pendingLoadTasks;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading() {
        this.loading = true;
    }

    public void releaseLoading() {
        this.loading = false;
    }
}
