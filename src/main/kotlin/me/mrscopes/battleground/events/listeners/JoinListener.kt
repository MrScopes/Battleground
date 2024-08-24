package me.mrscopes.battleground.events.listeners

import me.mrscopes.battleground.utilities.display
import me.mrscopes.battleground.utilities.miniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinListener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        event.joinMessage(
            "<dark_gray>[<green>+<dark_gray>] <gray><player>".miniMessage(
                Placeholder.component(
                    "player",
                    player.display()
                )
            )
        )

        if (player.gameMode != GameMode.CREATIVE && player.inventory.isEmpty) {
            player.performCommand("spawn")
            player.performCommand("kit")
        }

        val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        var team = scoreboard.getTeam("d-default")

        if (scoreboard.teams.isEmpty()) {
            scoreboard.getTeam("a-admin")
        }

        if (player.hasPermission("battleground.admin")) {
            team = scoreboard.getTeam("a-admin")
        } else if (player.hasPermission("battleground.mod")) {
            team = scoreboard.getTeam("b-mod")
        } else if (player.hasPermission("battleground.donator")) {
            team = scoreboard.getTeam("c-donator")
        }

        team?.addPlayer(player)

        player.sendPlayerListHeader("<br>     [<yellow>Battleground <reset>]     <br>".miniMessage())
        player.sendPlayerListFooter("<br>     Hello, ${event.player.name}!     <br>     <gray><italic>battleground.minehut.gg     <br>".miniMessage())

        player.playerListName(" <player> ".miniMessage(Placeholder.component("player", player.display())))
    }
}
