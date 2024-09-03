package me.mrscopes.battleground.kits.commands

import me.mrscopes.battleground.kits.Kits
import me.mrscopes.battleground.utilities.Utilities
import me.mrscopes.battleground.utilities.miniMessage
import me.mrscopes.battleground.utilities.mongoPlayer
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RerollCommand : Command("reroll") {
    init {
        description = "Reroll an item or ability."
    }

    override fun execute(sender: CommandSender, command: String, args: Array<String>): Boolean {
        val player = sender as Player

        if (!Utilities.isInSpawn(player.location)) {
            player.miniMessage("<red>You must be in spawn to reroll an item.")
            return true
        }

        val item = player.inventory.itemInMainHand

        val mongoPlayer = player.mongoPlayer()
        if (mongoPlayer.rerolls < 1) {
            player.miniMessage("<br> <dark_red>| <red>You are out of rerolls!<br> <dark_red>| <red>Die to get ${mongoPlayer.maxRerolls} more.<br> <dark_red>| <red>Or reroll your kit (/kit)<br>")
            return true
        }

        if (Kits.swords.contains(item.type)) {
            player.inventory.setItemInMainHand(Kits.randomSword())
        } else if (Kits.helmets.contains(item.type)) {
            player.inventory.setItemInMainHand(Kits.randomItem(Kits.helmets, Kits.armorEnchantments))
        } else if (Kits.chestplates.contains(item.type)) {
            player.inventory.setItemInMainHand(Kits.randomItem(Kits.chestplates, Kits.armorEnchantments))
        } else if (Kits.leggings.contains(item.type)) {
            player.inventory.setItemInMainHand(Kits.randomItem(Kits.leggings, Kits.armorEnchantments))
        } else if (Kits.boots.contains(item.type)) {
            player.inventory.setItemInMainHand(Kits.randomItem(Kits.boots, Kits.armorEnchantments))
        } else if (item.type == Material.AIR || item.type == Material.GOLDEN_CARROT) {
            player.miniMessage("<br> <dark_red>| <red>You can't reroll that.<br>")
            return true
        } else {
            player.inventory.setItemInMainHand(Kits.randomAbility())
        }

        mongoPlayer.rerolls -= 1
        player.miniMessage("<br> <dark_green>| <green>You now have ${mongoPlayer.rerolls} rerolls left.<br>")

        player.heal(100.0)

        return true
    }
}