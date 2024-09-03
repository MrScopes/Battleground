package me.mrscopes.battleground.commands

import io.papermc.paper.command.brigadier.Commands
import me.mrscopes.battleground.commands.general.*
import me.mrscopes.battleground.commands.staff.AdminChatCommand
import me.mrscopes.battleground.commands.staff.StaffChatCommand

class Commands(val manager: Commands) {
    init {
        register(
            SpawnCommand(),
            DiscordCommand(),
            GitHubCommand(),
            TrashCommand(),

            StaffChatCommand(),
            AdminChatCommand()
        )
    }

    fun register(vararg commands: CustomCommand) {
        commands.forEach {
            it.create(manager)
        }
    }
}
