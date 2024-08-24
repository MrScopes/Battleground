package me.mrscopes.battleground.events.listeners

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import io.papermc.paper.event.player.AsyncChatEvent
import me.mrscopes.battleground.utilities.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.concurrent.TimeUnit

class ChatListener : Listener {

    private val antispam: LoadingCache<UUID, Long> = CacheBuilder.newBuilder()
        .expireAfterWrite(2, TimeUnit.SECONDS)
        .build(object : CacheLoader<UUID, Long>() {
            override fun load(key: UUID): Long = System.currentTimeMillis()
        })

    private val lastSentence: LoadingCache<UUID, Component> = CacheBuilder.newBuilder()
        .expireAfterWrite(1, TimeUnit.MINUTES)
        .build(object : CacheLoader<UUID, Component>() {
            override fun load(key: UUID): Component = Component.text("")
        })

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onChat(event: AsyncChatEvent) {
        val player = event.player
        val message = event.message()

        if (!player.hasPermission("battleground.spam")) {
            val uuid = player.uniqueId

            val lastMessage = lastSentence.getIfPresent(uuid)
            if (lastMessage == message) {
                player.miniMessage("<red>Don't repeat the same message.")
                event.isCancelled = true
                return
            }

            val now = System.currentTimeMillis()
            val lastMessageTime = antispam.getIfPresent(uuid)?.let { now - it }
            if (lastMessageTime != null && lastMessageTime < 2000) {
                val remaining = 2000 - lastMessageTime
                val seconds = remaining / 1000.0
                player.miniMessage("<red>Wait ${"%.2f".format(seconds)} seconds to chat again.")
                event.isCancelled = true
                return
            }

            antispam.put(uuid, now)
            lastSentence.put(uuid, message)

        }

        event.renderer { source, displayName, msg, audience ->
            var finalMessage = msg

            if (finalMessage.toString().contains("[item]")) {
                finalMessage = replaceItemPlaceholder(finalMessage, player.inventory.itemInMainHand)
            }

            val prefix = Utilities.translateChatColors(source.prefix())
            val nameColor = source.mongoPlayer().nameColor ?: ""
            val suffix = Utilities.translateChatColors(source.suffix())
            val chatColor = if (!source.hasPermission("battleground.spam")) "<gray>" else "<white>"

            return@renderer "$prefix$nameColor${source.name}$suffix<reset><dark_gray>: <reset>${chatColor}<message>".miniMessage(
                Placeholder.component("message", finalMessage)
            )
        }
    }

    private fun replaceItemPlaceholder(message: Component, item: ItemStack): Component {
        return if (item.type != Material.AIR) {
            val itemName = item.displayName().hoverEvent(item.asHoverEvent())
            val itemComponent = Component.text()
                .append(Component.text(if (item.amount > 1) "${item.amount}x " else "", item.displayName().color()))
                .append(itemName)
                .build()

            message.replaceText { builder ->
                builder.once().match("\\[item]")
                    .replacement(itemComponent)
                    .build()
            }
        } else {
            message
        }
    }
}
