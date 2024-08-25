package me.mrscopes.battleground.events

import me.mrscopes.battleground.Battleground
import me.mrscopes.battleground.abilities.listeners.AbilityRightClickListener
import me.mrscopes.battleground.combatlog.CombatLogListeners
import me.mrscopes.battleground.enchantments.listeners.EnchPlayerDamageListener
import me.mrscopes.battleground.events.listeners.*
import me.mrscopes.battleground.healthbars.HealthBarListeners
import me.mrscopes.battleground.level.LevelListeners
import me.mrscopes.battleground.mongo.MongoListeners
import me.mrscopes.battleground.scoreboard.ScoreboardListeners
import org.bukkit.Bukkit
import org.bukkit.event.Listener

object Events {
    fun register() {
        registerListeners(
            listOf(
                ChatListener(),
                JoinListener(),
                QuitListener(),
                RespawnListener(),
                CancelListeners(),
                LaunchListener(),
                EnchPlayerDamageListener(),
                DeathListener(),
                MongoListeners(),
                HealthBarListeners(),
                CombatLogListeners(),
                ScoreboardListeners(),
                AbilityRightClickListener(),
                LevelListeners()
            )
        )
    }

    fun registerListener(listener: Listener) {
        Bukkit.getPluginManager().registerEvents(listener, Battleground.instance)
    }

    fun registerListeners(listeners: List<Listener>) {
        listeners.forEach {
            Bukkit.getPluginManager().registerEvents(it, Battleground.instance)
        }
    }
}
