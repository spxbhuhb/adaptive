package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.model.ItemId

class SetBackendData<IT : AdatClass>(
    initialValue: MutableMap<ItemId, PropertyBackend<IT>>?,
) {

    private val additions = mutableSetOf<ItemId>()
    private val removals = mutableSetOf<ItemId>()
    private val active = mutableSetOf<ItemId>()

    private val items = initialValue ?: mutableMapOf()

    fun removals(): Set<ItemId> =
        removals.toSet()

    fun active() =
        active.toSet()

    fun add(itemId: ItemId, backend: PropertyBackend<IT>): Boolean {
        if (itemId in removals) return false

        additions += itemId
        active += itemId
        items[itemId] = backend

        return true
    }

    fun addAll(itemIds: Set<ItemId>) {
        additions.addAll(itemIds)
        active.addAll(itemIds)
    }

    fun remove(itemId: ItemId, fromPeer: Boolean): IT? {
        removals += itemId
        active -= itemId
        val removed = items.remove(itemId)
        removed?.removed(fromPeer)
        return removed?.getItem()
    }

    operator fun get(itemId: ItemId) =
        items[itemId]


    operator fun set(itemId: ItemId, value: PropertyBackend<IT>) {
        items[itemId] = value

    }

    fun remove(itemIds: Set<ItemId>, fromPeer: Boolean): Set<Pair<ItemId, IT>> {

        removals.addAll(itemIds)
        active.removeAll(itemIds)

        val removed = mutableSetOf<Pair<ItemId, IT>>()

        for (itemId in itemIds) {
            items.remove(itemId)?.let {
                removed += itemId to it.getItem()
                it.removed(fromPeer)
            }
        }

        return removed
    }

    operator fun contains(itemId: ItemId) =
        itemId in active


    fun isEmpty(): Boolean =
        active.isEmpty()


    fun items() =
        items.values.sortedBy { it.itemId }

}