package me.mrscopes.battleground.combatlog

import me.mrscopes.battleground.Battleground
import me.mrscopes.battleground.utilities.miniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerQuitEvent

class CombatLogListeners : Listener {
    val combatTag = Battleground.combatLog

    @EventHandler(ignoreCancelled = true)
    fun onDamage(event: EntityDamageByEntityEvent) {
        if (event.entity is Player && event.damager is Player) {
            val victim = event.entity as Player
            val attacker = event.damager as Player

            val expirationTime = System.currentTimeMillis() + 20000

            val victimCombatData = combatTag.combatTagCache.getIfPresent(victim.uniqueId)
            val attackerCombatData = combatTag.combatTagCache.getIfPresent(attacker.uniqueId)

            if (victimCombatData == null || System.currentTimeMillis() > victimCombatData.expiration) {
                victim.miniMessage("<red>${attacker.name} combat tagged you for 20 seconds.")
            }
            if (attackerCombatData == null || System.currentTimeMillis() > attackerCombatData.expiration) {
                attacker.miniMessage("<red>${victim.name} combat tagged you for 20 seconds.")
            }

            combatTag.combatTagCache.put(victim.uniqueId, CombatTagData(attacker.uniqueId, expirationTime))
            combatTag.combatTagCache.put(attacker.uniqueId, CombatTagData(victim.uniqueId, expirationTime))
        }
    }

    @EventHandler
    fun onPlayerCommand(event: PlayerCommandPreprocessEvent) {
        val player = event.player

        if (player.hasPermission("battleground.mod")) return

        val combatData = combatTag.combatTagCache.getIfPresent(player.uniqueId)
        if (combatData != null && System.currentTimeMillis() < combatData.expiration) {
            event.isCancelled = true
            player.miniMessage("<red>You can't run commands while in combat!")
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        val combatData = combatTag.combatTagCache.getIfPresent(player.uniqueId)

        if (combatData != null && System.currentTimeMillis() < combatData.expiration) {
            player.damage(100.0, combatData.attacker?.let { Bukkit.getPlayer(it) })
            Bukkit.broadcast("<red>${player.name} combat logged!".miniMessage())
        }

        combatTag.combatTagCache.invalidate(player.uniqueId)
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if (combatTag.combatTagCache.getIfPresent(event.player.uniqueId) != null) {
            combatTag.combatTagCache.invalidate(event.player.uniqueId)
        }
    }

}