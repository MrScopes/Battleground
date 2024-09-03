package me.mrscopes.battleground.enchantments.listeners

import me.mrscopes.battleground.enchantments.EnchantmentType
import me.mrscopes.battleground.enchantments.Enchantments
import me.mrscopes.battleground.enchantments.data
import me.mrscopes.battleground.kits.Kits
import me.mrscopes.battleground.utilities.Utilities
import me.mrscopes.battleground.utilities.miniMessage
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.persistence.PersistentDataType

class BookInventoryClickEvent : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onInventoryClick(event: InventoryClickEvent) {
        val item = event.currentItem ?: return

        val cursor = event.cursor
        println(cursor.type)
        if (cursor.type != Material.ENCHANTED_BOOK) return

        if (!Kits.swords.contains(item.type) && !Kits.helmets.contains(item.type) && !Kits.chestplates.contains(item.type) && !Kits.leggings.contains(item.type) && !Kits.boots.contains(item.type)) return

        event.isCancelled = true

        val enchantments = Enchantments.getEnchantments(cursor)
        if (enchantments.isNullOrEmpty()) {
            return event.whoClicked.miniMessage("<red>Invalid item... no enchantments found.")
        }

        val enchant = enchantments.entries.first()

        if (Kits.swords.contains(item.type) && enchant.key.type != EnchantmentType.MELEE) return event.whoClicked.miniMessage("<red>You can only apply melee enchantments to swords.")
        if ((Kits.helmets.contains(item.type) || Kits.chestplates.contains(item.type) || Kits.leggings.contains(item.type) || Kits.boots.contains(item.type)) && enchant.key.type != EnchantmentType.ARMOR)
            return event.whoClicked.miniMessage("<red>You can only apply armor enchantments to armor.")

        val chance = cursor.data().get("chance", PersistentDataType.DOUBLE)
        if (chance == null) {
            event.whoClicked.miniMessage("<red>Invalid item... no chance found.")
            return
        }

        val itemEnchantments = Enchantments.getEnchantments(item)
        if (itemEnchantments != null) {
            if (itemEnchantments.contains(enchant.key)) {
                println("${itemEnchantments[enchant.key]} , ${enchant.value}")
                if (itemEnchantments[enchant.key]!! <= enchant.value) {
                    event.whoClicked.miniMessage("<red>You already have a higher level of ${enchant.key.name} on that item.")
                    return
                }
            }
        }

        if (!Utilities.chance(chance)) {
            return event.whoClicked.miniMessage("<red>Enchantment application failed with $chance% chance.")
        }

        Enchantments.addEnchantment(item, enchant.key, enchant.value)
        event.whoClicked.miniMessage("<green>Successfully applied ${enchant.key.name} ${enchant.value} with $chance% chance.")

        event.whoClicked.inventory.remove(cursor)
    }
}