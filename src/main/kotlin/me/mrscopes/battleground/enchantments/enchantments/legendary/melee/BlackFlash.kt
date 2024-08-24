package me.mrscopes.battleground.enchantments.enchantments.legendary.melee

import me.mrscopes.battleground.enchantments.Enchantment
import me.mrscopes.battleground.enchantments.EnchantmentRarity
import me.mrscopes.battleground.enchantments.EnchantmentType
import me.mrscopes.battleground.enchantments.listeners.CustomEntityDamageByEntityEvent
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack

class BlackFlash : Enchantment(
    "Black Flash",
    doubleArrayOf(2.0, 4.0, 6.0, 8.0, 10.0),
    EnchantmentRarity.LEGENDARY,
    EnchantmentType.MELEE
) {
    override fun activate(item: ItemStack, level: Int, event: Event?): String {
        val evt = event as CustomEntityDamageByEntityEvent

        val addedDmg = evt.damage * 2.5 - evt.damage
        evt.damage *= 2.5

        val location = evt.entity.location

        location.world?.spawnParticle(Particle.LARGE_SMOKE, location, 20, 0.5, 0.5, 0.5, 0.1)
        location.world?.spawnParticle(Particle.DRAGON_BREATH, location, 10, 0.5, 0.5, 0.5, 0.1)

        location.world?.playSound(location, Sound.ENTITY_WITHER_SHOOT, 1.0f, 1.0f)

        return "Black Flash added ${"%.2f".format(addedDmg)} damage"
    }

    override fun description(level: Int): String {
        return "${this.chance[level - 1]}% to deal 2.5x damage"
    }
}