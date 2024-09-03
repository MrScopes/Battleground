package me.mrscopes.battleground.enchantments.enchantments.rare.armor

import me.mrscopes.battleground.enchantments.Enchantment
import me.mrscopes.battleground.enchantments.EnchantmentRarity
import me.mrscopes.battleground.enchantments.EnchantmentType
import me.mrscopes.battleground.enchantments.listeners.CustomEntityDamageByEntityEvent
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack

class Reflection : Enchantment("Reflection", doubleArrayOf(2.0, 3.0, 4.0, 6.0, 8.0), EnchantmentRarity.RARE, EnchantmentType.ARMOR) {
    override fun activate(item: ItemStack, level: Int, event: Event?): String {
        val evt = event as CustomEntityDamageByEntityEvent

        val damageReflected = evt.damage * (reflectionPercentage(level) / 100)
        evt.attacker.damage(damageReflected, evt.entity)
        evt.damage -= damageReflected

        return "Reflection reflected ${"%.2f".format(damageReflected)} damage"
    }

    override fun description(level: Int): String {
        return "${this.chance[level - 1]}% to reflect ${reflectionPercentage(level)}% of damage back onto the attacker"
    }

    private fun reflectionPercentage(level: Int): Double {
        return when (level) {
            1 -> 10.0
            2 -> 15.0
            3 -> 20.0
            4 -> 25.0
            5 -> 25.0
            else -> 10.0
        }
    }
}