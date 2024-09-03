package me.mrscopes.battleground.enchantments.enchantments.common.melee

import me.mrscopes.battleground.enchantments.Enchantment
import me.mrscopes.battleground.enchantments.EnchantmentRarity
import me.mrscopes.battleground.enchantments.EnchantmentType
import me.mrscopes.battleground.enchantments.listeners.CustomEntityDamageByEntityEvent
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack

class SecondStrike : Enchantment("Second Strike", doubleArrayOf(2.0, 5.0, 7.0, 9.0, 10.0), EnchantmentRarity.COMMON, EnchantmentType.MELEE) {
    override fun activate(item: ItemStack, level: Int, event: Event?): String {
        val evt = event as CustomEntityDamageByEntityEvent

        evt.victim.damage(evt.damage, evt.damager)

        return "Second Strike hit ${evt.victim.name} again"
    }

    override fun description(level: Int): String {
        return "${this.chance[level - 1]}% to attack a second time"
    }
}