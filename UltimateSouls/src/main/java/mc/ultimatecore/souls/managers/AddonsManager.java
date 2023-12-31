package mc.ultimatecore.souls.managers;

import lombok.Getter;
import mc.ultimatecore.souls.HyperSouls;
import mc.ultimatecore.souls.addons.ClipPlaceholderAPIManager;
import mc.ultimatecore.souls.addons.MVDWPlaceholderAPIManager;
import mc.ultimatecore.souls.addons.VaultAPIManager;
import mc.ultimatecore.souls.utils.StringUtils;
import org.bukkit.Bukkit;

@Getter
public class AddonsManager {
    
    private final HyperSouls plugin;
    private VaultAPIManager economyPlugin;
    private ClipPlaceholderAPIManager placeholderAPI;
    private MVDWPlaceholderAPIManager mvdwPlaceholderAPI;
    
    public AddonsManager(HyperSouls plugin) {
        this.plugin = plugin;
        load();
    }
    
    public void load() {
        String name = plugin.getDescription().getName();
        if (isPlugin("Vault")) {
            economyPlugin = new VaultAPIManager();
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e[" + name + "] &aSuccessfully hooked into Vault!"));
        }
        if (isPlugin("PlaceholderAPI")) {
            placeholderAPI = new ClipPlaceholderAPIManager(plugin);
            placeholderAPI.register();
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e[" + name + "] &aSuccessfully hooked into PlaceholderAPI!"));
        }
        if (isPlugin("MVDWPlaceholderAPI")) {
            mvdwPlaceholderAPI = new MVDWPlaceholderAPIManager(plugin);
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e[" + name + "] &aSuccessfully hooked into MVDWPlaceholderAPI!"));
        }
    }
    
    private boolean isPlugin(String name) {
        return Bukkit.getServer().getPluginManager().getPlugin(name) != null;
    }
    
}