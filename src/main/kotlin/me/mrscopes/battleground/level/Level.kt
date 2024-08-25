package me.mrscopes.battleground.level

import me.mrscopes.battleground.utilities.miniMessage
import me.mrscopes.battleground.utilities.mongoPlayer
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import kotlin.math.floor

object Level {
    fun level(xp: Double): Int {
        val level = floor(1 + xp / 100.0).toInt()

        return level.coerceAtMost(100)
    }

    fun levelDisplay(level: Int): String {
        val color: String = if (level < 10) "<gray>"
        else if (level < 20) "<light_blue>"
        else if (level < 30) "<yellow>"
        else if (level < 40) "<light_green>"
        else if (level < 50) "<light_purple>"
        else if (level < 60) "<green>"
        else if (level < 70) "<blue>"
        else if (level < 80) "<gold>"
        else if (level < 90) "<red>"
        else if (level < 100) "<dark_red>"
        else "<dark_red><bold>"

        return "<dark_gray>[$color$level<reset><dark_gray>]"
    }

    fun levelDisplay(player: Player): String {
        return levelDisplay(level(player.mongoPlayer().xp))
    }
}