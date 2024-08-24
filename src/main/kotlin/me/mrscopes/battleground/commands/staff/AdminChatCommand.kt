package me.mrscopes.battleground.commands.staff

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import me.mrscopes.battleground.utilities.Utilities
import org.bukkit.command.CommandSender

@CommandAlias("adminchat|ac")
@CommandPermission("battleground.admin")
@Description("Chat between all admins")
class AdminChatCommand : BaseCommand() {
    @Default
    fun run(sender: CommandSender, args: Array<String>) {
        val message = args.joinToString()
        Utilities.broadcastToAdmins(("<dark_red><bold>!<reset> <dark_red>[Admin Chat] <red>${sender.name}<dark_gray>: <white>$message"))
    }
}