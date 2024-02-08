package mc.ultimatecore.helper.implementations.object;

import java.util.*;

public record QueueingObject<V extends DatabaseObject>(UUID uuid, V data) {}
