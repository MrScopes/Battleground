package me.mrscopes.battleground.commands.general

import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import me.mrscopes.battleground.commands.CommandDescription
import me.mrscopes.battleground.commands.CommandName
import me.mrscopes.battleground.commands.CustomCommand
import me.mrscopes.battleground.utilities.miniMessage

@CommandName("github")
@CommandDescription("View the GitHub repository.")
class GitHubCommand : CustomCommand() {
    override fun execute(ctx: CommandContext<CommandSourceStack>): Int {
        ctx.source.sender.miniMessage("<click:open_url:'https://github.com/MrScopes/Battleground'>Click here to view the GitHub repository.")
        return 1
    }
}