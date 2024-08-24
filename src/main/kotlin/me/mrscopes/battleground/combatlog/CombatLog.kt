package me.mrscopes.battleground.combatlog

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import me.mrscopes.battleground.utilities.Utilities
import me.mrscopes.battleground.utilities.miniMessage
import org.bukkit.Bukkit
import java.util.*
import java.util.concurrent.TimeUnit

class CombatLog {
    val combatTagCache: LoadingCache<UUID, CombatTagData> = CacheBuilder.newBuilder()
        .expireAfterWrite(20, TimeUnit.SECONDS)
        .build(object : CacheLoader<UUID, CombatTagData>() {
            override fun load(key: UUID): CombatTagData {
                return CombatTagData(null, System.currentTimeMillis() + 20000)
            }
        })

    init {
        Utilities.whileLoop({ true }, {
            for (player in Bukkit.getOnlinePlayers()) {
                val combatData = combatTagCache.getIfPresent(player.uniqueId)
                if (combatData != null) {
                    val remainingTime = (combatData.expiration - System.currentTimeMillis()) / 1000
                    if (remainingTime > 0) {
                        player.sendActionBar("<red>You're combat tagged for $remainingTime seconds.".miniMessage())
                    }
                }
            }
        }, 20)
    }
}

data class CombatTagData(val attacker: UUID?, val expiration: Long)