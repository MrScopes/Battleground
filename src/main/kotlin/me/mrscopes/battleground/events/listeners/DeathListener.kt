package me.mrscopes.battleground.events.listeners

import me.mrscopes.battleground.Battleground
import me.mrscopes.battleground.utilities.Utilities
import me.mrscopes.battleground.utilities.miniMessage
import me.mrscopes.battleground.utilities.mongoPlayer
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class DeathListener : Listener {
    @EventHandler()
    fun onDeath(event: PlayerDeathEvent) {
        event.deathMessage(event.deathMessage()?.color(NamedTextColor.GRAY))

        val player = event.player
        val mongoPlayer = player.mongoPlayer()

        val killer: OfflinePlayer? = when {
            mongoPlayer.lastAttacker != null -> Bukkit.getPlayer(UUID.fromString(mongoPlayer.lastAttacker))
            player.killer != null -> player.killer
            player.lastDamageCause?.entity is Player -> player.lastDamageCause?.entity as Player
            player.lastDamageCause?.entity is OfflinePlayer -> player.lastDamageCause?.entity as OfflinePlayer
            else -> null
        }

        if (killer != null) {
            val updateKillerData: () -> Unit = {
                killer.mongoPlayer().apply {
                    kills += 1
                    killStreak += 1
                }
            }

            if (Battleground.mongo.mongoPlayers.contains(killer.player?.uniqueId)) {
                updateKillerData()
            } else {
                Utilities.async { updateKillerData() }
            }

            killer.player?.miniMessage("You killed ${player.name}.")
        }

        event.player.inventory.clear()

        mongoPlayer.deaths += 1
        mongoPlayer.killStreak = 0
        mongoPlayer.lastAttacker = null
    }
}
