package me.mrscopes.battleground.mongo

import me.mrscopes.battleground.Battleground
import me.mrscopes.battleground.utilities.Utilities
import me.mrscopes.battleground.utilities.mongoPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerQuitEvent

class MongoListeners : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onConnect(event: AsyncPlayerPreLoginEvent) {
        val mongo = Battleground.mongo
        val uuid = event.uniqueId
        val player = mongo.playerFromDatabase(uuid)
        mongo.mongoPlayers[uuid] = player
        mongo.mongoPlayers[uuid]?.lastName = event.playerProfile.name.toString()
        mongo.putPlayerInDatabase(player)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        val mongo = Battleground.mongo

        val mongoPlayer = player.mongoPlayer()
        mongoPlayer.lastAttacker = null

        Utilities.async { mongo.putPlayerInDatabase(mongoPlayer) }

        mongo.mongoPlayers.remove(player.uniqueId)
    }
}