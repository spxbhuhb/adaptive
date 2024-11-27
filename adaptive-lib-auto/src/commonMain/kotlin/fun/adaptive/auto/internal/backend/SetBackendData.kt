package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use

class SetBackendData<IT : AdatClass>(
    initialValue: MutableMap<ItemId, PropertyBackend<IT>>?,
) {

    private val structuralLock = getLock()

    private val additions = mutableSetOf<ItemId>()
    private val removals = mutableSetOf<ItemId>()
    private val active = mutableSetOf<ItemId>()

    private val items = initialValue ?: mutableMapOf()

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is SetBackendData<*>) return false

        val thisActive = structuralLock.use { active }
        val otherActive = other.structuralLock.use { active }

        return thisActive == otherActive
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    fun removals(): Set<ItemId> =
        structuralLock.use {
            removals.toSet()
        }

    fun active() =
        structuralLock.use {
            active.toSet()
        }

    fun add(itemId: ItemId, backend: PropertyBackend<IT>): Boolean {
        structuralLock.use {
            if (itemId in removals) return false

            additions += itemId
            active += itemId
            items[itemId] = backend

            return true
        }
    }

    fun addAll(itemIds: Set<ItemId>) {
        structuralLock.use {
            additions.addAll(itemIds)
            active.addAll(itemIds)
        }
    }

    fun remove(itemId: ItemId, fromPeer: Boolean) : IT? =
        structuralLock.use {
            removals += itemId
            active -= itemId
            val removed = items.remove(itemId)
            removed?.removed(fromPeer)
            removed?.getItem()
        }

    operator fun get(itemId: ItemId) =
        structuralLock.use {
            items[itemId]
        }

    operator fun set(itemId: ItemId, value: PropertyBackend<IT>) {
        structuralLock.use {
            items[itemId] = value
        }
    }

    fun remove(itemIds: Set<ItemId>, fromPeer: Boolean) {
        structuralLock.use {
            removals.addAll(itemIds)
            active.removeAll(itemIds)
            for (itemId in itemIds) {
                items.remove(itemId)?.removed(fromPeer)
            }
        }
    }

    operator fun contains(itemId: ItemId) =
        structuralLock.use {
            itemId in active
        }

    fun isEmpty(): Boolean =
        structuralLock.use {
            active.isEmpty()
        }

    fun items() =
        structuralLock.use {
            items.values.sortedBy { it.itemId }
        }

}