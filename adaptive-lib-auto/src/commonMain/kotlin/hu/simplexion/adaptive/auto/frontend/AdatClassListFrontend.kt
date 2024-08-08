package hu.simplexion.adaptive.auto.frontend

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.AdatCompanion
import hu.simplexion.adaptive.adat.store.AdatStore
import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.backend.ListBackend

class AdatClassListFrontend<A : AdatClass<A>>(
    val backend: ListBackend,
    val companion: AdatCompanion<A>
) : AbstractFrontend(), AdatStore {

    var classFrontends = mutableMapOf<ItemId, AdatClassFrontend<A>>()

    var values: List<Pair<ItemId, A>> = emptyList()
        private set

    override fun commit() {
        // TODO optimize AutoClassListFrontend.commit (or maybe ListBackend)
        values =
            (backend.additions.map { it.first }.toSet() subtract backend.removals)
                .sorted()
                .map { it to getFrontend(it).value !! }
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
            AdatClassFrontend(propertyBackend, companion, companion.newInstance(propertyBackend.values))
        }

}