package `fun`.adaptive.auto.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.store.AdatStore
import `fun`.adaptive.auto.ItemId
import `fun`.adaptive.auto.backend.ListBackend
import `fun`.adaptive.auto.model.AutoItemInstance

// TODO optimize AutoClassListFrontend.commit (or maybe ListBackend)
class AdatClassListFrontend<A : AdatClass<A>>(
    val backend: ListBackend,
    val companion: AdatCompanion<A>
) : CollectionFrontendBase(), AdatStore {

    var classFrontends = mutableMapOf<ItemId, AdatClassFrontend<A>>()

    var values: List<AutoItemInstance<A>> = emptyList()
        private set

    override fun commit() {
        values =
            (backend.additions.map { it.first }.toSet() subtract backend.removals)
                .sorted()
                .map { AutoItemInstance(it, getFrontend(it).value !!) }
    }

    override fun commit(itemId: ItemId) {
        val index = values.indexOfFirst { it.itemId == itemId }
        if (index == - 1) return
        values = values.subList(0, index) + AutoItemInstance(itemId, getFrontend(itemId).value !!) + values.subList(index + 1, values.size)
    }

    fun add(item: A) {
        backend.add(item, null, null, true, true)
    }

    fun remove(itemId: ItemId) {
        backend.remove(itemId, true, true)
    }

    fun removeAll(itemIds: Set<ItemId>) {
        backend.removeAll(itemIds, true, true)
    }

    fun modify(itemId: ItemId, propertyName: String, propertyValue: Any?) {
        getFrontend(itemId).modify(propertyName, propertyValue)
    }

    fun getFrontend(itemId: ItemId) =
        classFrontends.getOrPut(itemId) {
            val propertyBackend = checkNotNull(backend.items[itemId])
            AdatClassFrontend(propertyBackend, companion, companion.newInstance(propertyBackend.values), this)
                .also { propertyBackend.frontEnd = it }
        }

}