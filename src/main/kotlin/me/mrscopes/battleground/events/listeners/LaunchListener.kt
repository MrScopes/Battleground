package me.mrscopes.battleground.events.listeners

import me.mrscopes.battleground.utilities.Utilities
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class LaunchListener : Listener {
    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.clickedBlock?.type == Material.POLISHED_BLACKSTONE_PRESSURE_PLATE) {
            val player = event.player

            val direction = player.location.direction.normalize()
            val forwardSpeed = 10.0
            val upwardSpeed = 1.0

            val velocity = direction.multiply(forwardSpeed).setY(upwardSpeed)

            player.velocity = velocity

            val originalChestplate = player.inventory.chestplate

            player.inventory.chestplate = ItemStack(Material.ELYTRA)

            Utilities.after({
                player.inventory.chestplate = originalChestplate
                player.inventory.remove(Material.ELYTRA)
            }, 80)
        }
    }
}