package me.mrscopes.battleground.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands.literal
import io.papermc.paper.command.brigadier.Commands as PaperCommands

open class CustomCommand {

    val name: String
    val description: String
    val permission: String?
    val aliases: List<String>

    init {
        val clazz = this::class

        var commandName: String? = null
        var commandDescription: String? = null
        var commandPermission: String? = null
        var commandAliases: List<String>? = null

        for (annotation in clazz.annotations) {
            when (annotation) {
                is CommandName -> commandName = annotation.name
                is CommandDescription -> commandDescription = annotation.description
                is CommandPermission -> commandPermission = annotation.permission
                is CommandAliases -> commandAliases = annotation.aliases.toList()
            }
        }

        name = commandName
            ?: throw IllegalArgumentException("CommandName annotation is missing")

        description = commandDescription ?: "Description not provided."

        permission = commandPermission

        aliases = commandAliases ?: listOf()
    }

    open fun create(commands: PaperCommands) {
        val commandNode = this.register()

        commands.register(commandNode!!.build(), description, aliases)
    }

    open fun register(): LiteralArgumentBuilder<CommandSourceStack>? {
        return literal(name)
            .executes {
                execute(it)
            }
    }

    open fun execute(ctx: CommandContext<CommandSourceStack>): Int {
        return 1
    }
}

fun playerOnly(ctx: CommandContext<CommandSourceStack>) {
    if (ctx.source.executor == null)
        throw SimpleCommandExceptionType { "This command is only executable by players." }.create()
}


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandName(val name: String)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandDescription(val description: String)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandPermission(val permission: String)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandAliases(vararg val aliases: String)