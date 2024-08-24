package me.mrscopes.battleground.commands.general

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender

@CommandAlias("github")
@Description("View the github repository.")
class GitHubCommand : BaseCommand() {
    @Default
    fun run(sender: CommandSender) {
        val message = MiniMessage.miniMessage()
            .deserialize("<click:open_url:'https://github.com/MrScopes/Battleground'>Click here to view the GitHub repository.")
        sender.sendMessage(message)
    }
}