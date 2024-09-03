package me.mrscopes.battleground.enchantments.enchantments.legendary.melee

import me.mrscopes.battleground.enchantments.Enchantment
import me.mrscopes.battleground.enchantments.EnchantmentRarity
import me.mrscopes.battleground.enchantments.EnchantmentType
import me.mrscopes.battleground.enchantments.listeners.CustomEntityDamageByEntityEvent
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack

class Judgement : Enchantment("Judgement", doubleArrayOf(0.01, 0.02, 0.03, 0.04, 0.05), EnchantmentRarity.LEGENDARY, EnchantmentType.MELEE) {
    override fun activate(item: ItemStack, level: Int, event: Event?): String {
        val evt = event as CustomEntityDamageByEntityEvent

        if (evt.entity is Player) {
            (evt.entity as Player).damage(99999.0, evt.damager)
        } else {
            evt.entity.remove()
        }

        return "Judgement sent ${evt.victim.name} to hell"
    }

    override fun description(level: Int): String {
        return "${this.chance[level - 1]}% to send your opponent to hell"
    }
}