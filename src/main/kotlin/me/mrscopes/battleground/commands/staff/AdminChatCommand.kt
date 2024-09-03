package me.mrscopes.battleground.commands.staff

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands.argument
import io.papermc.paper.command.brigadier.Commands.literal
import me.mrscopes.battleground.commands.CommandAliases
import me.mrscopes.battleground.commands.CommandDescription
import me.mrscopes.battleground.commands.CommandName
import me.mrscopes.battleground.commands.CustomCommand
import me.mrscopes.battleground.utilities.Utilities
import org.bukkit.command.CommandSender

@CommandName("adminchat")
@CommandDescription("Chat between all admins.")
@CommandAliases("ac")
class AdminChatCommand : CustomCommand() {
    override fun register(): LiteralArgumentBuilder<CommandSourceStack>? {
        return literal(name)
            .requires { it.sender.hasPermission("battleground.admin" )}
            .then(
                argument("message", StringArgumentType.greedyString())
                    .executes {
                        execute(it.source.sender, it.getArgument("message", String::class.java))
                    }
            )
    }

    fun execute(sender: CommandSender, message: String): Int {
        Utilities.broadcastToAdmins(("<dark_red><bold>!<reset> <dark_red>[Admin Chat] <red>${sender.name}<dark_gray>: <white>$message"))

        return 1
    }
}