package me.mrscopes.battleground.enchantments.enchantments.common.melee

import me.mrscopes.battleground.enchantments.Enchantment
import me.mrscopes.battleground.enchantments.EnchantmentRarity
import me.mrscopes.battleground.enchantments.EnchantmentType
import me.mrscopes.battleground.enchantments.listeners.CustomEntityDamageByEntityEvent
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack

class Damage :
    Enchantment("Damage", doubleArrayOf(3.0, 8.0, 12.0, 18.0, 25.0), EnchantmentRarity.COMMON, EnchantmentType.MELEE) {
    override fun activate(item: ItemStack, level: Int, event: Event?): String {
        val evt = event as CustomEntityDamageByEntityEvent

        evt.damage += level * 1

        return "Damage added ${level * 1} damage"
    }

    override fun description(level: Int): String {
        return "${this.chance[level - 1]}% to deal an extra ${level * 1} true damage"
    }
}