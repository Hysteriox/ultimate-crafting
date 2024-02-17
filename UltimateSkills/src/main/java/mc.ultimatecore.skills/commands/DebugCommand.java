package mc.ultimatecore.skills.commands;

import mc.ultimatecore.skills.*;
import mc.ultimatecore.skills.objects.abilities.*;
import mc.ultimatecore.skills.utils.*;
import org.bukkit.*;
import org.bukkit.command.*;

import java.util.*;

public class DebugCommand extends HyperCommand {

    public DebugCommand() {
        super(List.of("debug"), "", "", false, "");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if (arguments.length < 1 ) {
            sender.sendMessage("§cNope");
            return;
        }
        System.out.println(arguments[1]);
        try {
            final UUID uuid = UniqueIdUtils.getUniqueId(arguments[1]);
            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            HyperSkills.getInstance().getAbilitiesManager().getUpdate(uuid, playerAbilities -> {
                System.out.println("Debug of: " + uuid);
                sender.sendMessage(ChatColor.GREEN + offlinePlayer.getName() + " (" + uuid + "):");
                for (Ability value : Ability.values()) {
                    sender.sendMessage(ChatColor.GREEN + " > " + value.name() + " = " + playerAbilities.getAbility(value));
                }
            });
        } catch (NumberFormatException exception) {
            sender.sendMessage("§cNope");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return List.of();
    }
}
