package me.mrscopes.battleground.leaderboards

import com.mongodb.client.model.Sorts
import me.mrscopes.battleground.Battleground
import me.mrscopes.battleground.utilities.Utilities
import me.mrscopes.battleground.utilities.miniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Display
import org.bukkit.entity.TextDisplay

class Leaderboards {
    var kills: TextDisplay
    val collection = Battleground.mongo.playerCollection

    init {
        val world = Bukkit.getWorlds()[0]

        kills = world.spawn(Location(world, 0.5, 113.0, 9.5), TextDisplay::class.java) {
            it.billboard = Display.Billboard.CENTER
            it.isShadowed = true
        }

        updateKills()

        Utilities.whileLoop({ true }, { updateKills() }, 1200)
    }

    fun updateKills() {
        Utilities.async {
            val sorted = collection.find()
                .sort(Sorts.descending("kills"))
                .limit(10)
                .toList()

            val component =
                "<br> <blue><bold>Kills Leaderboard<reset><br><br> <leaderboard> <gray>Updates every minute <br>"
            var leaderboard = ""

            sorted.forEachIndexed { index, player ->
                leaderboard += "<dark_gray>(#<blue>${index + 1}<dark_gray>) <white>${player.lastName.ifEmpty { "?" }} <blue>${player.kills}<br>"
            }

            kills.text(component.miniMessage(Placeholder.component("leaderboard", leaderboard.miniMessage())))
        }
    }
}