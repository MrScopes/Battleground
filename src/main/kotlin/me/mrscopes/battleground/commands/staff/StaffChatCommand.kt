package me.mrscopes.battleground.commands.staff

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import me.mrscopes.battleground.utilities.Utilities
import org.bukkit.command.CommandSender

@CommandAlias("staffchat|sc")
@CommandPermission("battleground.mod")
@Description("Chat between all staff members")
class StaffChatCommand : BaseCommand() {
    @Default
    fun run(sender: CommandSender, args: Array<String>) {
        val message = args.joinToString()
        Utilities.broadcastToStaff("<dark_red><bold>!<reset> <gold>[Staff Chat] <yellow>${sender.name}<dark_gray>: <white>$message")
    }
}
