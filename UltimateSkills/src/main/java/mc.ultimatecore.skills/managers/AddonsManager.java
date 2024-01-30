package mc.ultimatecore.skills.managers;

import lombok.Getter;
import mc.ultimatecore.helper.regionwrapper.RegionPluginImpl;
import mc.ultimatecore.helper.regionwrapper.WorldGuard;
import mc.ultimatecore.skills.HyperSkills;
import mc.ultimatecore.skills.addons.*;
import mc.ultimatecore.skills.implementations.EconomyPluginImpl;
import mc.ultimatecore.skills.implementations.ManagerImpl;
import mc.ultimatecore.skills.listener.minions.JetMinionsListener;
import mc.ultimatecore.skills.listener.minions.UltraMinionsListener;
import mc.ultimatecore.skills.listener.mmoitems.AbilityUseEventListener;
import mc.ultimatecore.skills.listener.skills.HyperAnvilListener;
import mc.ultimatecore.skills.listener.skills.HyperCraftingListener;
import mc.ultimatecore.skills.listener.skills.HyperEnchantingListener;
import mc.ultimatecore.skills.listener.skills.HyperRegionListener;
import mc.ultimatecore.skills.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.List;

@Getter
public class AddonsManager extends ManagerImpl {

    private MMOItemsAPIManager mmoItems;
    private RegionPluginImpl regionPlugin;
    private MythicMobsAPIManager mythicMobs;
    private EconomyPluginImpl economyPlugin;
    private ClipPlaceholderAPIManager placeholderAPI;
    private MVDWPlaceholderAPIManager mvdwPlaceholderAPI;
    private CitizensAPIManager citizensAPIManager;
    private EcoEnchantsAPIManager ecoEnchants;

    public AddonsManager(HyperSkills plugin) {
        super(plugin);
        load();
    }

    @Override
    public void load() {
        System.out.println("LOADING ADDONS");
        String name = plugin.getDescription().getName();
        System.out.println("> 1");
        if (isPlugin("UltimateCore-Crafting")) {
            System.out.println("> 2");
            registerListeners(new HyperCraftingListener(plugin));
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e[" + name + "] &aSuccessfully hooked into HyperCrafting!"));
            System.out.println("> 3");
        }
        if (isPlugin("UltimateCore-Enchantment")) {
            System.out.println("> 4");
            registerListeners(new HyperEnchantingListener(plugin));
            System.out.println("> 6");
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e[" + name + "] &aSuccessfully hooked into HyperEnchants!"));
            System.out.println("> 7");
        }
        if (isPlugin("UltimateCore-Anvil")) {
            System.out.println("> 8");
            registerListeners(new HyperAnvilListener(plugin));
            System.out.println("> 9");
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e[" + name + "] &aSuccessfully hooked into HyperAnvil!"));
            System.out.println("> 10");
        }
        if (isPlugin("UltimateCore-Farm")) {
            System.out.println("> 11");
            registerListeners(new HyperRegionListener(plugin));
            System.out.println("> 12");
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e[" + name + "] &aSuccessfully hooked into HyperRegions!"));
            System.out.println("> 13");
        }
        if (isPlugin("MMOItems") && isPlugin("MythicLib")) {
            System.out.println("> 14");
            mmoItems = new MMOItemsAPIManager("MMOItems & MythicLib");
            System.out.println("> 15");
            registerListeners(new AbilityUseEventListener(plugin.getConfiguration().allowHunger));
            System.out.println("> 16");
        }
        if (isPlugin("Residence")) {
            System.out.println("> 17");
            regionPlugin = new ResidenceSupportAPIManager("Residence");
            System.out.println("> 18");
        } else if (isPlugin("WorldGuard")) {
            if (isPlugin("WorldEdit") || isPlugin("FastAsyncWorldEdit")) {
                System.out.println("> 19");
                regionPlugin = new WorldGuard();
                System.out.println("> 20");
                Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e[" + name + "] &aSuccessfully hooked into WorldEdit && WorldGuard!"));
                System.out.println("> 21");
            }
        } else if (isPlugin("UltraRegions")) {
            System.out.println("> 22");
            regionPlugin = new UltraRegionsAPIManager("UltraRegions");
            System.out.println("> 23");
        }
        if (isPlugin("MythicMobs")) {
            System.out.println("> 24");
            mythicMobs = new MythicMobsAPIManager("MythicMobs");
            System.out.println("> 25");
        }
        if (isPlugin("Vault")) {
            System.out.println("> 26");
            economyPlugin = new VaultAPIManager("Vault");
            System.out.println("> 27");
        }
        if (isPlugin("Citizens")) {
            System.out.println("> 28");
            citizensAPIManager = new CitizensAPIManager("Citizens");
            System.out.println("> 29");

        }
        if (isPlugin("PlaceholderAPI")) {
            System.out.println("> 30");
            placeholderAPI = new ClipPlaceholderAPIManager(plugin);
            System.out.println("> 31");
            placeholderAPI.register();
            System.out.println("> 32");
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e[" + name + "] &aSuccessfully hooked into PlaceholderAPI!"));
            System.out.println("> 33");
        }
        if (isPlugin("MVDWPlaceholderAPI")) {
            System.out.println("> 34");
            mvdwPlaceholderAPI = new MVDWPlaceholderAPIManager(plugin);
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e[" + name + "] &aSuccessfully hooked into MVDWPlaceholderAPI!"));
            System.out.println("> 35");
        }
        if (isPlugin("JetsMinions") && plugin.getConfiguration().jetMinions) {
            System.out.println("> 36");
            registerListeners(new JetMinionsListener(plugin));
            System.out.println("> 37");
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e[" + name + "] &aSuccessfully hooked into JetMinions!"));
            System.out.println("> 38");
        }
        if (isPlugin("UltraMinions") && plugin.getConfiguration().ultraMinions) {
            System.out.println("> 39");
            registerListeners(new UltraMinionsListener(plugin));
            System.out.println("> 40");
            System.out.println("> 41");
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e[" + name + "] &aSuccessfully hooked into UltraMinions!"));
        }
        if (isPlugin("EcoEnchants")) {
            System.out.println("> 43");
            ecoEnchants = new EcoEnchantsAPIManager("EcoEnchants");
            System.out.println("> 44");
        }

        System.out.println("FIN DU CHARGEMENT TROU DE BALE");

    }

    public void registerListeners(Listener... listener) {
        for (Listener l : listener)
            Bukkit.getPluginManager().registerEvents(l, HyperSkills.getInstance());
    }

    private boolean isPlugin(String name) {
        return Bukkit.getServer().getPluginManager().getPlugin(name) != null;
    }

    private String getPluginVersion(String name) {
        return Bukkit.getServer().getPluginManager().getPlugin(name).getDescription().getVersion();
    }

    private List<String> getPluginAuthor(String name) {
        return Bukkit.getServer().getPluginManager().getPlugin(name).getDescription().getAuthors();
    }

    public boolean isMMOItems() {
        return mmoItems != null;
    }

    public boolean isEcoEnchants() {
        return ecoEnchants != null;
    }

}