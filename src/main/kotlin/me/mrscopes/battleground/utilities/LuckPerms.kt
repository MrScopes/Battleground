package me.mrscopes.battleground.utilities

import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.model.user.User
import org.bukkit.entity.Player

object LuckPerms {
    val luckPerms = LuckPermsProvider.get()
    val playerAdapter = luckPerms.getPlayerAdapter(Player::class.java)

    fun getUser(player: Player): User {
        return playerAdapter.getUser(player)
    }
}
