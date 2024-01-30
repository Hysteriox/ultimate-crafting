package mc.ultimatecore.skills;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import mc.ultimatecore.helper.UltimatePlugin;
import mc.ultimatecore.helper.database.Credentials;
import mc.ultimatecore.helper.database.DatabaseType;
import mc.ultimatecore.skills.api.HyperSkillsAPI;
import mc.ultimatecore.skills.api.HyperSkillsAPIImpl;
import mc.ultimatecore.skills.armorequipevent.ArmorListener;
import mc.ultimatecore.skills.commands.CommandManager;
import mc.ultimatecore.skills.configs.*;
import mc.ultimatecore.skills.database.Database;
import mc.ultimatecore.skills.database.implementations.MySQLDatabase;
import mc.ultimatecore.skills.database.implementations.SQLiteDatabase;
import mc.ultimatecore.skills.listener.*;
import mc.ultimatecore.skills.listener.perks.AlchemyPerks;
import mc.ultimatecore.skills.listener.perks.DamageListener;
import mc.ultimatecore.skills.listener.perks.DefenseListener;
import mc.ultimatecore.skills.listener.perks.EnchantingPerks;
import mc.ultimatecore.skills.listener.skills.*;
import mc.ultimatecore.skills.managers.*;
import mc.ultimatecore.skills.objects.DebugType;
import mc.ultimatecore.skills.serializer.GSON;
import mc.ultimatecore.skills.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
public class HyperSkills extends UltimatePlugin {

    private Config configuration;
    private Messages messages;
    private Rewards rewards;
    private Inventories inventories;
    private Requirements requirements;
    private SkillsPoints skillPoints;
    private UltimateItems ultimateItems;
    private HyperSkillsAPI api;
    private Database pluginDatabase;
    private Skills skills;
    private NormalItems normalItems;
    private CommandManager commandManager;
    private AddonsManager addonsManager;
    private SkillManager skillManager;
    private ManaManager manaManager;
    private AbilitiesManager abilitiesManager;
    private PerksManager perksManager;
    private ActionBarManager actionBarManager;
    private ResetDataManager resetDataManager;
    private SpeedManager speedManager;
    private HealthManager healthManager;
    private GSON gson;

    public static HyperSkills getInstance() {
        return HyperSkills.getPlugin(HyperSkills.class);
    }

    @Override
    public void onEnable() {
        getLogger().info("GSON - START");
        this.gson = new GSON();
        getLogger().info("GSON - END");
        getLogger().info("CONFIGS - START");
        loadConfigs();
        getLogger().info("CONFIGS - END");
        getLogger().info("CREDENTIALS - START");
        Credentials credentials = Credentials.fromConfig(configuration.getConfig());
        getLogger().info("CREDENTIALS - END");
        getLogger().info("PLUGIN DATABASE - START");
        this.pluginDatabase = credentials.getDatabaseType() == DatabaseType.MySQL ? new MySQLDatabase(this, credentials) : new SQLiteDatabase(this, credentials);
        getLogger().info("PLUGIN DATABASE - END");
        getLogger().info("SKILLS API - START");
        this.api = new HyperSkillsAPIImpl(this);
        getLogger().info("SKILLS API - END");
        getLogger().info("SKILLS MANAGER - START");
        this.skillManager = new SkillManager(this);
        getLogger().info("SKILLS MANAGER - END");
        getLogger().info("PERKS MANAGER - START");
        this.perksManager = new PerksManager(this);
        getLogger().info("PERKS MANAGER - END");
        getLogger().info("ABILITIES MANAGER - START");
        this.abilitiesManager = new AbilitiesManager(this);
        getLogger().info("ABILITIES MANAGER - END");
        getLogger().info("ADDONS MANAGER - START");
        this.addonsManager = new AddonsManager(this);
        getLogger().info("ADDONS MANAGER - END");
        getLogger().info("COMMAND MANAGER - START");
        this.commandManager = new CommandManager(this);
        getLogger().info("COMMAND MANAGER - END");
        getLogger().info("ACTIONBAR MANAGER - START");
        this.actionBarManager = new ActionBarManager(this);
        getLogger().info("ACTIONBAR MANAGER - END");
        getLogger().info("RESET DATA MANAGER - START");
        this.resetDataManager = new ResetDataManager(this);
        getLogger().info("RESET DATA MANAGER - END");
        getLogger().info("MANA MANAGER - START");
        this.manaManager = new ManaManager(this);
        getLogger().info("MANA MANAGER - END");
        getLogger().info("SPEED MANAGER - START");
        this.speedManager = new SpeedManager(this);
        getLogger().info("SPEED MANAGER - END");
        getLogger().info("HEALTH MANAGER - START");
        this.healthManager = new HealthManager(this);
        getLogger().info("HEALTH MANAGER - END");
        getLogger().info("REGISTER LISTENERS - START");
        registerListeners(actionBarManager, new DamageListener(this), new ArmorListener(new ArrayList<>()), new BlockBreakListener(this), new ArmorSetupListener(), new ArmorEquipListener(), new AlchemyListener(this), XMaterial.getVersion() == 8 ? new MobKillListener_Legacy(this) : new MobKillListener(this), new PlayerJoinLeaveListener(this), new EnchantingListener(this), new FishingListener(this), new AlchemyPerks(), new DefenseListener(this), new EnchantingPerks(), new BlockPlaceListener(getConfiguration()), new ItemStatsListener(this), new InventoryClickListener());
        getLogger().info("REGISTER LISTENERS - END");
        Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e" + getDescription().getName() + " Has been enabled!"));
    }


    @Override
    public void onDisable() {
        if (skillManager != null) skillManager.disable();
        if (perksManager != null) perksManager.disable();
        if (abilitiesManager != null) abilitiesManager.disable();
        if (pluginDatabase != null) pluginDatabase.close();
        Bukkit.getServer().getOnlinePlayers().forEach(HumanEntity::closeInventory);
        ultimateItems.save();
        getLogger().info(getDescription().getName() + " Disabled!");
    }

    public void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    public void sendErrorMessage(Exception e) {
        e.printStackTrace();
    }

    public void loadConfigs() {
        normalItems = new NormalItems(this, "normalitems", false, true);
        ultimateItems = new UltimateItems(this, "ultimateitems", true, true);
        configuration = new Config(this, "config", true, false);
        messages = new Messages(this, "messages", true, false);
        rewards = new Rewards(this, "rewards", true, false);
        requirements = new Requirements(this, "requirements", true, false);
        skillPoints = new SkillsPoints(this, "skillspoints", true, false);
        inventories = new Inventories(this, "inventories", true, false);
        skills = new Skills(this, "skills", true, false);
    }

    public void reloadConfigs() {
        normalItems.reload();
        ultimateItems.reload();
        configuration.reload();
        messages.reload();
        rewards.reload();
        requirements.reload();
        skillPoints.reload();
        inventories.reload();
        skills.reload();
    }

    public void sendDebug(String message, DebugType debugType) {
        if (!configuration.debug) return;
        if (debugType == DebugType.LOG)
            getLogger().info(message);
        else
            Bukkit.getConsoleSender().sendMessage(StringUtils.color(message));
    }

    @Override
    public String getPluginName() {
        return getDescription().getName();
    }
}
