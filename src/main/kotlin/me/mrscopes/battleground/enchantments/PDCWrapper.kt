package me.mrscopes.battleground.enchantments

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class PDCWrapper(val item: ItemStack, val namespace: String) {
    val meta = item.itemMeta
    val pdc = meta.persistentDataContainer

    fun set(key: String, value: String) {
        pdc.set(NamespacedKey(namespace, key), PersistentDataType.STRING, value)
        update()
    }

    fun set(key: String, value: Int) {
        pdc[NamespacedKey(namespace, key), PersistentDataType.INTEGER] = value
        update()
    }

    fun set(key: String, value: Long) {
        pdc[NamespacedKey(namespace, key), PersistentDataType.LONG] = value
        update()
    }

    fun set(parent: String, key: String, value: String) {
        set(parent, key, value, PersistentDataType.STRING)
    }

    fun set(parent: String, key: String, value: Int) {
        set(parent, key, value, PersistentDataType.INTEGER)
    }

    fun set(parent: String, key: String, value: Long) {
        set(parent, key, value, PersistentDataType.LONG)
    }

    fun <T : Any> set(parent: String, key: String, value: T, type: PersistentDataType<T, T>) {
        val container = pdc.get(NamespacedKey(namespace, parent), PersistentDataType.TAG_CONTAINER)
        if (container == null) {
            pdc.set(
                NamespacedKey(namespace, parent),
                PersistentDataType.TAG_CONTAINER,
                pdc.adapterContext.newPersistentDataContainer()
            )
            update()
            set(parent, key, value, type)
            return
        }

        container.set(NamespacedKey(namespace, key), type, value)

        pdc.set(NamespacedKey(namespace, parent), PersistentDataType.TAG_CONTAINER, container)

        update()
    }

    fun <T> get(key: String, type: PersistentDataType<T, T>): T? {
        return pdc[NamespacedKey(namespace, key), type]
    }

    fun update() {
        item.itemMeta = meta
    }
}

fun ItemStack.data(namespace: String) = PDCWrapper(this, namespace)

fun ItemStack.data() = PDCWrapper(this, "battleground")
