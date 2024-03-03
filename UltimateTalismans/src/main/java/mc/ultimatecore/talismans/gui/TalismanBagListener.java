package mc.ultimatecore.talismans.gui;

import mc.ultimatecore.talismans.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;

public class TalismanBagListener implements Listener {

    private final HyperTalismans plugin;

    public TalismanBagListener(HyperTalismans plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        this.plugin.getUserManager().getGUI(e.getWhoClicked().getUniqueId()).onClick(e);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        this.plugin.getUserManager().getGUI(e.getPlayer().getUniqueId()).onClose(e);
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent e){
        this.plugin.getUserManager().getGUI(e.getPlayer().getUniqueId()).onOpen(e);
    }
}
