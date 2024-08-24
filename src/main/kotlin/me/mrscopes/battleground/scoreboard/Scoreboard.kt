package me.mrscopes.battleground.scoreboard

import fr.mrmicky.fastboard.adventure.FastBoard
import me.mrscopes.battleground.utilities.Utilities
import me.mrscopes.battleground.utilities.miniMessage
import me.mrscopes.battleground.utilities.mongoPlayer
import org.bukkit.Bukkit
import java.util.*

class Scoreboard {
    val boards = HashMap<UUID, FastBoard>()

    init {
        Utilities.whileLoop({ true }, {
            boards.forEach { updateBoard(it.value) }
        }, 20)
    }

    private fun updateBoard(board: FastBoard) {
        val mongoPlayer = board.player.mongoPlayer()

        board.updateLines(
            listOf(
                "",
                " <gold>Players: <white>${Bukkit.getServer().onlinePlayers.size}/100",
                "",
                " <gold>Kills: <white>${mongoPlayer.kills}${if (mongoPlayer.killStreak > 0) " ${mongoPlayer.killStreak} streak" else ""}",
                " <gold>Deaths: <white>${mongoPlayer.deaths}",
                "",
                " <gold>Rerolls: <white>(${mongoPlayer.rerolls}/${mongoPlayer.maxRerolls})",
                "",
                "<gray><italic>battleground.minehut.gg"
            ).map { it.miniMessage() }
        )
    }

}