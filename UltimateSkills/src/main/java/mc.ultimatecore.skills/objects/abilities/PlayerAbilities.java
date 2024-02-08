package mc.ultimatecore.skills.objects.abilities;

import mc.ultimatecore.helper.implementations.object.*;

import java.util.*;

public class PlayerAbilities extends DatabaseObject {

    private final HashMap<Ability, Double> playerAbilities = new HashMap<>();

    private final HashMap<Ability, Double> armorAbilities = new HashMap<>();

    public PlayerAbilities() {
        for (Ability ability : Ability.values()) {
            armorAbilities.put(ability, 0D);
            playerAbilities.put(ability, 0D);
        }
    }

    public void addAbility(Ability ability, Double quantity) {
        final Double current = playerAbilities.getOrDefault(ability, 0D);
        playerAbilities.put(ability, current + quantity);
    }

    public void removeAbility(Ability ability, Double quantity) {
        final Double current = playerAbilities.getOrDefault(ability, 0D);
        playerAbilities.put(ability, current - quantity);
    }

    public Double getAbility(Ability ability) {
        if (playerAbilities.containsKey(ability))
            return playerAbilities.get(ability);
        return 0D;
    }

    public void setAbility(Ability ability, Double quantity) {
        playerAbilities.put(ability, quantity);
    }

    public Double getArmorAbility(Ability ability) {
        if (armorAbilities.containsKey(ability))
            return armorAbilities.get(ability);
        return 0D;
    }

    public void addArmorAbility(Ability ability, Double quantity) {
        final Double current = armorAbilities.getOrDefault(ability, 0D);
        armorAbilities.put(ability, current + quantity);
    }

    public void removeArmorAbility(Ability ability, Double quantity) {
        final Double current = armorAbilities.getOrDefault(ability, 0D);
        armorAbilities.put(ability, current - quantity);
    }

    public void load(PlayerAbilities fromObject) {
        this.playerAbilities.putAll(fromObject.playerAbilities);
        this.armorAbilities.putAll(fromObject.armorAbilities);
    }
}
