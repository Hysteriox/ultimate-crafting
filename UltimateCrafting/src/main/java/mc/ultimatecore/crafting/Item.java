package mc.ultimatecore.crafting;

import com.cryptomorin.xseries.XMaterial;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * Class which represents an item in an inventory.
 * Uses {@link XMaterial} for multi-version support.
 */
@NoArgsConstructor
public class Item {

    public XMaterial material;
    public int amount;
    public String title;
    public String headData;
    public String headOwner;
    public List<String> lore;
    public Integer slot;
    public String command;
    public boolean enabled;

    public Item(Item item) {
        this.material = item.material;
        this.amount = item.amount;
        this.lore = item.lore;
        this.title = item.title;
        this.slot = item.slot;
    }

    public Item(XMaterial material, int amount, String title, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.lore = lore;
        this.title = title;
    }

    public Item(XMaterial material, int slot, int amount, String title, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.lore = lore;
        this.title = title;
        this.slot = slot;
    }

    public Item(XMaterial material, int slot, String headData, int amount, String title, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.lore = lore;
        this.title = title;
        this.slot = slot;
        this.headData = headData;
    }

    public Item(XMaterial material, int slot, int amount, String title, String headOwner, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.lore = lore;
        this.title = title;
        this.headOwner = headOwner;
        this.slot = slot;
    }

    public Item(XMaterial material, int amount, String title, String headOwner, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.lore = lore;
        this.title = title;
        this.headOwner = headOwner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return amount == item.amount && enabled == item.enabled && material == item.material && Objects.equals(title, item.title) && Objects.equals(headData, item.headData) && Objects.equals(headOwner, item.headOwner) && Objects.equals(lore, item.lore) && Objects.equals(slot, item.slot) && Objects.equals(command, item.command);
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, amount, title, headData, headOwner, lore, slot, command, enabled);
    }
}