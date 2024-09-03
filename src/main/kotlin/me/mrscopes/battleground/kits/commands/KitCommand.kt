package me.mrscopes.battleground.kits.commands

import me.mrscopes.battleground.kits.Kits
import me.mrscopes.battleground.utilities.Utilities
import me.mrscopes.battleground.utilities.miniMessage
import me.mrscopes.battleground.utilities.mongoPlayer
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class KitCommand : Command("kit") {
    init {
        description = "Get a random kit."
    }

    override fun execute(sender: CommandSender, command: String, args: Array<String>): Boolean {
        val player = sender as Player

        if (!Utilities.isInSpawn(player.location)) {
            player.miniMessage("<red>You must be in spawn to get a new kit.")
            return true
        }

        val mongoPlayer = player.mongoPlayer()
        mongoPlayer.rerolls = mongoPlayer.maxRerolls

        player.inventory.clear()

        player.inventory.helmet = Kits.randomItem(Kits.helmets, Kits.armorEnchantments)
        player.inventory.chestplate = Kits.randomItem(Kits.chestplates, Kits.armorEnchantments)
        player.inventory.leggings = Kits.randomItem(Kits.leggings, Kits.armorEnchantments)
        player.inventory.boots = Kits.randomItem(Kits.boots, Kits.armorEnchantments)

        player.inventory.setItem(0, Kits.randomSword())
        player.inventory.setItem(1, ItemStack(Material.GOLDEN_CARROT))
        player.inventory.setItem(2, Kits.randomAbility())

        player.miniMessage("<br> <blue>| <reset>You have received your kit.<br> <blue>| <reset>Reroll items up to ${mongoPlayer.rerolls} times (/rr)<br> <blue>| <gray><reset>Or reroll your kit (/kit)<br>")

        Utilities.after({ player.heal(100.0) }, 10)

        return true
    }
}