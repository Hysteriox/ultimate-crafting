package mc.ultimatecore.talismans.gui;

import de.tr7zw.changeme.nbtapi.NBTItem;
import mc.ultimatecore.talismans.HyperTalismans;
import mc.ultimatecore.talismans.objects.Talisman;
import mc.ultimatecore.talismans.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TalismanBagGUI extends GUI implements Listener {

    public TalismanBagGUI(HyperTalismans plugin) {
        super(HyperTalismans.getInstance().getInventories().bagSize, StringUtils.color(plugin.getInventories().bagTitle), plugin);
        plugin.registerListeners(this);
    }

    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null || !e.getClickedInventory().equals(getInventory()) && !e.getInventory().equals(getInventory())) return;
        Player player = (Player) e.getWhoClicked();
        int slot = e.getSlot();
        if(e.getClickedInventory().equals(getInventory())) {
            if (slot == plugin.getInventories().closeButton.slot) {
                e.setCancelled(true);
                player.closeInventory();
            } else if (plugin.getInventories().decorationSlots.contains(slot)) {
                e.setCancelled(true);
            }else{
                ItemStack itemStack = e.getCursor();
                if(itemStack == null || itemStack.getType() == Material.AIR) return;
                NBTItem nbtItem = new NBTItem(itemStack);
                if(!nbtItem.hasKey("uc_talisman")){
                    player.sendMessage(StringUtils.color(plugin.getMessages().getMessage("noTalisman")));
                    e.setCancelled(true);
                }
            }
        }else {
            ItemStack itemStack = e.getCurrentItem();
            if (itemStack == null || itemStack.getType() == Material.AIR) return;
            NBTItem nbtItem = new NBTItem(itemStack);
            if (!nbtItem.hasKey("uc_talisman")) {
                player.sendMessage(StringUtils.color(plugin.getMessages().getMessage("noTalisman")));
                e.setCancelled(true);
            }
        }
    }

    public void onClose(InventoryCloseEvent e) {
        if(!e.getInventory().equals(getInventory())) return;
        List<String> talismans = new ArrayList<>();
        for (ItemStack itemStack : getInventory().getContents()) {
            if(itemStack == null || itemStack.getType().equals(Material.AIR)) continue;
            NBTItem nbtItem = new NBTItem(itemStack);
            if(!nbtItem.hasKey("uc_talisman")) continue;
            String name = nbtItem.getString("uc_talisman_name");
            talismans.add(name);
        }
        Player player = (Player) e.getPlayer();
        plugin.getUserManager().getBagTalismans(player.getUniqueId()).setTalismans(talismans);
    }

    public void onOpen(InventoryOpenEvent e){
        if(!e.getInventory().equals(getInventory())) return;
        getInventory().clear();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> this.addItems(e.getPlayer()));
    }

    public void addItems(HumanEntity player) {
        this.addItems();
        int i = 0;
        List<String> talismans = this.plugin.getUserManager().getBagTalismans(player.getUniqueId()).getTalismans();
        for(String name : talismans){
            Talisman talisman = plugin.getTalismans().getTalisman(name);
            if(talisman == null) continue;
            setItem(i, talisman.getItem());
            i++;
        }
    }
}