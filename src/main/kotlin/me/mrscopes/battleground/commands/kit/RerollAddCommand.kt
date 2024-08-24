import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.bukkit.contexts.OnlinePlayer
import me.mrscopes.battleground.utilities.miniMessage
import me.mrscopes.battleground.utilities.mongoPlayer
import org.bukkit.command.CommandSender

@CommandAlias("rerolladd")
@Description("Add rerolls to a user")
@CommandPermission("battleground.admin")
class RerollAddCommand : BaseCommand() {
    @Default
    fun run(sender: CommandSender, player: OnlinePlayer, rerolls: Int) {

        val plr = player.player
        val mongoPlayer = plr.mongoPlayer()

        mongoPlayer.rerolls += rerolls
        mongoPlayer.maxRerolls += rerolls

        plr.miniMessage("You now have ${mongoPlayer.maxRerolls} rerolls.")
        sender.sendMessage("${plr.name} now has ${mongoPlayer.maxRerolls} rerolls.")
    }
}