package me.mrscopes.battleground.enchantments.enchantments.rare.armor

import me.mrscopes.battleground.enchantments.Enchantment
import me.mrscopes.battleground.enchantments.EnchantmentRarity
import me.mrscopes.battleground.enchantments.EnchantmentType
import me.mrscopes.battleground.enchantments.listeners.CustomEntityDamageByEntityEvent
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack

class BounceBack : Enchantment("Bounce Back", doubleArrayOf(5.0, 7.5, 10.0, 12.5, 15.0), EnchantmentRarity.RARE, EnchantmentType.ARMOR) {
    override fun activate(item: ItemStack, level: Int, event: Event?): String? {
        val evt = event as CustomEntityDamageByEntityEvent

        if (evt.victim.health > 6) return null

        val direction = evt.victim.location.toVector().subtract(evt.attacker.location.toVector()).normalize()

        val pushForce = 3.0
        val pushForceVert = 2.0

        evt.victim.velocity = direction.multiply(pushForce)
        evt.victim.velocity.y = pushForceVert

        evt.attacker.velocity = direction.multiply(-pushForce)
        evt.attacker.velocity.y = pushForceVert

        return "Bounce Back pushed both fighters back"
    }

    override fun description(level: Int): String {
        return "${this.chance[level - 1]}% to separate both fighters when you're under 3 hearts."
    }
}