package mc.ultimatecore.skills.objects.xp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import mc.ultimatecore.skills.objects.SkillType;

@AllArgsConstructor
@Getter
public class GainingXP {
    @Setter
    private boolean isGaining;
    private final SkillType skillType;
    private final double amount;
}
