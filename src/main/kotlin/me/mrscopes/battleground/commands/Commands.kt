package me.mrscopes.battleground.commands

import AbilitiesCommand
import AbilityCommand
import EnchantCommand
import EnchantmentsCommand
import RerollAddCommand
import co.aikar.commands.BaseCommand
import co.aikar.commands.PaperCommandManager
import me.mrscopes.battleground.Battleground
import me.mrscopes.battleground.commands.general.*
import me.mrscopes.battleground.commands.kit.KitCommand
import me.mrscopes.battleground.commands.kit.RerollCommand
import me.mrscopes.battleground.commands.staff.AdminChatCommand
import me.mrscopes.battleground.commands.donator.ColorCommand
import me.mrscopes.battleground.commands.staff.StaffChatCommand

object Commands {
    val commandManager = PaperCommandManager(Battleground.instance)

    fun register() {
        commandManager.enableUnstableAPI("brigadier")

        registerCommands(
            listOf(
                SpawnCommand(),
                DiscordCommand(),
                GitHubCommand(),
                TrashCommand(),

                ColorCommand(),

                KitCommand(),
                RerollCommand(),
                RerollAddCommand(),

                EnchantCommand(),
                EnchantmentsCommand(),

                AbilityCommand(),
                AbilitiesCommand(),

                StaffChatCommand(),
                AdminChatCommand(),
            )
        )
    }

    private fun registerCommands(commands: List<BaseCommand>) {
        commands.forEach { command -> registerCommand(command) }
    }

    private fun registerCommand(command: BaseCommand) {
        commandManager.registerCommand(command)
    }
}
