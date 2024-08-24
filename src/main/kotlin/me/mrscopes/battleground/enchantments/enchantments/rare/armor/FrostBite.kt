package me.mrscopes.battleground.enchantments.enchantments.rare.armor

import me.mrscopes.battleground.enchantments.Enchantment
import me.mrscopes.battleground.enchantments.EnchantmentRarity
import me.mrscopes.battleground.enchantments.EnchantmentType
import me.mrscopes.battleground.enchantments.listeners.CustomEntityDamageByEntityEvent
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class FrostBite :
    Enchantment("Frostbite", doubleArrayOf(2.0, 4.0, 8.0, 10.0, 15.0), EnchantmentRarity.RARE, EnchantmentType.ARMOR) {
    override fun activate(item: ItemStack, level: Int, event: Event?): String {
        val evt = event as CustomEntityDamageByEntityEvent

        evt.attacker.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, slowDuration(level), slowLevel(level)))
        evt.attacker.freezeTicks = 200

        return "Frostbite froze ${evt.attacker.name}"
    }

    override fun description(level: Int): String {
        return "${this.chance[level - 1]}% to apply frost and give Slowness ${slowLevel(level) + 1} for ${
            slowDuration(
                level
            ) / 20
        } seconds."
    }

    private fun slowLevel(level: Int): Int {
        return when (level) {
            1, 2 -> 0
            3 -> 1
            4, 5 -> 2
            else -> 0
        }
    }

    private fun slowDuration(level: Int): Int {
        return when (level) {
            1 -> 40
            2, 3 -> 60
            4 -> 60
            5 -> 80
            else -> 40
        }
    }
}