package me.mrscopes.battleground.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.FindOneAndReplaceOptions
import me.mrscopes.battleground.Battleground
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class Mongo(url: String) {
    val mongoPlayers: ConcurrentHashMap<UUID, MongoPlayer> = ConcurrentHashMap()
    val client: MongoClient
    val playerCollection: MongoCollection<MongoPlayer>
    private val plugin = Battleground.instance
    private val database: MongoDatabase

    init {
        val clientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(url))
            .codecRegistry(
                fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build())
                )
            )
            .build()

        client = MongoClients.create(clientSettings)
        database = client.getDatabase("kits")
        playerCollection = database.getCollection("players", MongoPlayer::class.java)

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, Runnable {
            plugin.logger.info("Backing up all players...")
            mongoPlayers.values.forEach { putPlayerInDatabase(it) }
            plugin.logger.info("Backed up all players.")
        }, 6000, 6000)
    }

    fun playerFromDatabase(uuid: UUID): MongoPlayer {
        return playerCollection.find(eq("uuid", uuid.toString())).firstOrNull()
            ?: MongoPlayer(uuid = uuid.toString())
    }

    fun putPlayerInDatabase(mongoPlayer: MongoPlayer) {
        playerCollection.findOneAndReplace(
            eq("uuid", mongoPlayer.uuid),
            mongoPlayer,
            FindOneAndReplaceOptions().upsert(true)
        )
    }
}

data class MongoPlayer(
    var uuid: String = "",
    var lastName: String = "",
    var kills: Int = 0,
    var killStreak: Int = 0,
    var deaths: Int = 0,
    var rerolls: Int = 3,
    var maxRerolls: Int = 3,
    var nameColor: String? = null,
    var xp: Double = 0.0,
)