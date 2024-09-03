package me.mrscopes.battleground.enchantments.enchantments.rare.melee

import me.mrscopes.battleground.enchantments.Enchantment
import me.mrscopes.battleground.enchantments.EnchantmentRarity
import me.mrscopes.battleground.enchantments.EnchantmentType
import me.mrscopes.battleground.enchantments.listeners.CustomEntityDamageByEntityEvent
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack

class Drain : Enchantment("Drain", doubleArrayOf(3.0, 6.0, 8.0, 9.0, 10.0), EnchantmentRarity.RARE, EnchantmentType.MELEE) {
    override fun activate(item: ItemStack, level: Int, event: Event?): String {
        val evt = event as CustomEntityDamageByEntityEvent

        val healAmount = evt.damage * (healPercentage(level) / 100)
        evt.attacker.heal(healAmount)

        return "Drain healed ${evt.attacker.name} by ${"%.2f".format(healAmount)} hp"
    }

    override fun description(level: Int): String {
        return "${this.chance[level - 1]}% to heal ${healPercentage(level)}% of damage dealt"
    }

    private fun healPercentage(level: Int): Double {
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