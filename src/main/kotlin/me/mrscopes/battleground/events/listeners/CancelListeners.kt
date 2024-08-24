package me.mrscopes.battleground.events.listeners

import me.mrscopes.battleground.utilities.isInSpawn
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerItemDamageEvent

class CancelListeners : Listener {
    @EventHandler
    fun onItemConsume(event: PlayerItemConsumeEvent) {
        event.replacement = event.item
    }

    @EventHandler
    fun onItemDamage(event: PlayerItemDamageEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onItemDrop(event: PlayerDropItemEvent) {
        if (event.player.gameMode == GameMode.CREATIVE) return
        event.isCancelled = true
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.action != Action.LEFT_CLICK_BLOCK || event.action != Action.RIGHT_CLICK_BLOCK) return
        if (event.player.gameMode == GameMode.CREATIVE) return
        event.isCancelled = true
    }

    @EventHandler
    fun onInvClick(event: InventoryClickEvent) {
        if (event.whoClicked.gameMode == GameMode.CREATIVE) return
        if (event.cursor.type == Material.ELYTRA) event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onDamage(event: EntityDamageEvent) {
        if (event.entity.location.isInSpawn()) {
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onBreak(event: BlockBreakEvent) {
        if (event.player.gameMode == GameMode.CREATIVE) return
        event.player.gameMode = GameMode.ADVENTURE
        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlace(event: BlockPlaceEvent) {
        if (event.player.gameMode == GameMode.CREATIVE) return
        event.player.gameMode = GameMode.ADVENTURE
        event.isCancelled = true
    }
}