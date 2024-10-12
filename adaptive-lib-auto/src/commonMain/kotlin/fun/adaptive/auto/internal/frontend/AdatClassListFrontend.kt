package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.model.ItemId

open class AdatClassListFrontend<A : AdatClass>(
    override val backend: SetBackend<A>
) : CollectionFrontendBase<A>() {

    var classFrontends = mutableMapOf<ItemId, AdatClassFrontend<A>>()

    override var values: List<A> = emptyList()

    // TODO optimize AutoClassListFrontend.commit  (or maybe SetBackend)
    override fun commit(initial : Boolean) {
        val active = (backend.additions subtract backend.removals)
        values = active.sorted().map { getItemFrontend(it).value !! }
        if (initial) {
            backend.context.onInit(values)
        } else {
            backend.context.onChange(values)
        }
    }

    override fun commit(itemId: ItemId, newValue : A, oldValue: A?, initial : Boolean) {
        // TODO check AdatClassListFrontend.commit, should we throw an exception when there is no item?
        @Suppress("UNCHECKED_CAST")
        val index = values.indexOfFirst { (it.adatContext as AdatContext<ItemId>).id == itemId }
        if (index == - 1) return

        values = values.subList(0, index) + newValue + values.subList(index + 1, values.size)

        backend.context.onChange(newValue, oldValue)
    }

    operator fun plusAssign(item: A) {
        add(item)
    }

    override fun add(item: A) {
        backend.add(item, null, true)
    }

    operator fun minusAssign(item: A) {
        remove(item.adatContext !!.id as ItemId)
    }

    override fun remove(itemId: ItemId) {
        backend.remove(itemId, true)
    }

    override fun remove(selector: (A) -> Boolean) {
        for (item in values.filter(selector)) {
            backend.remove(itemId(item), true)
        }
    }

    fun modify(itemId: ItemId, propertyName: String, propertyValue: Any?) {
        getItemFrontend(itemId).modify(propertyName, propertyValue)
    }

    override fun update(original: AdatClass, new: AdatClass) {
        getItemFrontend(itemId(original)).update(new)
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