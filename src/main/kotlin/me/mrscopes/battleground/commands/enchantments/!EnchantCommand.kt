package me.mrscopes.battleground.commands.enchantments

import me.mrscopes.battleground.enchantments.Enchantment
import me.mrscopes.battleground.enchantments.Enchantments
import me.mrscopes.battleground.utilities.miniMessage
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.enchantments.Enchantment as BukkitEnchantment
import org.bukkit.entity.Player

class `!EnchantCommand` : Command("enchant"), TabCompleter {
    val allEnchantments = mutableListOf(
        *BukkitEnchantment.values().map { it.key.toString() }.toTypedArray(),
        *Enchantments.enchantments.map { NamespacedKey("battleground", it.name.replace(" ", "_").lowercase()).toString() }
            .toTypedArray()
    )

    init {
        description = "Enchant your item."
        permission = "battleground.admin"
    }

    override fun execute(sender: CommandSender, command: String, args: Array<String>): Boolean {
        if (args.size < 2) {
            val message = when (args.size) {
                0 -> "Must provide an enchantment"
                else -> "Must provide a level"
            }
            sender.miniMessage(message)
            return true
        }

        val player = sender as Player

        val enchant = args[0]
        val enchantment = resolveEnchantment(enchant)
        if (enchantment == null) {
            player.miniMessage("<red>Invalid enchantment.")
            return true
        }

        val level = args[1].toIntOrNull()

        if (level == null || level < 0 || level > 5) {
            player.miniMessage("<red>Must be between 0 and 5.")
            return true
        }

        if (args.size < 3) {
            val item = player.inventory.itemInMainHand

            if (level == 0) {
                if (enchantment is BukkitEnchantment) {
                    item.removeEnchantment(enchantment)
                } else {
                    Enchantments.removeEnchantment(item, enchantment as Enchantment)
                }
            } else {
                if (enchantment is BukkitEnchantment) {
                    item.addUnsafeEnchantment(enchantment, level)
                } else {
                    Enchantments.addEnchantment(item, enchantment as Enchantment, level)
                }
            }
        } else {
            if (level < 1) {
                player.miniMessage("<red>Must be between 1 and 5.")
                return true
            }

            if (enchantment !is Enchantment) {
                player.miniMessage("<red>Must be a custom enchantment.")
                return true
            }

            val chance = args[2].toDoubleOrNull()
            if (chance == null) {
                player.miniMessage("<red>Chance must be a double.")
                return true
            }

            player.sendMessage("Giving book.")
            player.inventory.addItem(Enchantments.getEnchantedBook(enchantment, level, chance.toDouble()))
        }

        return true
    }

    fun resolveEnchantment(enchantment: String): Any? {
        if (enchantment.isBlank() || !enchantment.contains(":")) return null
        val ench = enchantment.split(":")[1].replace("_", " ").lowercase()

        return if (enchantment.startsWith("minecraft")) {
            BukkitEnchantment.getByName(ench)!!
        } else {
            Enchantments[ench]
        }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String> {
        println("tab")

        if (args.size == 1) {
            allEnchantments.filter { it.startsWith(args[0], ignoreCase = true) }
        }

        if (args.size == 2) {
            return listOf("1", "2", "3", "4", "5").filter { it.startsWith(args[1], ignoreCase = true) }
        }

        return allEnchantments
    }

}

/*
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
 */