package me.mrscopes.battleground.abilities.listeners

import me.mrscopes.battleground.abilities.Abilities
import me.mrscopes.battleground.utilities.isInSpawn
import me.mrscopes.battleground.utilities.miniMessage
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class AbilityRightClickListener : Listener {
    @EventHandler
    fun onRightClick(event: PlayerInteractEvent) {
        if (event.player.location.isInSpawn()) return
        if (event.item == null || event.item?.type == Material.AIR) return

        Abilities.getAbility(event.item!!) ?: return
        event.isCancelled = true
        val result = Abilities.attempt(event.item!!, event)

        if (result != null) {
            event.player.miniMessage(" <blue>☆ <white>$result")
        }
    }
}