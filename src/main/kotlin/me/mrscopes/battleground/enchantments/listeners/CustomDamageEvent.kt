package me.mrscopes.battleground.enchantments.listeners

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent

class CustomEntityDamageByEntityEvent(
    damager: Entity,
    damagee: Entity,
    cause: DamageCause,
    damage: Double,
) : EntityDamageByEntityEvent(damager, damagee, cause, damage) {
    var attacker: LivingEntity = damager as LivingEntity
    var victim: LivingEntity = damagee as LivingEntity
}