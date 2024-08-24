package me.mrscopes.battleground.kits

import me.mrscopes.battleground.abilities.Abilities
import me.mrscopes.battleground.enchantments.EnchantmentType
import me.mrscopes.battleground.enchantments.Enchantments
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

object Kits {
    fun randomItem(itemPool: List<Material>, enchantmentPool: List<Any>): ItemStack {
        val item = ItemStack(itemPool.random())
        handleEnchanting(item, enchantmentPool)
        return item
    }

    fun randomSword(): ItemStack {
        val item = randomItem(swords, swordEnchantments)
        item.editMeta {
            it.addAttributeModifier(
                Attribute.GENERIC_ATTACK_SPEED,
                AttributeModifier(NamespacedKey("battleground", "attack_speed"), 1.0, AttributeModifier.Operation.ADD_NUMBER)
            )
        }
        return item
    }

    fun randomAbility(): ItemStack {
        val ability = Abilities.abilities.random()
        val level = Random.nextInt(1, 6)
        return Abilities.abilityItem(ability, level)
    }

    private fun handleEnchanting(item: ItemStack, enchantments: List<Any>) {
        val enchantmentCount = Random.nextInt(1, 6)
        val enchantmentCopy = enchantments.toMutableList()

        repeat(enchantmentCount) {
            val randomEnchantment = enchantmentCopy.random()
            enchantmentCopy.remove(randomEnchantment)
            val level = Random.nextInt(1, 6)

            if (randomEnchantment is Enchantment) {
                item.addUnsafeEnchantment(randomEnchantment, level)
            } else if (randomEnchantment is me.mrscopes.battleground.enchantments.Enchantment) {
                Enchantments.addEnchantment(item, randomEnchantment, level)
            }
        }
    }

    val swords = listOf(
        Material.IRON_SWORD,
        Material.GOLDEN_SWORD,
        Material.DIAMOND_SWORD,
        Material.NETHERITE_SWORD
    )

    val helmets = listOf(
        Material.IRON_HELMET,
        Material.GOLDEN_HELMET,
        Material.DIAMOND_HELMET,
        Material.NETHERITE_HELMET
    )

    val chestplates = listOf(
        Material.IRON_CHESTPLATE,
        Material.GOLDEN_CHESTPLATE,
        Material.DIAMOND_CHESTPLATE,
        Material.NETHERITE_CHESTPLATE
    )

    val leggings = listOf(
        Material.IRON_LEGGINGS,
        Material.GOLDEN_LEGGINGS,
        Material.DIAMOND_LEGGINGS,
        Material.NETHERITE_LEGGINGS
    )

    val boots = listOf(
        Material.IRON_BOOTS,
        Material.GOLDEN_BOOTS,
        Material.DIAMOND_BOOTS,
        Material.NETHERITE_BOOTS
    )

    val swordEnchantments = listOf(
        Enchantment.SHARPNESS,
        Enchantment.FIRE_ASPECT,
    ) + Enchantments.enchantments.filter { it.type == EnchantmentType.MELEE }

    val armorEnchantments = listOf(
        Enchantment.PROTECTION,
        Enchantment.PROJECTILE_PROTECTION,
        Enchantment.BLAST_PROTECTION,
        Enchantment.THORNS,
    ) + Enchantments.enchantments.filter { it.type == EnchantmentType.ARMOR }
}
