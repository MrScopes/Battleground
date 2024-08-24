package me.mrscopes.battleground.commands.donator

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import me.mrscopes.battleground.utilities.mongoPlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("color")
@CommandPermission("battleground.color")
@Description("Change your name color")
class ColorCommand : BaseCommand() {
    @Default
    fun run(sender: CommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            (sender as Player).mongoPlayer().nameColor = null
            return
        }

        val color = args.joinToString()
        (sender as Player).mongoPlayer().nameColor = color.replace("\"", "")
    }
}