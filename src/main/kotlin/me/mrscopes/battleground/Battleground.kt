package me.mrscopes.battleground

import me.mrscopes.battleground.combatlog.CombatLog
import me.mrscopes.battleground.commands.Commands
import me.mrscopes.battleground.events.Events
import me.mrscopes.battleground.healthbars.HealthBars
import me.mrscopes.battleground.leaderboards.Leaderboards
import me.mrscopes.battleground.mongo.Mongo
import me.mrscopes.battleground.scoreboard.Scoreboard
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Battleground : JavaPlugin() {
    override fun onEnable() {
        instance = this
        saveDefaultConfig()

        mongo = Mongo(config.getString("mongo url")!!)
        healthBars = HealthBars()
        combatLog = CombatLog()
        scoreboard = Scoreboard()
        leaderboards = Leaderboards()

        Commands.register()
        Events.register()

        // cleanup extra entities
        Bukkit.getWorld("world")?.entities?.forEach {
            if (it.scoreboardTags.contains("remove")) {
                it.remove()
            }
        }
    }

    override fun onDisable() {
        healthBars.bars.forEach { healthBars.remove(it.key) }
        leaderboards.kills.remove()
        mongo.client.close()
    }

    companion object {
        lateinit var instance: Battleground
            private set

        lateinit var mongo: Mongo
            private set

        lateinit var healthBars: HealthBars
            private set

        lateinit var combatLog: CombatLog
            private set

        lateinit var scoreboard: Scoreboard
            private set

        lateinit var leaderboards: Leaderboards
            private set
    }
}