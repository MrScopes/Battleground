package me.mrscopes.battleground.commands.general

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands.literal
import me.mrscopes.battleground.commands.CommandDescription
import me.mrscopes.battleground.commands.CommandName
import me.mrscopes.battleground.commands.CustomCommand
import me.mrscopes.battleground.commands.playerOnly
import me.mrscopes.battleground.utilities.miniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

@CommandName("trash")
@CommandDescription("Open a Trash Can.")
class TrashCommand : CustomCommand() {
    override fun register(): LiteralArgumentBuilder<CommandSourceStack>? {
        return literal(name)
            .executes {
                playerOnly(it)
                execute(it.source.sender as Player)
            }
    }

    fun execute(player: Player): Int {
        player.openInventory(Bukkit.createInventory(null, 45, "Trash Can".miniMessage()))
        return 1
    }
}