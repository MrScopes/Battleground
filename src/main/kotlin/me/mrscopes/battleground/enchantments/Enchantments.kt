package me.mrscopes.battleground.enchantments

import me.mrscopes.battleground.enchantments.enchantments.common.armor.Reduction
import me.mrscopes.battleground.enchantments.enchantments.common.melee.Damage
import me.mrscopes.battleground.enchantments.enchantments.common.melee.SecondStrike
import me.mrscopes.battleground.enchantments.enchantments.legendary.armor.Tiny
import me.mrscopes.battleground.enchantments.enchantments.legendary.armor.Vitality
import me.mrscopes.battleground.enchantments.enchantments.legendary.melee.BlackFlash
import me.mrscopes.battleground.enchantments.enchantments.legendary.melee.Judgement
import me.mrscopes.battleground.enchantments.enchantments.rare.armor.BounceBack
import me.mrscopes.battleground.enchantments.enchantments.rare.armor.FrostBite
import me.mrscopes.battleground.enchantments.enchantments.rare.armor.Quick
import me.mrscopes.battleground.enchantments.enchantments.rare.armor.Reflection
import me.mrscopes.battleground.enchantments.enchantments.rare.melee.Drain
import me.mrscopes.battleground.enchantments.enchantments.rare.melee.Snare
import me.mrscopes.battleground.enchantments.enchantments.rare.melee.Zeus
import me.mrscopes.battleground.enchantments.listeners.BookInventoryClickEvent
import me.mrscopes.battleground.enchantments.listeners.EnchPlayerDamageListener
import me.mrscopes.battleground.events.Events
import me.mrscopes.battleground.utilities.Utilities
import me.mrscopes.battleground.utilities.miniMessage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

object Enchantments {
    init {
        Events.registerListeners(
            listOf(
                EnchPlayerDamageListener(),
                BookInventoryClickEvent()
            )
        )
    }

    val enchantments = listOf(
        // Melee
        SecondStrike(),
        Damage(),
        Snare(),
        Drain(),
        Judgement(),
        BlackFlash(),
        Zeus(),

        // Armor
        Reduction(),
        FrostBite(),
        Reflection(),
        BounceBack(),
        Tiny(),
        Vitality(),
        Quick(),
    )

    fun attempt(item: ItemStack, event: Event, list: List<String>? = null): String? {
        val enchantments = getEnchantments(item)
        enchantments?.forEach {
            if (list == null || list.contains(it.key.name)) {
                if (it.key.chance.isNotEmpty() && Utilities.chance(it.key.chance[it.value - 1])) {
                    return it.key.activate(item, it.value, event)
                }
            }
        }

        return null
    }

    fun getEnchantments(item: ItemStack): Map<Enchantment, Int>? {
        val enchantmentData = item.data().get("enchantments", PersistentDataType.TAG_CONTAINER) ?: return null

        return enchantmentData.keys.mapNotNull { ench ->
            val enchantment = Enchantments[ench.value().replace("_", " ")] ?: return null
            enchantment to (enchantmentData.get(NamespacedKey("battleground", ench.value()), PersistentDataType.INTEGER)
                ?: return null)
        }.toMap()
    }


    fun addEnchantment(item: ItemStack, enchantment: Enchantment, level: Int) {
        if (getEnchantments(item)?.contains(enchantment) == true) {
            removeEnchantment(item, enchantment)
        }

        item.data().set("enchantments", enchantment.name.lowercase().replace(" ", "_"), level)

        if (enchantment.chance.isEmpty()) {
            enchantment.activate(item, level, null)
        }

        updateLore(item)
    }

    fun removeEnchantment(item: ItemStack, enchantment: Enchantment) {
        item.data().unset("enchantments", enchantment.name.lowercase().replace(" ", "_"))

        if (enchantment.chance.isEmpty()) {
            removeAttribute(item, enchantment.name)
        }

        updateLore(item)
    }

    private fun updateLore(item: ItemStack) {
        val enchantments = getEnchantments(item) ?: return

        item.editMeta { meta ->
            meta.lore(enchantments.map { (enchantment, level) -> enchantmentDisplay(enchantment, level) })
        }
    }

    fun enchantmentDisplay(enchantment: Enchantment, level: Int = 0, showDescription: Boolean = true): Component {
        val color = when (enchantment.rarity) {
            EnchantmentRarity.COMMON -> "<gray>"
            EnchantmentRarity.RARE -> "<blue>"
            EnchantmentRarity.LEGENDARY -> "<rainbow>"
        }

        val component = buildString {
            append("<!i>$color${enchantment.name}")
            if (level > 0) {
                append(" ${Utilities.levelDisplay(level)}")
                if (showDescription) {
                    append(" <dark_gray>(<gray>${enchantment.description(level)}<dark_gray>)")
                }
            }
        }.miniMessage()

        return component
    }


    fun addAttribute(item: ItemStack, attribute: Attribute, key: String, amount: Double, operation: AttributeModifier.Operation) {
        item.editMeta {
            it.addAttributeModifier(
                attribute,
                AttributeModifier(
                    NamespacedKey("enchantments", "attribute_$key"),
                    amount,
                    operation
                )
            )
            item.type.defaultAttributeModifiers.forEach { att, mod ->
                if (it.getAttributeModifiers(att).isNullOrEmpty()) {
                    it.addAttributeModifier(att, AttributeModifier(mod.key, mod.amount, mod.operation))
                }
            }
        }
    }

    fun removeAttribute(item: ItemStack, key: String) {
        item.editMeta {
            it.attributeModifiers?.forEach { attribute, modifier ->
                if (modifier.name == "attribute_${key.lowercase()}") {
                    it.removeAttributeModifier(attribute, modifier)
                }
            }
        }
    }

    fun getEnchantedBook(enchantment: Enchantment, level: Int, chance: Double): ItemStack {
        val book = ItemStack(Material.ENCHANTED_BOOK)

        book.editMeta{
            it.itemName("<!i><enchantment>".miniMessage(Placeholder.component("enchantment", enchantmentDisplay(enchantment, level))))

            val lore = mutableListOf(
                "<!i><gray>${enchantment.description(level)}".miniMessage(),
                "".miniMessage(),
                "<gray>Drag and Click onto an item you want to enchant.".miniMessage()
            )

            if (chance < 100.0) {
                lore.add("<green>Chance: $chance%".miniMessage())
            }

            it.lore(lore)
        }

        val data = book.data()
        data.set("enchantments", enchantment.name.lowercase().replace(" ", "_"), level)
        data.set("chance", chance)

        return book
    }

    operator fun get(name: String): Enchantment? {
        return enchantments.firstOrNull { it.name.equals(name, ignoreCase = true) }
    }
}

enum class EnchantmentRarity {
    COMMON, RARE, LEGENDARY
}

enum class EnchantmentType {
    MELEE, BOW, ARMOR
}