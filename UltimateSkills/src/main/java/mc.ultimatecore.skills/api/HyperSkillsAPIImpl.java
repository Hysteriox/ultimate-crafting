package mc.ultimatecore.skills.api;

import lombok.AllArgsConstructor;
import mc.ultimatecore.skills.HyperSkills;
import mc.ultimatecore.skills.objects.SkillType;
import mc.ultimatecore.skills.objects.abilities.*;
import mc.ultimatecore.skills.objects.perks.Perk;
import mc.ultimatecore.skills.objects.perks.PlayerPerks;

import java.util.*;

@AllArgsConstructor
public class HyperSkillsAPIImpl implements HyperSkillsAPI {

    private final HyperSkills plugin;

    @Override
    public int getLevel(UUID uuid, SkillType skill) {
        return plugin.getSkillManager().getLevel(uuid, skill);
    }

    @Override
    public double getXP(UUID uuid, SkillType skill) {
        return plugin.getSkillManager().getXP(uuid, skill);
    }

    @Override
    public void setLevel(UUID uuid, SkillType skill, int level) {
        plugin.getSkillManager().getUpdate(uuid, skills -> skills.setLevel(skill, level));
    }

    @Override
    public void setXP(UUID uuid, SkillType skill, double xp) {
        plugin.getSkillManager().getUpdate(uuid, skills -> skills.setXP(skill, xp));
    }

    @Override
    public void addLevel(UUID uuid, SkillType skill, int level) {
        plugin.getSkillManager().getUpdate(uuid, skills -> skills.addLevel(skill, level));
    }

    @Override
    public void addXP(UUID uuid, SkillType skill, double xp) {
        plugin.getSkillManager().getUpdate(uuid, skills -> skills.addXP(skill, xp));
    }

    @Override
    public void addAbility(UUID uuid, Ability ability, double quantity) {
        this.plugin.getAbilitiesManager().getUpdate(uuid, data -> data.addAbility(ability, quantity));
    }

    @Override
    public void addArmorAbility(UUID uuid, Ability ability, double quantity) {
        this.plugin.getAbilitiesManager().getUpdate(uuid, data -> data.addArmorAbility(ability, quantity));
    }

    @Override
    public void removeArmorAbility(UUID uuid, Ability ability, double quantity) {
        this.plugin.getAbilitiesManager().getUpdate(uuid, data -> data.removeArmorAbility(ability, quantity));
    }

    @Override
    public void addArmorPerk(UUID uuid, Perk perk, double quantity) {
        this.plugin.getPerksManager().getUpdate(uuid, playerPerks -> playerPerks.addArmorPerk(perk, quantity));
    }

    @Override
    public void removeArmorPerk(UUID uuid, Perk perk, double quantity) {
        this.plugin.getPerksManager().getUpdate(uuid, playerPerks -> playerPerks.removeArmorPerk(perk, quantity));
    }

    @Override
    public void setAbility(UUID uuid, Ability ability, double quantity) {
        this.plugin.getAbilitiesManager().getUpdate(uuid, data -> data.setAbility(ability, quantity));
    }

    @Override
    public void removeAbility(UUID uuid, Ability ability, double quantity) {
        this.plugin.getAbilitiesManager().getUpdate(uuid, data -> data.removeAbility(ability, quantity));
    }

    @Override
    public double getTotalAbility(UUID uuid, Ability ability) {
        double simpleAbility = getSimpleAbility(uuid, ability);
        double extraAbility = getExtraAbility(uuid, ability);
        double total = Math.max(0, simpleAbility + extraAbility);
        if (plugin.getAddonsManager().isMMOItems()) {
            if(ability == Ability.HEALTH) {
                total += plugin.getAddonsManager().getMmoItems().getStats(uuid, ability);
            }
        }
        return ability == Ability.HEALTH ? total + 80 : total;
    }


    @Override
    public double getSimpleAbility(UUID uuid, Ability ability) {
        return plugin.getAbilitiesManager().getAbility(uuid, ability);
    }

    @Override
    public double getExtraAbility(UUID uuid, Ability ability) {
        if (plugin.getAddonsManager().isMMOItems()) {
            return plugin.getAbilitiesManager().getPlayerAbilities(uuid).getArmorAbility(ability) + plugin.getAddonsManager().getMmoItems().getMMOArmor(uuid, ability);
        }else {
            return plugin.getAbilitiesManager().getPlayerAbilities(uuid).getArmorAbility(ability);
        }
    }

    @Override
    public void setPerk(UUID uuid, Perk perk, double quantity) {
        this.plugin.getPerksManager().getUpdate(uuid, playerPerks -> playerPerks.setPerk(perk, quantity));
    }

    @Override
    public void addPerk(UUID uuid, Perk perk, double quantity) {
        this.plugin.getPerksManager().getUpdate(uuid, playerPerks -> playerPerks.addPerk(perk, quantity));
    }

    @Override
    public void removePerk(UUID uuid, Perk perk, double quantity) {
        this.plugin.getPerksManager().getUpdate(uuid, playerPerks -> playerPerks.removePerk(perk, quantity));
    }

    @Override
    public double getSimplePerk(UUID uuid, Perk perk) {
        return plugin.getPerksManager().getPerk(uuid, perk);
    }

    @Override
    public double getExtraPerk(UUID uuid, Perk perk) {
        return plugin.getPerksManager().getPlayerPerks(uuid).getArmorPerk(perk);
    }

    @Override
    public double getRank(UUID uuid, SkillType skillType) {
        return plugin.getSkillManager().getPlayerSkills(uuid).getRankPosition(skillType);
    }

    @Override
    public double getTotalPerk(UUID uuid, Perk perk) {
        PlayerPerks playerPerks = plugin.getPerksManager().getPlayerPerks(uuid);
        if (playerPerks == null) return 0;
        double armorPerk = Math.max(playerPerks.getArmorPerk(perk), 0);
        return playerPerks.getPerk(perk) + armorPerk;
    }

    @Override
    public boolean useMana(UUID uuid, int quantity) {
        // needs implementation
        return false;
    }

    @Override
    public double getMana(UUID uuid) {
        return getTotalAbility(uuid, Ability.INTELLIGENCE);
    }
}