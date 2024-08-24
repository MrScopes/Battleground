package me.mrscopes.battleground.events.listeners

import me.mrscopes.battleground.utilities.mongoPlayer
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class DamageListener : Listener {
    @EventHandler(priority = EventPriority.LOW)
    fun onDamage(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player || event.entity !is LivingEntity || event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.cause != EntityDamageEvent.DamageCause.PROJECTILE || event.isCancelled) return
        (event.entity as Player).mongoPlayer().lastAttacker = event.entity as Player
    }
}