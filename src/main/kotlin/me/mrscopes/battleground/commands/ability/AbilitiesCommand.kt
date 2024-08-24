import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import me.mrscopes.battleground.abilities.Abilities
import me.mrscopes.battleground.utilities.miniMessage
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@CommandAlias("abilities")
@Description("View all the Abilities")
class AbilitiesCommand : BaseCommand() {
    private val mainMenu = ChestGui(3, "Abilities")

    init {
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

    @Default
    fun run(player: Player) {
        player.openInventory(mainMenu.inventory)
    }
}
