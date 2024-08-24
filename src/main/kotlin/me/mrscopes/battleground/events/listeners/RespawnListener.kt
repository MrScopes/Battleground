package me.mrscopes.battleground.events.listeners

import me.mrscopes.battleground.Battleground
import me.mrscopes.battleground.utilities.mongoPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

class RespawnListener : Listener {
    @EventHandler
    fun onDeath(event: PlayerRespawnEvent) {
        val mongoPlayer = event.player.mongoPlayer()
        mongoPlayer.rerolls = mongoPlayer.maxRerolls

        event.respawnLocation = Battleground.instance.config.getLocation("spawn")!!
        event.player.performCommand("kit")
    }
}