package me.mrscopes.battleground.abilities

import me.mrscopes.battleground.abilities.common.Jump
import me.mrscopes.battleground.abilities.rare.Dash

import me.mrscopes.battleground.enchantments.data
import me.mrscopes.battleground.utilities.Utilities
import me.mrscopes.battleground.utilities.miniMessage
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object Abilities {
    val abilities = mutableListOf(
        Jump(),

        Dash()
    )

    private const val ABILITY_KEY = "ability"
    private const val LEVEL_KEY = "level"
    private const val LAST_USED_KEY = "last_used"

    fun getAbility(item: ItemStack): Triple<Ability, Int, Long>? {
        return item.data().run {
            val abilityData = get(ABILITY_KEY, PersistentDataType.STRING) ?: return null
            val level = get(LEVEL_KEY, PersistentDataType.INTEGER) ?: return null
            val lastUsed = get(LAST_USED_KEY, PersistentDataType.LONG) ?: return null
            abilities.find { it.name == abilityData }?.let { Triple(it, level, lastUsed) }
        }
    }

    fun attempt(item: ItemStack, event: Event): String? {
        val (ability, level, lastUsed) = getAbility(item) ?: return null
        val cooldown = ability.cooldown[level - 1] * 1000
        val currentTime = System.currentTimeMillis()

        return if (currentTime - lastUsed < cooldown) {
            val remainingCooldown = cooldown - (currentTime - lastUsed)
            val remainingTime = remainingCooldown / 1000
            "<gray>${"%.2f".format(remainingTime)} seconds remaining on cooldown."
        } else {
            item.data().set(LAST_USED_KEY, currentTime)
            ability.activate(item, level, event)
        }
    }

    fun abilityItem(ability: Ability, level: Int): ItemStack {
        return ItemStack(ability.item).apply {
            data().apply {
                set(ABILITY_KEY, ability.name)
                set(LEVEL_KEY, level)
                set(LAST_USED_KEY, 0L)
            }
            editMeta {
                it.itemName("Ability: ${getRarityColor(ability.rarity)}${ability.name} ${Utilities.levelDisplay(level)}".miniMessage())
                it.lore(
                    listOf(
                        "<!i><light_purple>${ability.cooldown[level - 1]} second cooldown".miniMessage(),
                        "<!i><gray>${ability.description(level)}".miniMessage()
                    )
                )
            }
        }
    }

    private fun getRarityColor(rarity: AbilityRarity) = when (rarity) {
        AbilityRarity.COMMON -> "<gray>"
        AbilityRarity.RARE -> "<blue>"
        AbilityRarity.LEGENDARY -> "<rainbow>"
    }

    operator fun get(name: String): Ability? {
        return abilities.firstOrNull { it.name.equals(name, ignoreCase = true) }
    }
}

enum class AbilityRarity {
    COMMON, RARE, LEGENDARY
}