package me.mrscopes.battleground.utilities

import me.mrscopes.battleground.Battleground
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.security.SecureRandom

object Utilities {
    fun broadcastToStaff(message: String) {
        Bukkit.getConsoleSender().miniMessage(message)
        Bukkit.getOnlinePlayers().forEach {
            if (it.hasPermission("battleground.mod") || it.isOp) it.miniMessage(message)
        }
    }

    fun broadcastToAdmins(message: String) {
        Bukkit.getConsoleSender().miniMessage(message)
        Bukkit.getOnlinePlayers().forEach {
            if (it.hasPermission("battleground.admin") || it.isOp) it.miniMessage(message)
        }
    }

    fun moneyFormat(money: Int): String {
        return "$${String.format("%,d", money)}"
    }

    fun whileLoop(condition: () -> Boolean, fn: () -> Unit, interval: Long, delay: Long = 0): BukkitTask {
        val task = object : BukkitRunnable() {
            override fun run() {
                if (!condition()) {
                    cancel()
                    return
                }
                fn()
            }
        }.runTaskTimer(Battleground.instance, delay, interval)

        return task
    }

    fun after(fn: () -> Unit, delay: Long): BukkitTask {
        val task = object : BukkitRunnable() {
            override fun run() {
                fn()
            }
        }.runTaskLater(Battleground.instance, delay)

        return task
    }

    fun async(fn: () -> Unit): BukkitTask {
        val task = object : BukkitRunnable() {
            override fun run() {
                fn()
            }
        }.runTaskAsynchronously(Battleground.instance)

        return task
    }

    fun translateChatColors(input: String): String {
        val colorCodes = mapOf(
            '1' to "<dark_blue>",
            '2' to "<dark_green>",
            '3' to "<dark_aqua>",
            '4' to "<dark_red>",
            '5' to "<dark_purple>",
            '6' to "<gold>",
            '7' to "<gray>",
            '8' to "<dark_gray>",
            '9' to "<blue>",
            'a' to "<green>",
            'b' to "<aqua>",
            'c' to "<red>",
            'd' to "<light_purple>",
            'e' to "<yellow>",
            'f' to "<white>",
            'n' to "<u>",
            'm' to "<st>",
            'k' to "<obf>",
            'o' to "<i>",
            'l' to "<b>",
            'r' to "<r>"
        )

        var result = input
        colorCodes.forEach { (code, replacement) ->
            result = result.replace("&$code", replacement)
        }
        return result
    }

    fun levelDisplay(int: Int): String {
        return when (int) {
            1 -> "<yellow>⭐"
            2 -> "<yellow>⭐⭐"
            3 -> "<yellow>⭐⭐⭐"
            4 -> "<yellow>⭐⭐⭐⭐"
            5 -> "<rainbow>⭐⭐⭐⭐⭐"
            else -> int.toString()
        }
    }

    fun chance(chance: Double): Boolean {
        val secureRandom = SecureRandom()
        val randomValue = secureRandom.nextDouble() * 100.0
        return randomValue <= chance
    }

    fun isInSpawn(location: Location): Boolean {
        val corner1 = Location(Bukkit.getWorld("world"), 28.0, 105.0, -35.0)
        val corner2 = Location(Bukkit.getWorld("world"), -33.0, 133.0, 26.0)

        return isLocationWithinBounds(location, corner1, corner2)
    }

    fun isLocationWithinBounds(location: Location, corner1: Location, corner2: Location): Boolean {
        val minX = minOf(corner1.x, corner2.x)
        val maxX = maxOf(corner1.x, corner2.x)
        val minY = minOf(corner1.y, corner2.y)
        val maxY = maxOf(corner1.y, corner2.y)
        val minZ = minOf(corner1.z, corner2.z)
        val maxZ = maxOf(corner1.z, corner2.z)

        return (location.x in minX..maxX) &&
                (location.y in minY..maxY) &&
                (location.z in minZ..maxZ)
    }
}

