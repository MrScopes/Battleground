package me.mrscopes.battleground.events.listeners

import me.mrscopes.battleground.utilities.display
import me.mrscopes.battleground.utilities.miniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class QuitListener : Listener {
    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        event.quitMessage(
            "<dark_gray>[<red>-<dark_gray>] <gray><player>".miniMessage(
                Placeholder.component(
                    "player",
                    player.display()
                )
            )
        )
    }
}