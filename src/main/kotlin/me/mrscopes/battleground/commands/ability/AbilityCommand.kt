import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import me.mrscopes.battleground.abilities.Abilities
import me.mrscopes.battleground.commands.Commands
import me.mrscopes.battleground.utilities.miniMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("ability")
@Description("Get an ability")
@CommandPermission("battleground.admin")
class AbilityCommand : BaseCommand() {
    init {
        Commands.commandManager.commandCompletions.registerCompletion("abilities") {
            Abilities.abilities.map { it.name.lowercase() }
        }
    }

    @Default
    @CommandCompletion("@abilities @range:1-5")
    fun run(sender: CommandSender, abilityInput: String, level: Int) {
        if (level !in 1..5) {
            sender.miniMessage("<red>Level must be between 1 and 5.")
            return
        }

        val ability = Abilities.abilities.firstOrNull { it.name.equals(abilityInput, ignoreCase = true) }
            ?: return sender.miniMessage("<red>Invalid Ability")

        (sender as? Player)?.inventory?.addItem(Abilities.abilityItem(ability, level))
    }
}