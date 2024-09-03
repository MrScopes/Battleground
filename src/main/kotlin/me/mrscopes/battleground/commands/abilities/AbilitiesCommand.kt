package me.mrscopes.battleground.commands.abilities

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import me.mrscopes.battleground.abilities.Abilities
import me.mrscopes.battleground.utilities.miniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class AbilitiesCommand : Command("abilities") {
    private val mainMenu = ChestGui(3, "Abilities")

    init {
        description = "View all the Abilities."

        setupMainMenu()
    }

    private fun setupMainMenu() {
        mainMenu.setOnGlobalClick { it.isCancelled = true }
        Abilities.abilities.sortedBy { it.rarity }.forEach { ability ->
            val lore = (1..5).map { level ->
                val description = ability.description(level)
                val cooldown = ability.cooldown[level - 1]
                "<!i><gray>$description <light_purple>(${cooldown}s cooldown)".miniMessage()
            }

            mainMenu.inventory.addItem(
                ItemStack(ability.item).apply {
                    editMeta {
                        it.displayName("<!i><blue>${ability.name}".miniMessage())
                        it.lore(lore)
                    }
                }
            )
        }

        mainMenu.update()
    }

    override fun execute(sender: CommandSender, command: String, args: Array<String>): Boolean {
        (sender as Player).openInventory(mainMenu.inventory)
        return true
    }
}
