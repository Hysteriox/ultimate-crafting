package mc.ultimatecore.crafting.managers;

import mc.ultimatecore.crafting.*;
import mc.ultimatecore.crafting.gui.recipeeditor.*;
import mc.ultimatecore.crafting.objects.*;
import mc.ultimatecore.crafting.playerdata.User;

import java.util.*;

public class PlayerManager {

    private final Map<UUID, User> users = new HashMap<>();
    private final HyperCrafting plugin;

    public PlayerManager(HyperCrafting plugin) {
        this.plugin = plugin;
    }

    public User createOrGetUser(UUID uuid) {
        return users.computeIfAbsent(uuid, x -> new User(uuid, this.plugin));
    }

    public Optional<User> getUserIfPresent(UUID uuid) {
        return Optional.ofNullable(this.users.get(uuid));
    }

    public void removeUser(UUID uuid) {
        this.users.remove(uuid);
    }

    public void purgeUsers() {
        if (this.users == null) {
            return;
        }
        if (this.users.isEmpty()) {
            return;
        }
        for (Iterator<User> iterator = this.users.values().iterator(); iterator.hasNext(); ) {
            User value = iterator.next();
            this.users.remove(value);
        }
    }

    public RecipeCreatorGUI getRecipeCreatorGUI(CraftingRecipe craftingRecipe){
        return new RecipeCreatorGUI(craftingRecipe, this.plugin);
    }
}
