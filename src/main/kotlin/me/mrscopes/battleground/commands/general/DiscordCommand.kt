package me.mrscopes.battleground.commands.general

import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import me.mrscopes.battleground.commands.CommandDescription
import me.mrscopes.battleground.commands.CommandName
import me.mrscopes.battleground.commands.CustomCommand
import me.mrscopes.battleground.utilities.miniMessage

@CommandName("discord")
@CommandDescription("Join the discord server.")
class DiscordCommand : CustomCommand() {
    override fun execute(ctx: CommandContext<CommandSourceStack>): Int {
        ctx.source.sender.sendMessage("<click:open_url:'https://discord.gg/wjQz6ea6JV'>Click here to join our discord!".miniMessage())
        return 1
    }
}