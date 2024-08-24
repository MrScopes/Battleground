import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import me.mrscopes.battleground.commands.Commands
import me.mrscopes.battleground.enchantments.Enchantments
import me.mrscopes.battleground.utilities.miniMessage
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

@CommandAlias("enchant")
@Description("Enchant your item")
@CommandPermission("battleground.admin")
class EnchantCommand : BaseCommand() {
    init {
        val allEnchantments = mutableListOf(
            *Enchantment.values().map { it.key.toString() }.toTypedArray(),
            *Enchantments.enchantments.map { NamespacedKey("battleground", it.name.replace(" ", "_").lowercase()).toString() }
                .toTypedArray()
        )

        Commands.commandManager.commandCompletions.registerCompletion("enchantments") {
            allEnchantments
        }
    }

    @Default
    @CommandCompletion("@enchantments @range:1-5")
    fun run(sender: CommandSender, enchant: String, @Optional level: Int) {
        val player = sender as Player
        val item = player.inventory.itemInMainHand

        if (level < 1 || level > 5) {
            player.miniMessage("<red>Must be between 1 and 5.")
            return
        }

        val enchantment = resolveEnchantment(enchant)

        if (enchantment is Enchantment) {
            item.addUnsafeEnchantment(enchantment, level)
        } else if (enchantment is me.mrscopes.battleground.enchantments.Enchantment) {
            Enchantments.addEnchantment(item, enchantment, level)
        } else {
            player.miniMessage("<red>Invalid enchantment.")
        }

    }

    fun resolveEnchantment(enchantment: String): Any? {
        val ench = enchantment.split(":")[1].replace("_", " ").lowercase()

        return if (enchantment.startsWith("minecraft")) {
            Enchantment.getByName(ench)!!
        } else {
            Enchantments[ench]
        }
    }

}

