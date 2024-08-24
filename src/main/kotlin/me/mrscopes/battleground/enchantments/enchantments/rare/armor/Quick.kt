package me.mrscopes.battleground.enchantments.enchantments.rare.armor

import me.mrscopes.battleground.enchantments.Enchantment
import me.mrscopes.battleground.enchantments.EnchantmentRarity
import me.mrscopes.battleground.enchantments.EnchantmentType
import me.mrscopes.battleground.enchantments.Enchantments
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack

class Quick : Enchantment("Quick", doubleArrayOf(), EnchantmentRarity.LEGENDARY, EnchantmentType.ARMOR) {
    override fun activate(item: ItemStack, level: Int, event: Event?): String? {
        Enchantments.addAttribute(
            item,
            Attribute.GENERIC_MOVEMENT_SPEED,
            "quick",
            speedAmount(level),
            AttributeModifier.Operation.ADD_NUMBER
        )
        return null
    }

    override fun description(level: Int): String {
        return "Increases movement speed by ${speedAmount(level) * 1000}%"
    }

    private fun speedAmount(level: Int): Double {
        return when (level) {
            1 -> 0.01
            2 -> 0.015
            3 -> 0.018
            4 -> 0.020
            5 -> 0.0225
            else -> 0.01
        }
    }
}