package me.mrscopes.battleground.commands.enchantments

import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.Pane
import me.mrscopes.battleground.enchantments.Enchantment
import me.mrscopes.battleground.enchantments.EnchantmentType
import me.mrscopes.battleground.enchantments.Enchantments
import me.mrscopes.battleground.utilities.miniMessage
import me.mrscopes.battleground.utilities.plainText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class `!EnchantmentsCommand` : Command("enchantments") {
    var mainMenu = ChestGui(3, "Enchantments")
    val armorMenu = ChestGui(3, "Armor Enchantments")
    val meleeMenu = ChestGui(3, "Melee Enchantments")
    val bowMenu = ChestGui(3, "Bow Enchantments")

    init {
        description = "View all the enchantments."
        aliases.add("enchants")

        setupMenu(armorMenu, EnchantmentType.ARMOR)
        setupMenu(meleeMenu, EnchantmentType.MELEE)
        setupMenu(bowMenu, EnchantmentType.BOW)
        setupMainMenu()
    }

    fun setupMainMenu() {
        val mainPane = OutlinePane(3, 1, 3, 1)

        mainMenu.setOnGlobalClick { it.isCancelled = true }

        val background = OutlinePane(0, 0, 9, 3, Pane.Priority.LOWEST)
        background.addItem(GuiItem(ItemStack(Material.BLACK_STAINED_GLASS_PANE)))
        background.setRepeat(true)
        mainMenu.addPane(background)

        val items = listOf(
            createMenuItem(
                Material.DIAMOND_HELMET,
                "<!i><blue>Armor Enchantments",
                "<!i><gray>Click to view armor enchantments"
            ),
            createMenuItem(
                Material.IRON_SWORD,
                "<!i><blue>Melee Enchantments",
                "<!i><gray>Click to view sword enchantments"
            ),
            createMenuItem(Material.BOW, "<!i><blue>Bow Enchantments", "<!i><gray>Click to view bow enchantments")
        )

        val menus = listOf(armorMenu, meleeMenu, bowMenu)

        items.zip(menus).forEach { (item, menu) ->
            mainPane.addItem(GuiItem(item) { it.whoClicked.openInventory(menu.inventory) })
        }

        mainMenu.addPane(mainPane)
        mainMenu.update()
    }

    fun setupMenu(menu: ChestGui, type: EnchantmentType) {
        menu.setOnClose { mainMenu.show(it.player) }
        menu.setOnGlobalClick { it.isCancelled = true }

        Enchantments.enchantments
            .filter { it.type == type }
            .sortedBy { it.rarity }
            .forEach { ench ->
                val item = ItemStack(Material.ENCHANTED_BOOK)
                item.editMeta {
                    it.displayName("<!i><enchantment>".miniMessage(Placeholder.component("enchantment", Enchantments.enchantmentDisplay(ench))))
                    it.lore(createEnchantmentLore(ench))
                }
                menu.inventory.addItem(item)
            }

        menu.update()
    }

    fun createMenuItem(material: Material, displayName: String, lore: String): ItemStack {
        val item = ItemStack(material)
        item.editMeta {
            it.displayName(displayName.miniMessage())
            it.lore(listOf(lore.miniMessage()))
        }
        return item
    }

    fun createEnchantmentLore(ench: Enchantment): List<Component> {
        val maxLength = Enchantments.enchantmentDisplay(ench, 5).plainText().length
        val dashes = ("<!i><dark_gray><strikethrough>" + " ".repeat(maxLength)).miniMessage()

        return listOf(
            "<white><!i>${ench.rarity.name.lowercase().replaceFirstChar { it.uppercase() }}".miniMessage(),
            dashes,
            Enchantments.enchantmentDisplay(ench, 1),
            Enchantments.enchantmentDisplay(ench, 2),
            Enchantments.enchantmentDisplay(ench, 3),
            Enchantments.enchantmentDisplay(ench, 4),
            Enchantments.enchantmentDisplay(ench, 5),
            dashes
        )
    }

    override fun execute(sender: CommandSender, command: String, args: Array<String>): Boolean {
        mainMenu.show(sender as Player)
        return true
    }
}