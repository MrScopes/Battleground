package me.mrscopes.battleground.healthbars

import me.mrscopes.battleground.Battleground
import me.mrscopes.battleground.utilities.Utilities
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.player.PlayerTeleportEvent

class HealthBarListeners : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        Battleground.healthBars.add(event.player)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        Battleground.healthBars.remove(event.player.uniqueId)
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        Battleground.healthBars.remove(event.player.uniqueId)
    }

    @EventHandler
    fun onHeal(event: EntityRegainHealthEvent) {
        if (event.entity is Player) {
            val player = event.entity as Player
            Utilities.after({
                Battleground.healthBars.update(player)
            }, 2)
        }
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        if (event.entity is Player) {
            val player = event.entity as Player
            Utilities.after({ Battleground.healthBars.update(player) }, 2)
        }
    }

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        Utilities.after({
            Battleground.healthBars.add(event.player)
        }, 10)
    }

    @EventHandler
    fun onTeleport(event: PlayerTeleportEvent) {
        Battleground.healthBars.remove(event.player.uniqueId)
        Battleground.healthBars.add(event.player)
    }
}
