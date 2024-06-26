package mc.ultimatecore.crafting.nms;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.*;

import java.util.*;

public class v1_16_R3 implements VanillaCraftingSource {

    @Override
    public Recipe getRecipe(org.bukkit.inventory.ItemStack[] craftingMatrix, World world) {
        Container container = new Container(null, -1) {
            public InventoryView getBukkitView() {
                return null;
            }

            public boolean canUse(EntityHuman entityhuman) {
                return false;
            }
        };

        InventoryCrafting inventoryCrafting = new InventoryCrafting(container, 3, 3);
        return this.getNMSRecipe(craftingMatrix, inventoryCrafting, (CraftWorld) world).map(IRecipe::toBukkitRecipe).orElse(null);
    }

    private Optional<RecipeCrafting> getNMSRecipe(ItemStack[] craftingMatrix, InventoryCrafting inventoryCrafting, CraftWorld world) {
        Object[] formatted = new String[craftingMatrix.length];
        for (int i = 0; i < craftingMatrix.length; ++i) {
            final ItemStack itemStack = craftingMatrix[i];
            formatted[i] = itemStack == null ? "Air" : itemStack.getType().name();
            inventoryCrafting.setItem(i, CraftItemStack.asNMSCopy(itemStack));
        }

        return MinecraftServer.getServer().getCraftingManager().craft(Recipes.CRAFTING, inventoryCrafting, world.getHandle());
    }

    @Override
    public ItemStack[] getRemainingItemsForCrafting(ItemStack[] craftingMatrix, World world) {
        Container container = new Container(null, -1) {
            public InventoryView getBukkitView() {
                return null;
            }

            public boolean canUse(EntityHuman entityhuman) {
                return false;
            }
        };

        InventoryCrafting crafting = new InventoryCrafting(container, 3, 3);
        for (int i = 0; i < craftingMatrix.length; ++i) {
            crafting.setItem(i, CraftItemStack.asNMSCopy(craftingMatrix[i]));
        }

        net.minecraft.server.v1_16_R3.ItemStack[] nmsItems = MinecraftServer.getServer().getCraftingManager().c(Recipes.CRAFTING, crafting, ((CraftWorld) world).getHandle()).toArray(new net.minecraft.server.v1_16_R3.ItemStack[0]);
        ItemStack[] bukkitItems = new ItemStack[nmsItems.length];
        for (int i = 0; i < nmsItems.length; i++) {
            bukkitItems[i] = CraftItemStack.asBukkitCopy(nmsItems[i]);
        }

        return bukkitItems;
    }
}