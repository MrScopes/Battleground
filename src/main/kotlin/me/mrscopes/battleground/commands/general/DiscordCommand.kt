package me.mrscopes.battleground.commands.general

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender

@CommandAlias("discord")
@Description("Join the Discord server.")
class DiscordCommand : BaseCommand() {
    @Default
    fun run(sender: CommandSender) {
        val message = MiniMessage.miniMessage()
            .deserialize("<click:open_url:'https://discord.gg/wjQz6ea6JV'>Click here to join our discord!")
        sender.sendMessage(message)
    }
}