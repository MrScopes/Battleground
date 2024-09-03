package me.mrscopes.battleground.commands.staff

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands.argument
import io.papermc.paper.command.brigadier.Commands.literal
import me.mrscopes.battleground.commands.*
import me.mrscopes.battleground.utilities.Utilities
import org.bukkit.command.CommandSender

@CommandName("staffchat")
@CommandDescription("Chat between all staff members.")
@CommandAliases("sc")
class StaffChatCommand : CustomCommand() {
    override fun register(): LiteralArgumentBuilder<CommandSourceStack>? {
        return literal(name)
            .requires { it.sender.hasPermission("battleground.mod" )}
            .then(
                argument("message", StringArgumentType.greedyString())
                    .executes {
                        execute(it.source.sender, it.getArgument("message", String::class.java))
                    }
            )
    }

    fun execute(sender: CommandSender, message: String): Int {
        Utilities.broadcastToStaff("<dark_red><bold>!<reset> <gold>[Staff Chat] <yellow>${sender.name}<dark_gray>: <white>$message")

        return 1
    }
}