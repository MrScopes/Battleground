package me.mrscopes.battleground.commands.general

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import me.mrscopes.battleground.utilities.miniMessage
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("spawn")
@Description("Teleport to spawn")
class SpawnCommand : BaseCommand() {
    @Default
    fun run(sender: CommandSender) {
        val player = sender as Player
        player.miniMessage("<gray>Teleporting to spawn.")
        player.passengers.forEach {
            player.removePassenger(it)
        }
        player.teleport(Location(player.world, 0.5, 116.0, -16.5, -0.35f, 8f))
    }
}