package mc.ultimatecore.crafting.utils;

import com.cryptomorin.xseries.*;
import de.tr7zw.changeme.nbtapi.*;
import mc.ultimatecore.crafting.*;
import mc.ultimatecore.crafting.objects.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InventoryUtils {

    private static final Map<Integer, ItemStack> cachedNbtItems = new HashMap<>();
    private static final Map<Item, ItemStack> cachedItems = new HashMap<>();

    public static int hashed(Item item, ItemStack it, String nbt) {
        return Objects.hash(item.hashCode(), it, nbt);
    }

    public static ItemStack makeItem(ItemStack item, String name, List<String> lore) {
        ItemMeta m = item.getItemMeta();
        if (item.getItemMeta() == null)
            return null;
        if (lore != null)
            m.setLore(Utils.color(lore));
        m.setDisplayName(Utils.color(name));
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack makeItem(XMaterial material, int amount, String name, List<String> lore) {
        ItemStack item = material.parseItem();
        if (item == null)
            return null;
        item.setAmount(amount);
        ItemMeta m = item.getItemMeta();
        m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if (lore != null && lore.size() > 0)
            m.setLore(Utils.color(lore));
        m.setDisplayName(Utils.color(name == null ? " " : name));
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack makeItem(Item item, ItemStack it, String nbt) {
        int hashed = hashed(item, it, nbt);
        ItemStack result = cachedNbtItems.get(hashed);
        if (result != null) return result;
        long start = System.currentTimeMillis();
        try {
            ItemStack itemStack = it.clone();
            ItemMeta meta = itemStack.getItemMeta();
            String title = getNewTitle(item.title, meta);
            List<String> lore = new ArrayList<String>() {{
                for (String line : item.lore) {
                    if (line.contains("%item_lore%"))
                        addAll(meta == null || !meta.hasLore() ? new ArrayList<>() : meta.getLore());
                    else
                        add(line);
                }
            }};
            ItemStack itemstack = makeItem(itemStack.clone(), title, lore);
            if (item.material == XMaterial.PLAYER_HEAD && item.headData != null) {
                NBTItem nbtItem = new NBTItem(itemstack);
                NBTCompound skull = nbtItem.addCompound("SkullOwner");
                skull.setString("Name", "tr7zw");
                skull.setString("Id", UUID.randomUUID().toString());
                NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
                texture.setString("Value", item.headData);
                return nbtItem.getItem();
            } else if (item.material == XMaterial.PLAYER_HEAD && item.headOwner != null) {
                SkullMeta m = (SkullMeta) itemstack.getItemMeta();
                m.setOwner(item.headOwner);
                itemstack.setItemMeta(m);
            }
            NBTItem nbtItem = new NBTItem(itemstack);
            nbtItem.setBoolean(nbt, true);
            result = nbtItem.getItem();
            cachedNbtItems.put(hashed, result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return makeItem(XMaterial.STONE, item.amount, item.title, item.lore);
        }
    }

    public static String getNewTitle(String title, ItemMeta meta) {
        if (meta == null || !meta.hasDisplayName()) return "";
        return title.replace("%item_title%", meta.getDisplayName());
    }

    public static ItemStack makeItem(Item item) {
        ItemStack result = cachedItems.get(item);
        if (result != null) return result;
        try {
            ItemStack itemstack = makeItem(item.material, item.amount, item.title, item.lore);
            if (item.material == XMaterial.PLAYER_HEAD && item.headData != null) {
                NBTItem nbtItem = new NBTItem(itemstack);
                NBTCompound skull = nbtItem.addCompound("SkullOwner");
                skull.setString("Name", "tr7zw");
                skull.setString("Id", UUID.randomUUID().toString());
                NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
                texture.setString("Value", item.headData);
                return nbtItem.getItem();
            } else if (item.material == XMaterial.PLAYER_HEAD && item.headOwner != null) {
                SkullMeta m = (SkullMeta) itemstack.getItemMeta();
                m.setOwner(item.headOwner);
                itemstack.setItemMeta(m);
            }
            result = itemstack;
            cachedItems.put(item, result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return makeItem(XMaterial.STONE, item.amount, item.title, item.lore);
        }
    }

    public static ItemStack makeItem(Item item, List<Placeholder> placeholders, Category category) {

        try {
            XMaterial material = XMaterial.valueOf(category.getMaterial());
            ItemStack itemstack = makeItem(material, item.amount, Utils.processMultiplePlaceholders(item.title, placeholders), Utils.processMultiplePlaceholders(item.lore, placeholders));
            if (material == XMaterial.PLAYER_HEAD && item.headData != null) {
                NBTItem nbtItem = new NBTItem(itemstack);
                NBTCompound skull = nbtItem.addCompound("SkullOwner");
                skull.setString("Name", "tr7zw");
                skull.setString("Id", UUID.randomUUID().toString());
                NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
                texture.setString("Value", item.headData);
                return nbtItem.getItem();
            }
            if (material == XMaterial.PLAYER_HEAD && item.headOwner != null) {
                SkullMeta m = (SkullMeta) itemstack.getItemMeta();
                m.setOwner(Utils.processMultiplePlaceholders(item.headOwner, placeholders));
                itemstack.setItemMeta(m);
            }
            return itemstack;
        } catch (Exception e) {
            return makeItem(XMaterial.STONE, item.amount, Utils.processMultiplePlaceholders(item.title, placeholders), Utils.processMultiplePlaceholders(item.lore, placeholders));
        }
    }

    public static ItemStack makeItem(Item item, List<Placeholder> placeholders) {
        try {
            ItemStack itemstack = makeItem(item.material, item.amount, Utils.processMultiplePlaceholders(item.title, placeholders), Utils.processMultiplePlaceholders(item.lore, placeholders));
            if (item.material == XMaterial.PLAYER_HEAD && item.headData != null) {
                NBTItem nbtItem = new NBTItem(itemstack);
                NBTCompound skull = nbtItem.addCompound("SkullOwner");
                skull.setString("Name", "tr7zw");
                skull.setString("Id", UUID.randomUUID().toString());
                NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
                texture.setString("Value", item.headData);
                return nbtItem.getItem();
            }
            if (item.material == XMaterial.PLAYER_HEAD && item.headOwner != null) {
                SkullMeta m = (SkullMeta) itemstack.getItemMeta();
                m.setOwner(Utils.processMultiplePlaceholders(item.headOwner, placeholders));
                itemstack.setItemMeta(m);
            }
            return itemstack;
        } catch (Exception e) {
            return makeItem(XMaterial.STONE, item.amount, Utils.processMultiplePlaceholders(item.title, placeholders), Utils.processMultiplePlaceholders(item.lore, placeholders));
        }
    }


    public static ItemStack makeItem(Item item, List<Placeholder> placeholders, Material material) {
        try {
            item.material = XMaterial.matchXMaterial(material);
            ItemStack itemstack = makeItem(item.material, item.amount, Utils.processMultiplePlaceholders(item.title, placeholders), Utils.processMultiplePlaceholders(item.lore, placeholders));
            if (item.material == XMaterial.PLAYER_HEAD && item.headData != null) {
                NBTItem nbtItem = new NBTItem(itemstack);
                NBTCompound skull = nbtItem.addCompound("SkullOwner");
                skull.setString("Name", "tr7zw");
                skull.setString("Id", UUID.randomUUID().toString());
                NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
                texture.setString("Value", item.headData);
                return nbtItem.getItem();
            }
            if (item.material == XMaterial.PLAYER_HEAD && item.headOwner != null) {
                SkullMeta m = (SkullMeta) itemstack.getItemMeta();
                m.setOwner(Utils.processMultiplePlaceholders(item.headOwner, placeholders));
                itemstack.setItemMeta(m);
            }
            return itemstack;
        } catch (Exception e) {
            return makeItem(XMaterial.STONE, item.amount, Utils.processMultiplePlaceholders(item.title, placeholders), Utils.processMultiplePlaceholders(item.lore, placeholders));
        }
    }

    public static ItemStack makeItemHidden(Item item) {
        try {
            ItemStack itemstack = makeItemHidden(item.material, item.amount, item.title, item.lore);
            if (item.material == XMaterial.PLAYER_HEAD && item.headData != null) {
                NBTItem nbtItem = new NBTItem(itemstack);
                NBTCompound skull = nbtItem.addCompound("SkullOwner");
                skull.setString("Name", "tr7zw");
                skull.setString("Id", UUID.randomUUID().toString());
                NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
                texture.setString("Value", item.headData);
                return nbtItem.getItem();
            }
            if (item.material == XMaterial.PLAYER_HEAD && item.headOwner != null) {
                SkullMeta m = (SkullMeta) itemstack.getItemMeta();
                m.setOwner(item.headOwner);
                itemstack.setItemMeta(m);
            }
            return itemstack;
        } catch (Exception e) {
            e.printStackTrace();
            return makeItemHidden(XMaterial.STONE, item.amount, item.title, item.lore);
        }
    }


    public static ItemStack makeItemHidden(XMaterial material, int amount, String name, List<String> lore) {
        ItemStack item = material.parseItem();
        if (item == null)
            return null;
        item.setAmount(amount);
        ItemMeta m = item.getItemMeta();
        m.setLore(Utils.color(lore));
        m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        m.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(m);
        return item;
    }

    public static boolean isEmpty(@Nullable ItemStack itemStack) {
        return itemStack == null || itemStack.getType() == Material.AIR;
    }

    // Null if can not stack
    @Nullable
    public static ItemStack stackitem(@Nullable ItemStack stackItem, @Nullable ItemStack stackTo) {
        if (isEmpty(stackTo)) {
            return stackItem;
        }
        if (isEmpty(stackItem)) {
            return stackTo;
        }

        if (canStackitem(stackItem, stackTo)) {
            stackTo.setAmount(stackItem.getAmount() + stackTo.getAmount());

            return stackTo;
        }

        return null;
    }

    public static boolean canStackitem(@Nullable ItemStack stackItem, @Nullable ItemStack stackTo) {
        if (isEmpty(stackTo)) {
            return true;
        }
        if (isEmpty(stackItem)) {
            return true;
        }

        if (stackTo.isSimilar(stackItem)) {

            return stackItem.getAmount() + stackTo.getAmount() < stackTo.getMaxStackSize();
        }

        return false;
    }
}
