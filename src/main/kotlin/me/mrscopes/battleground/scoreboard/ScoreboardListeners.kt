package me.mrscopes.battleground.scoreboard

import fr.mrmicky.fastboard.adventure.FastBoard
import me.mrscopes.battleground.Battleground
import me.mrscopes.battleground.utilities.miniMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class ScoreboardListeners : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        val board = FastBoard(player)

        board.updateTitle("     <reset>[ <yellow>Battleground <reset>]     ".miniMessage())

        Battleground.scoreboard.boards[player.uniqueId] = board
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        Battleground.scoreboard.boards.remove(event.player.uniqueId)?.delete()
    }
}
