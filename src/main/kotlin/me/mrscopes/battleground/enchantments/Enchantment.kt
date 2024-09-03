package me.mrscopes.battleground.enchantments

import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

open class Enchantment(var name: String, var chance: DoubleArray, var rarity: EnchantmentRarity, var type: EnchantmentType): Listener {
    open fun activate(item: ItemStack, level: Int, event: Event?): String? {
        return "Did Nothing"
    }

    open fun description(level: Int): String {
        return "$name enchantment"
    }
}