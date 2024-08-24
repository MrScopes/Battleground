package me.mrscopes.battleground.utilities

import me.mrscopes.battleground.Battleground
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.luckperms.api.node.NodeType
import net.luckperms.api.node.types.SuffixNode
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

fun String.miniMessage() = MiniMessage.miniMessage().deserialize(this)
fun String.miniMessage(tagResolver: TagResolver) = MiniMessage.miniMessage().deserialize(this, tagResolver)
fun Component.plainText() = PlainTextComponentSerializer.plainText().serialize(this)
fun Audience.miniMessage(string: String) = this.sendMessage(string.miniMessage())

fun Player.mongoPlayer() = Battleground.mongo.mongoPlayers[this.uniqueId] ?: Battleground.mongo.playerFromDatabase(this.uniqueId)
fun OfflinePlayer.mongoPlayer() = Battleground.mongo.mongoPlayers[this.uniqueId] ?: Battleground.mongo.playerFromDatabase(this.uniqueId)

fun Player.prefix() = LuckPerms.getUser(this).cachedData.metaData.prefix ?: ""

fun Player.suffix() = LuckPerms.getUser(this).cachedData.metaData.suffix ?: ""
fun Player.suffix(suffix: String) = run {
    Utilities.async {
        val user = LuckPerms.getUser(this)

        user.nodes
            .filter { it.type == NodeType.SUFFIX }
            .forEach {
                user.data().remove(it)
            }

        val suffixNode = SuffixNode.builder(suffix, 1).build()
        user.data().add(suffixNode)
        LuckPerms.luckPerms.userManager.saveUser(user)
    }
}

fun Player.display(): Component {
    val prefix = this.prefix().let { Utilities.translateChatColors(it) }
    val name = this.mongoPlayer().nameColor?.let { it + this.name } ?: this.name
    val suffix = this.suffix().let { Utilities.translateChatColors(it) }

    val format = buildString {
        append(prefix)
        append(name)
        append(suffix)
    }

    return format.miniMessage()
}

fun Location.isInSpawn(): Boolean {
    return Utilities.isInSpawn(this)
}