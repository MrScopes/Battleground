package me.mrscopes.battleground.enchantments.listeners

import me.mrscopes.battleground.enchantments.Enchantments
import me.mrscopes.battleground.utilities.miniMessage
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause

class EnchPlayerDamageListener : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onPlayerDamage(event: EntityDamageByEntityEvent) {
        if (event.damager !is LivingEntity || event.entity !is LivingEntity) return
        if (event.cause !in listOf(DamageCause.PROJECTILE, DamageCause.ENTITY_ATTACK)) return

        val attacker = event.damager as LivingEntity
        val victim = event.entity as LivingEntity

        val event = CustomEntityDamageByEntityEvent(attacker, victim, event.cause, event.damage)

        attacker.equipment?.itemInMainHand.takeIf { it?.type != Material.AIR }?.let { hand ->
            Enchantments.attempt(hand, event)?.let { result ->
                attacker.miniMessage(" <gold>☆ <white>$result")
                victim.miniMessage(" <gold>☆ <white>[${attacker.name}] $result")
            }
        }

        victim.equipment?.armorContents?.forEach { armorPiece ->
            if (armorPiece == null || armorPiece.type == Material.AIR) return

            Enchantments.attempt(armorPiece, event)?.let { result ->
                attacker.miniMessage(" <gold>☆ <white>[${victim.name}] $result")
                victim.miniMessage(" <gold>☆ <white>$result")
            }
        }
    }
}