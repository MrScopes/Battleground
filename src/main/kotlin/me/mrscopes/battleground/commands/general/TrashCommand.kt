package me.mrscopes.battleground.commands.general

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import me.mrscopes.battleground.utilities.miniMessage
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("trash")
@Description("Open a trashcan")
class TrashCommand : BaseCommand() {
    @Default
    fun run(sender: CommandSender) {
        (sender as Player).openInventory(Bukkit.createInventory(null, 45, "Trash Can".miniMessage()))
    }
}