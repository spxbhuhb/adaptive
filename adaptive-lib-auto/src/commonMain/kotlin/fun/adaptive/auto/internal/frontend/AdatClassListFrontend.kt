package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.store.AdatStore
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.model.ItemId

class AdatClassListFrontend<A : AdatClass<A>>(
    override val backend: SetBackend,
    val companion: AdatCompanion<A>,
    val onListCommit: ((newValue: List<A>) -> Unit)?,
    val onItemCommit: ((item: A) -> Unit)?
) : CollectionFrontendBase() {

    var classFrontends = mutableMapOf<ItemId, AdatClassFrontend<A>>()

    var values: List<A> = emptyList()
        private set

    // TODO optimize AutoClassListFrontend.commit  (or maybe SetBackend)
    override fun commit() {
        val active = (backend.additions.map { it.first }.toSet() subtract backend.removals)
        values = active.sorted().map { getFrontend(it).value !! }
        onListCommit?.invoke(values)
    }

    override fun commit(itemId: ItemId) {
        @Suppress("UNCHECKED_CAST")
        val index = values.indexOfFirst { (it.adatContext as AdatContext<ItemId>).id == itemId.adatContext?.id }
        if (index == - 1) return
        val instance = getFrontend(itemId).value !!
        values = values.subList(0, index) + instance + values.subList(index + 1, values.size)
        onItemCommit?.invoke(instance)
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

    // TODO optimize AdatClassListFrontend.getFrontend - I think `newInstance` is unnecessary here
    fun getFrontend(itemId: ItemId) =
        classFrontends.getOrPut(itemId) {
            val propertyBackend = checkNotNull(backend.items[itemId])
            AdatClassFrontend(propertyBackend, companion, companion.newInstance(propertyBackend.values), itemId, this)
                .also { propertyBackend.frontEnd = it }
        }

}