package me.mrscopes.battleground.enchantments.enchantments.rare.melee

import me.mrscopes.battleground.enchantments.Enchantment
import me.mrscopes.battleground.enchantments.EnchantmentRarity
import me.mrscopes.battleground.enchantments.EnchantmentType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack

class Zeus : Enchantment("Zeus", doubleArrayOf(5.0, 7.0, 9.0, 12.0, 15.0), EnchantmentRarity.RARE, EnchantmentType.MELEE) {
    override fun activate(item: ItemStack, level: Int, event: Event?): String {
        val evt = event as EntityDamageByEntityEvent

        val victim = evt.entity as LivingEntity
        victim.location.world.strikeLightningEffect(victim.location)
        victim.damage(damageAmount(level), evt.damager)

        return "Zeus did ${damageAmount(level)} damage"
    }

    override fun description(level: Int): String {
        return "${this.chance[level - 1]}% to strike lightning and deal ${damageAmount(level)} damage"
    }

    private fun damageAmount(level: Int): Double {
        return when (level) {
            1 -> 3.0
            2 -> 4.0
            3 -> 5.0
            4 -> 5.0
            5 -> 6.0
            else -> 0.0
        }
    }
}