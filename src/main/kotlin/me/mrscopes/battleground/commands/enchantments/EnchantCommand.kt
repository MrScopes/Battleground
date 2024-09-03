package me.mrscopes.battleground.commands.enchantments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands.argument
import io.papermc.paper.command.brigadier.Commands.literal
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
import me.mrscopes.battleground.commands.CommandDescription
import me.mrscopes.battleground.commands.CommandName
import me.mrscopes.battleground.commands.CustomCommand
import me.mrscopes.battleground.commands.playerOnly
import me.mrscopes.battleground.enchantments.Enchantment
import me.mrscopes.battleground.enchantments.Enchantments
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture
import org.bukkit.enchantments.Enchantment as BukkitEnchantment

@CommandName("enchant")
@CommandDescription("Enchant your item.")
class EnchantCommand : CustomCommand() {
    override fun register(): LiteralArgumentBuilder<CommandSourceStack>? {
        return literal(name)
            .requires { it.sender.hasPermission("battleground.admin" )}
            .then(argument("enchantment", EnchantmentCommandArgumentType())
                .then(argument("level", IntegerArgumentType.integer(0, 5))
                    .suggests { _, builder ->
                        val input = builder.remaining
                        (0..5)
                            .filter { it.toString().startsWith(input) }
                            .forEach { builder.suggest(it) }
                        builder.buildFuture()
                    }
                    .executes {
                        playerOnly(it)
                        execute(it.source.sender as Player)
                    }
                )
            )
    }

    fun execute(player: Player): Int {
        return 1
    }
}


class EnchantmentCommandArgumentType : CustomArgumentType<BukkitEnchantmentOrCustomEnchantment, String> {
    private val allEnchantments = mutableListOf(
        *BukkitEnchantment.values().map { it.key.toString() }.toTypedArray(),
        *Enchantments.enchantments.map { "battleground:" + it.name.replace(" ", "_").lowercase() }
            .toTypedArray()
    )

    override fun parse(reader: StringReader): BukkitEnchantmentOrCustomEnchantment {
        val start = reader.cursor

        while (reader.canRead() && reader.peek() !== ' ') {
            reader.skip()
        }

        val input = reader.string.substring(start, reader.cursor)

        println(input)

        // Check for Bukkit enchantments first
        val bukkitEnchantment = BukkitEnchantment.values().find { it.key == NamespacedKey.fromString(input) }
        if (bukkitEnchantment != null) {
            println(bukkitEnchantment.name)
            return BukkitEnchantmentOrCustomEnchantment(bukkitEnchantment = bukkitEnchantment)
        }

        // Check for custom enchantments
        val customEnchantment = Enchantments[input.replace("_", " ").replace("battleground:", "").lowercase()]
        if (customEnchantment != null) {
            println(customEnchantment.name)
            return BukkitEnchantmentOrCustomEnchantment(customEnchantment = customEnchantment)
        }

        throw SimpleCommandExceptionType { "Invalid Enchantment: $input" }.create()
    }

    override fun getExamples(): Collection<String> {
        return allEnchantments
    }

    override fun getNativeType(): ArgumentType<String> {
        return StringArgumentType.word()
    }

    override fun <S: Any?> listSuggestions(context: CommandContext<S>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
        val input = builder.remaining
        val suggestions = allEnchantments.filter { it.startsWith(input, ignoreCase = true) }
        suggestions.forEach {
            builder.suggest(it)
        }
        return builder.buildFuture()
    }
}

data class BukkitEnchantmentOrCustomEnchantment(
    val customEnchantment: Enchantment? = null,
    val bukkitEnchantment: BukkitEnchantment? = null
)

