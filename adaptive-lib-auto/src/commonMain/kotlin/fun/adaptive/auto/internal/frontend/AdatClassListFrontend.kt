package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.AutoCollectionBackend
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.ItemId

open class AdatClassListFrontend<IT : AdatClass>(
    instance: AutoInstance<AutoCollectionBackend<IT>, AutoCollectionFrontend<IT>, Collection<IT>, IT>,
    val collectionBackend: SetBackend<IT>,
) : AutoCollectionFrontend<IT>(instance) {

    override val persistent: Boolean
        get() = false

    var classFrontends = mutableMapOf<ItemId, AdatClassFrontend<IT>>()

    override val values: Collection<IT>
        get() = checkNotNull(values)

    override var valueOrNull: Collection<IT>? = null
        protected set

    override fun load(): Pair<AutoConnectionInfo<Collection<IT>>?, Collection<IT>?> {
        throw UnsupportedOperationException("AdatClassListFrontend does not persist values, so it cannot load them")
    }

    // TODO optimize AutoClassListFrontend.commit  (or maybe SetBackend)
    override fun commit(initial: Boolean, fromBackend: Boolean) {
        valueOrNull = collectionBackend.data.active().sorted().map { getItemFrontend(it).value }

        if (initial) {
            instance.onInit(values, fromBackend)
        } else {
            instance.onChange(values, fromBackend)
        }
    }

    override fun commit(itemId: ItemId, newValue: IT, oldValue: IT?, initial: Boolean, fromBackend: Boolean) {
        // TODO check AdatClassListFrontend.commit, should we throw an exception when there is no item?
        @Suppress("UNCHECKED_CAST")
        val index = values.indexOfFirst { (it.adatContext as AdatContext<ItemId>).id == itemId }
        if (index == - 1) return

        valueOrNull = values.subList(0, index) + newValue + values.subList(index + 1, values.size)

        instance.onChange(newValue, oldValue, fromBackend)
    }

    operator fun plusAssign(item: IT) {
        add(item)
    }

    override fun add(item: IT) {
        collectionBackend.add(item, null, true)
    }

    operator fun minusAssign(item: IT) {
        remove(item.adatContext !!.id as ItemId)
    }

    override fun remove(itemId: ItemId) {
        collectionBackend.remove(itemId, true)
    }

    override fun remove(selector: (IT) -> Boolean) {
        for (item in values.filter(selector)) {
            collectionBackend.remove(itemId(item), true)
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

            val propertyBackend = checkNotNull(collectionBackend.data[itemId])
            val wireFormat = propertyBackend.wireFormat

            @Suppress("UNCHECKED_CAST")
            AdatClassFrontend<IT>(
                propertyBackend,
                wireFormat as AdatClassWireFormat<IT>,
                wireFormat.newInstance(propertyBackend.values),
                itemId,
                this
            )
                .also { propertyBackend.frontend = it }
        }

}