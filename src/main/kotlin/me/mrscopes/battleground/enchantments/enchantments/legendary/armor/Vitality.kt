package me.mrscopes.battleground.enchantments.enchantments.legendary.armor

import me.mrscopes.battleground.enchantments.Enchantment
import me.mrscopes.battleground.enchantments.EnchantmentRarity
import me.mrscopes.battleground.enchantments.EnchantmentType
import me.mrscopes.battleground.enchantments.Enchantments
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack

class Vitality : Enchantment("Vitality", doubleArrayOf(), EnchantmentRarity.LEGENDARY, EnchantmentType.ARMOR) {
    override fun activate(item: ItemStack, level: Int, event: Event?): String? {
        Enchantments.addAttribute(
            item,
            Attribute.GENERIC_MAX_HEALTH,
            "vitality",
            healthAmount(level),
            AttributeModifier.Operation.ADD_NUMBER
        )
        return null
    }

    override fun description(level: Int): String {
        return "Increases your max health by ${healthAmount(level)}"
    }

    private fun healthAmount(level: Int): Double {
        return when (level) {
            1 -> 2.0
            2 -> 3.0
            3 -> 3.5
            4 -> 4.0
            5 -> 5.0
            else -> 2.0
        }
    }
}