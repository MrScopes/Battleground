package me.mrscopes.battleground.commands.donator

import me.mrscopes.battleground.utilities.miniMessage
import me.mrscopes.battleground.utilities.mongoPlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class `!ColorCommand` : Command("color") {
    init {
        description = "Change your name color."
        permission = "battleground.color"
    }

    override fun execute(sender: CommandSender, command: String, args: Array<String>): Boolean {
        val player = sender as Player

        if (args.isEmpty()) {
            player.miniMessage("Reset your name color.")
            player.mongoPlayer().nameColor = null
            return true
        }

        val color = args.joinToString()
        val regex = Regex("""<#?[a-zA-Z0-9]+>""")
        val match = regex.find(color)

        if (match == null) {
            player.miniMessage("<red>Invalid color. Try \\<blue> \\<green> or \\<#12345>.")
            return true
        }

        player.mongoPlayer().nameColor = match.value
        return true
    }
}