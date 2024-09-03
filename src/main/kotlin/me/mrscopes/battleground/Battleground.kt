package me.mrscopes.battleground

import com.mojang.brigadier.LiteralMessage
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import io.papermc.paper.command.brigadier.Commands.argument
import io.papermc.paper.command.brigadier.Commands.literal
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import me.mrscopes.battleground.combatlog.CombatLog
import me.mrscopes.battleground.commands.Commands
import me.mrscopes.battleground.events.Events
import me.mrscopes.battleground.healthbars.HealthBars
import me.mrscopes.battleground.leaderboards.Leaderboards
import me.mrscopes.battleground.mongo.Mongo
import me.mrscopes.battleground.scoreboard.Scoreboard
import me.mrscopes.battleground.utilities.miniMessage
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin


class Battleground : JavaPlugin() {
    override fun onEnable() {
        val manager: LifecycleEventManager<Plugin> = this.lifecycleManager
        manager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            val cmds: io.papermc.paper.command.brigadier.Commands = event.registrar()

            cmds.register(
                literal("new-command")
                    .then(
                        argument("enchantment", StringArgumentType.word())
                        .suggests { context, builder ->
                            val input = builder.remaining.lowercase()

                            Enchantment.values()
                                .filter { it.key.key.startsWith(input) }
                                .forEach { enchantment -> builder.suggest(enchantment.key.key) }

                            builder.buildFuture()
                        }
                        .then(argument("level", IntegerArgumentType.integer(1, 5))
                            .suggests { ctx, builder ->
                                val input = builder.remaining
                                (1..5)
                                    .filter { it.toString().startsWith(input) }
                                    .forEach { builder.suggest(it) }
                                builder.buildFuture()
                            }
                            .executes { ctx ->
                                val enchantmentName = StringArgumentType.getString(ctx, "enchantment")
                                val level = IntegerArgumentType.getInteger(ctx, "level")

                                val enchantment = Enchantment.getByKey(NamespacedKey("minecraft", enchantmentName))
                                    ?: throw SimpleCommandExceptionType(LiteralMessage("Invalid enchantment: $enchantmentName")).create()

                                val player = ctx.source.sender as Player
                                val itemInHand = player.inventory.itemInMainHand
                                if (itemInHand.type.isAir) {
                                    player.sendMessage("You must hold an item to enchant it.")
                                    return@executes 0
                                }

                                itemInHand.addUnsafeEnchantment(enchantment, level)
                                player.sendMessage("Enchanted your item with ${enchantment.key.key} at level $level.")
                                return@executes 1
                            }
                        )
                    )
                    .build(),
                "some bukkit help description string",
                listOf("an-alias"),
            )
        }

        instance = this

        saveDefaultConfig()

        mongo = Mongo(config.getString("mongo url")!!)
        healthBars = HealthBars()
        combatLog = CombatLog()
        scoreboard = Scoreboard()
        leaderboards = Leaderboards()

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            Commands(event.registrar())
        }

        Events.register()

        // cleanup extra entities
        Bukkit.getWorld("world")?.entities?.forEach {
            if (it.scoreboardTags.contains("remove")) {
                it.remove()
            }
        }
    }

    override fun onDisable() {
        healthBars.bars.forEach { healthBars.remove(it.key) }
        leaderboards.kills.remove()
        mongo.client.close()
    }

    companion object {
        lateinit var instance: Battleground
            private set

        lateinit var mongo: Mongo
            private set

        lateinit var healthBars: HealthBars
            private set

        lateinit var combatLog: CombatLog
            private set

        lateinit var scoreboard: Scoreboard
            private set

        lateinit var leaderboards: Leaderboards
            private set

        lateinit var commands: Commands
    }
}