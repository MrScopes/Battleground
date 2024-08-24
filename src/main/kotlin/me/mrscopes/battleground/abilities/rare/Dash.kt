package me.mrscopes.battleground.abilities.rare

import me.mrscopes.battleground.abilities.Ability
import me.mrscopes.battleground.abilities.AbilityRarity
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class Dash :
    Ability("Dash", doubleArrayOf(20.0, 19.0, 17.0, 16.0, 15.0), AbilityRarity.RARE, Material.FIREWORK_ROCKET) {
    override fun activate(item: ItemStack, level: Int, event: Event?): String {
        val evt = event as PlayerInteractEvent
        evt.player.velocity = evt.player.location.direction.multiply(2.0)
        evt.player.world.playSound(evt.player.location, Sound.ENTITY_BLAZE_SHOOT, 1f, 1.5f)
        return "Dashed forward"
    }

    override fun description(level: Int): String {
        return "Pushes the player forwards"
    }
}