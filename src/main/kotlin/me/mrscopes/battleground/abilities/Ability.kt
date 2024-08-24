package me.mrscopes.battleground.abilities

import org.bukkit.Material
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

open class Ability(
    var name: String,
    var cooldown: DoubleArray,
    var rarity: AbilityRarity,
    var item: Material
): Listener {
    open fun activate(item: ItemStack, level: Int, event: Event?): String? {
        return "Did Nothing"
    }

    open fun description(level: Int): String {
        return "$name enchantment"
    }
}