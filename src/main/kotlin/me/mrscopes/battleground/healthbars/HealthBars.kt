package me.mrscopes.battleground.healthbars

import me.mrscopes.battleground.Battleground
import me.mrscopes.battleground.utilities.Utilities
import me.mrscopes.battleground.utilities.display
import me.mrscopes.battleground.utilities.miniMessage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Display
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay
import org.bukkit.util.Transformation
import org.joml.Vector3f
import java.util.*

class HealthBars {
    val bars = HashMap<UUID, MutableList<TextDisplay>>()

    init {
        Utilities.whileLoop({ true }, {
            Bukkit.getOnlinePlayers().forEach {
                update(it)
            }
        }, 20)
    }

    fun add(player: Player) {
        if (bars.containsKey(player.uniqueId)) {
            remove(player.uniqueId)
        }

        val world = player.world

        val displayBar = world.spawn(player.location, TextDisplay::class.java) {
            it.billboard = Display.Billboard.CENTER
            it.displayWidth /= 3
            it.isShadowed = true
            it.backgroundColor = Color.fromARGB(0, 0, 0, 0)
            it.transformation = Transformation(
                Vector3f(0.0f, 0.225f, 0.0f),
                it.transformation.leftRotation,
                Vector3f(0.7f, 0.7f, 0.7f),
                it.transformation.rightRotation
            )
            it.scoreboardTags.add("remove")
        }

        val healthBar = world.spawn(player.location, TextDisplay::class.java) {
            it.billboard = Display.Billboard.CENTER
            it.displayWidth /= 3
            it.isShadowed = true
            it.backgroundColor = Color.fromARGB(0, 0, 0, 0)
            it.transformation = Transformation(
                Vector3f(0.0f, 0.1125f, 0.0f),
                it.transformation.leftRotation,
                Vector3f(0.2f, 1f, 0.2f),
                it.transformation.rightRotation
            )
            it.scoreboardTags.add("remove")
        }

        player.addPassenger(displayBar)
        player.addPassenger(healthBar)

        bars[player.uniqueId] = mutableListOf(displayBar, healthBar)

        update(player)
    }

    fun remove(uuid: UUID) {
        val displayBar = Bukkit.getEntity(Battleground.healthBars.bars[uuid]!![0].uniqueId)
        displayBar?.remove()

        val healthBar = Bukkit.getEntity(Battleground.healthBars.bars[uuid]!![1].uniqueId)
        healthBar?.remove()

        Battleground.healthBars.bars.remove(uuid)
    }

    fun update(player: Player) {
        val text = display(player)
        bars[player.uniqueId]?.get(0)?.text(text[0])
        bars[player.uniqueId]?.get(1)?.text(text[1])
    }

    fun display(player: Player): List<Component> {
        val health = player.health + player.absorptionAmount
        val maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
        val healthPercentage = (health / maxHealth * 100).toInt()

        val totalBarLength = 20

        val filledLength = (totalBarLength * (healthPercentage.coerceAtMost(100) / 100.0)).toInt()

        var color = "green"
        if (healthPercentage <= 75) color = "yellow"
        if (healthPercentage <= 25) color = "red"

        val filledPart = "<$color>" + "\u0020 ".repeat(filledLength)
        val unfilledPart = "<gray>" + "\u0020 ".repeat(totalBarLength - filledLength)

        return listOf(
            "$healthPercentage<br><br><name>".miniMessage(Placeholder.component("name", player.display())),
            "<strikethrough>$filledPart$unfilledPart</strikethrough><br>".miniMessage()
        )
    }
}