package me.mrscopes.battleground.abilities.common

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import me.mrscopes.battleground.abilities.Abilities
import me.mrscopes.battleground.abilities.Ability
import me.mrscopes.battleground.abilities.AbilityRarity
import me.mrscopes.battleground.events.Events
import me.mrscopes.battleground.utilities.isInSpawn
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class Jump : Ability("Jump", doubleArrayOf(20.0, 19.0, 17.0, 16.0, 15.0), AbilityRarity.COMMON, Material.FEATHER) {
    init {
        Events.registerListener(this)
    }

    override fun activate(item: ItemStack, level: Int, event: Event?): String {
        val evt = event as PlayerInteractEvent
        evt.player.velocity = Vector(0.0, 1.5, 0.0)
        evt.player.world.playSound(evt.player.location, Sound.ENTITY_BAT_TAKEOFF, 1f, 1.5f)
        return "Jump pushes you upwards"
    }

    override fun description(level: Int): String {
        return "Pushes the player upwards"
    }

    @EventHandler(ignoreCancelled = true)
    fun onJump(event: PlayerJumpEvent) {
        if (event.player.location.isInSpawn()) return
        if (!event.player.inventory.contains(Abilities["Jump"]!!.item)) return
        // do something
    }
}