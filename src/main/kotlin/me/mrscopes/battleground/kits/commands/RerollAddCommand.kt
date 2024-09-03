package me.mrscopes.battleground.kits.commands

import me.mrscopes.battleground.utilities.miniMessage
import me.mrscopes.battleground.utilities.mongoPlayer
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class RerollAddCommand : Command("rerolladd") {
    init {
        description = "Add rerolls to a user."
        permission = "battleground.admin"
    }

    override fun execute(sender: CommandSender, command: String, args: Array<String>): Boolean {
        if (args.size < 3) {
            val message = when (args.size) {
                0 -> "Tried to add rerolls but no arguments were provided."
                1 -> "Player not provided."
                else -> "Amount of rerolls not provided."
            }
            sender.miniMessage(message)
            return true
        }

        Bukkit.getPlayer(args[1])?.apply {
            val rerolls = args[2].toIntOrNull() ?: return sender.miniMessage("Invalid number of rerolls provided.").let { true }

            val mongoPlayer = this.mongoPlayer()
            mongoPlayer.maxRerolls += rerolls
            mongoPlayer.rerolls += rerolls

            this.miniMessage("You now have ${mongoPlayer.maxRerolls} rerolls.")
            sender.sendMessage("${this.name} now has ${mongoPlayer.maxRerolls} rerolls.")
        } ?: run {
            sender.miniMessage("Player not found.")
            return true
        }

        return true
    }
}
