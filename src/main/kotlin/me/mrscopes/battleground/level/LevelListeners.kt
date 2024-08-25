package me.mrscopes.battleground.level

import me.mrscopes.battleground.utilities.miniMessage
import me.mrscopes.battleground.utilities.mongoPlayer
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import java.util.*

class LevelListeners : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.player
        val mongoPlayer = player.mongoPlayer()

        var killer: OfflinePlayer? = when {
            mongoPlayer.lastAttacker != null -> Bukkit.getPlayer(UUID.fromString(mongoPlayer.lastAttacker))
            player.killer != null -> player.killer
            player.lastDamageCause?.entity is Player -> player.lastDamageCause?.entity as Player
            player.lastDamageCause?.entity is OfflinePlayer -> player.lastDamageCause?.entity as OfflinePlayer
            else -> null
        }

        if (killer != null) {
            val mongoKiller = killer.mongoPlayer()
            if (!killer.isOnline) {
                mongoPlayer.xp += 10
            } else {
                killer = killer as Player
                val xp = xp(killer, player)
                mongoKiller.xp += xp.first

                killer.miniMessage("<white>You received $xp for killing ${player.name}.<br>${xp.second}")
            }
        }

        player.mongoPlayer().apply {
            this.xp += 1
        }
    }

    fun xp(attacker: Player, victim: Player) : Pair<Double, String> {
        var xp = 10.0

        val mongoAttacker = attacker.mongoPlayer()
        val mongoVictim = victim.mongoPlayer()

        var adjustments = " <dark_gray> - <gray>Base: <white>10"

        val healthAdjustment = healthAdjustment(attacker.health)
        adjustments += " <dark_gray> - <gray>Your Health (${attacker.health}: <green>+$healthAdjustment\n"
        xp += healthAdjustment

        val levelAdjustment = levelAdjustment(mongoAttacker.xp, mongoVictim.xp)
        val levelAdjustmentDisplay = if (levelAdjustment > 0) "<green>+${levelAdjustment}" else "<red>- $levelAdjustment"
        adjustments += " <dark_gray> - <gray>XP (You ${mongoAttacker.xp} vs Them ${mongoVictim.xp}): $levelAdjustmentDisplay\n"
        xp += healthAdjustment

        return Pair(xp, adjustments)
    }

    fun healthAdjustment(health: Double): Double {
        val minHealth = 0.0
        val maxHealth = 20.0
        val maxAdjustment = 5.0

        val clampedHealth = health.coerceIn(minHealth, maxHealth)

        return (maxAdjustment * (1 - (clampedHealth - minHealth) / (maxHealth - minHealth)))
    }

    fun levelAdjustment(attacker: Double, victim: Double): Double {
        val xpDifference = victim - attacker

        return when {
            xpDifference > 10000 -> 10.0
            xpDifference > 5000 -> 5.0
            xpDifference > 1000 -> 3.0
            else -> -1.0
        }
    }

}