package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.api.deepCopy
import `fun`.adaptive.adat.api.validateForContext
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.model.ItemId

open class AdatClassFrontend<A : AdatClass>(
    override val backend: PropertyBackend<A>,
    val wireFormat: AdatClassWireFormat<A>,
    initialValue: A?,
    val itemId: ItemId,
    val collectionFrontend: CollectionFrontendBase<A>?,
) : ItemFrontendBase<A>() {

    val adatContext = AdatContext<ItemId>(itemId, null, null, store = this, null)

    override val value: A
        get() = checkNotNull(valueOrNull) { "value not yet initialized" }

    var valueOrNull: A? = initialValue?.deepCopy()?.also {
        @Suppress("UNCHECKED_CAST")
        it.adatContext = adatContext as AdatContext<Any>
    }

    override fun removed(fromBackend: Boolean) {
        backend.context.onRemove(value, fromBackend)
    }

    /**
     * AdatClassListFrontend calls PropertyBackend on modify
     * PropertyBackend calls commit when finished
     */
    override fun commit(initial: Boolean, fromBackend: Boolean) {
        val oldValue = valueOrNull
        val newValue = wireFormat.newInstance(backend.values)

        @Suppress("UNCHECKED_CAST")
        newValue.adatContext = adatContext as AdatContext<Any>
        newValue.validateForContext()

        valueOrNull = newValue

        if (collectionFrontend != null) {
            collectionFrontend.commit(backend.itemId, newValue, oldValue, initial, fromBackend)
        } else {
            backend.context.onChange(newValue, oldValue, fromBackend)
        }
    }

    fun modify(propertyName: String, propertyValue: Any?) {
        backend.modify(backend.itemId, propertyName, propertyValue)
    }

    override fun update(instance: AdatClass, path: Array<String>, value: Any?) {
        check(path.size == 1) { "multi-level paths are not implemented yet" }
        modify(path[0], value)
    }

    override fun update(original: AdatClass, new: AdatClass) {
        backend.update(new.toArray())
    }

    override fun update(new: AdatClass) {
        backend.update(new.toArray())
    }


}