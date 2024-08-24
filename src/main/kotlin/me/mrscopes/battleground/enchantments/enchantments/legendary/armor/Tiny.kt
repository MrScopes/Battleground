package me.mrscopes.battleground.enchantments.enchantments.legendary.armor

import me.mrscopes.battleground.enchantments.Enchantment
import me.mrscopes.battleground.enchantments.EnchantmentRarity
import me.mrscopes.battleground.enchantments.EnchantmentType
import me.mrscopes.battleground.enchantments.Enchantments
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack

class Tiny : Enchantment("Tiny", doubleArrayOf(), EnchantmentRarity.LEGENDARY, EnchantmentType.ARMOR) {
    override fun activate(item: ItemStack, level: Int, event: Event?): String? {
        Enchantments.addAttribute(
            item,
            Attribute.GENERIC_SCALE,
            "tiny",
            shrinkAmount(level).unaryMinus(),
            AttributeModifier.Operation.ADD_NUMBER
        )
        return null
    }

    override fun description(level: Int): String {
        return "Shrinks you by ${shrinkAmount(level) * 100}%"
    }

    private fun shrinkAmount(level: Int): Double {
        return when (level) {
            1 -> 0.05
            2 -> 0.075
            3 -> 0.08
            4 -> 0.09
            5 -> 0.1
            else -> 0.0
        }
    }
}