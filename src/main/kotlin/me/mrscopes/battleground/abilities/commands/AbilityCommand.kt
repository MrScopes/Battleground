package me.mrscopes.battleground.abilities.commands

import me.mrscopes.battleground.abilities.Abilities
import me.mrscopes.battleground.utilities.miniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class AbilityCommand : Command("ability"), TabCompleter {
    init {
        description = "Get an ability."
        permission = "battleground.admin"
    }

    override fun execute(sender: CommandSender, command: String, args: Array<String>): Boolean {
        if (args.size < 2) {
            val message = when (args.size) {
                0 -> "Ability not provided."
                else -> "Level not provided."
            }
            sender.miniMessage(message)
            return true
        }

        val ability = Abilities.abilities.firstOrNull { it.name.equals(args[0], ignoreCase = true) }
            ?: return sender.miniMessage("<red>Invalid Ability").let { true }

        val level = args[1].toIntOrNull()

        if (level == null || level !in 1..5) {
            sender.miniMessage("<red>Level must be between 1 and 5.")
            return true
        }

        (sender as? Player)?.inventory?.addItem(Abilities.abilityItem(ability, level))

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String> {
        if (args.size == 1) {
            Abilities.abilities.filter { it.name.startsWith(args[0], ignoreCase = true) }
        }

        if (args.size == 2) {
            return listOf("1", "2", "3", "4", "5").filter { it.startsWith(args[1], ignoreCase = true) }
        }

        return Abilities.abilities.map { it.name }
    }
}