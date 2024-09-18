package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.model.ItemId

open class AdatClassListFrontend<A : AdatClass>(
    override val backend: SetBackend<A>
) : CollectionFrontendBase() {

    var classFrontends = mutableMapOf<ItemId, AdatClassFrontend<A>>()

    var values: List<A> = emptyList()
        private set

    // TODO optimize AutoClassListFrontend.commit  (or maybe SetBackend)
    override fun commit() {
        val active = (backend.additions subtract backend.removals)
        values = active.sorted().map { getItemFrontend(it).value !! }
        backend.context.onListCommit(values)
    }

    override fun commit(itemId: ItemId) {
        @Suppress("UNCHECKED_CAST")
        val index = values.indexOfFirst { (it.adatContext as AdatContext<ItemId>).id == itemId }
        if (index == - 1) return
        val instance = getItemFrontend(itemId).value !!
        values = values.subList(0, index) + instance + values.subList(index + 1, values.size)
        backend.context.onItemCommit(instance)
    }

    operator fun get(index: Int) = values[index]

    operator fun plusAssign(item: A) {
        add(item)
    }

    fun add(item: A) {
        backend.add(item, null, true)
    }

    operator fun minusAssign(item: A) {
        remove(item.adatContext !!.id as ItemId)
    }

    fun remove(itemId: ItemId) {
        backend.remove(itemId, true)
    }

    fun modify(itemId: ItemId, propertyName: String, propertyValue: Any?) {
        getItemFrontend(itemId).modify(propertyName, propertyValue)
    }

    fun itemId(instance: AdatClass) =
        instance.adatContext !!.id as ItemId

    override fun update(instance: AdatClass, path: Array<String>, value: Any?) {
        // FIXME only single properties are handled b y AdatClassListFrontend
        check(path.size == 1) { "multi-level paths are not implemented yet" }
        modify(itemId(instance), path[0], value)
    }

    // TODO optimize AdatClassListFrontend.getFrontend - I think `newInstance` is unnecessary here
    open fun getItemFrontend(itemId: ItemId) =
        classFrontends.getOrPut(itemId) {

            val propertyBackend = checkNotNull(backend.items[itemId])
            val wireFormat = propertyBackend.wireFormat

            @Suppress("UNCHECKED_CAST")
            AdatClassFrontend(
                propertyBackend,
                wireFormat as AdatClassWireFormat<A>,
                wireFormat.newInstance(propertyBackend.values),
                itemId,
                this
            )
                .also { propertyBackend.frontend = it }
        }

}