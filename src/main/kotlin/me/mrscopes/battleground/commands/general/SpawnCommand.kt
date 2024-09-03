package me.mrscopes.battleground.commands.general

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands.argument
import io.papermc.paper.command.brigadier.Commands.literal
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import me.mrscopes.battleground.commands.*
import me.mrscopes.battleground.utilities.miniMessage
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

@CommandName("spawn")
@CommandDescription("Teleport to spawn.")
class SpawnCommand : CustomCommand() {
    override fun register(): LiteralArgumentBuilder<CommandSourceStack>? {
        return literal(name)
            .executes {
                playerOnly(it)
                execute(it, it.source.sender as Player)
            }
            .then(
                argument("player", ArgumentTypes.player())
                    .requires { it.sender.hasPermission("battleground.mod") }
                    .executes {
                        val target = it.getArgument("player", PlayerSelectorArgumentResolver::class.java).resolve(it.source).first()
                        execute(it, target)
                    }
                )
    }

    fun execute(ctx: CommandContext<CommandSourceStack>, target: Player): Int {
        target.miniMessage("<gray>Teleporting to spawn.")
        target.passengers.forEach {
            it.removePassenger(it)
        }

        target.teleport(Location(Bukkit.getWorld("world"), 0.5, 116.0, -16.5, -0.35f, 8f))

        if (target != ctx.source.sender) {
            ctx.source.sender.miniMessage("<gray>Teleported ${target.name} to spawn.")
        }

        return 1
    }
}