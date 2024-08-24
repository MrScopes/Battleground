package me.mrscopes.battleground.enchantments.enchantments.common.armor

import me.mrscopes.battleground.enchantments.Enchantment
import me.mrscopes.battleground.enchantments.EnchantmentRarity
import me.mrscopes.battleground.enchantments.EnchantmentType
import me.mrscopes.battleground.enchantments.listeners.CustomEntityDamageByEntityEvent
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack

class Reduction : Enchantment(
    "Reduction",
    doubleArrayOf(5.0, 10.0, 15.0, 20.0, 25.0),
    EnchantmentRarity.COMMON,
    EnchantmentType.ARMOR
) {
    override fun activate(item: ItemStack, level: Int, event: Event?): String {
        val evt = event as CustomEntityDamageByEntityEvent

        val damageRemoved = evt.damage * (reductionPercentage(level) / 100)
        evt.damage -= damageRemoved

        return "Reduction removed ${"%.2f".format(damageRemoved)} damage"
    }

    override fun description(level: Int): String {
        return "${this.chance[level - 1]}% to reduce damage by ${reductionPercentage(level)}%"
    }

    private fun reductionPercentage(level: Int): Double {
        return when (level) {
            1 -> 5.0
            2 -> 8.0
            3 -> 10.0
            4 -> 15.0
            5 -> 20.0
            else -> 5.0
        }
    }
}