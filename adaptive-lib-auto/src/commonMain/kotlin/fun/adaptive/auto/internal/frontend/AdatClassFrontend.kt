package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.api.deepCopy
import `fun`.adaptive.adat.api.validateForContext
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.model.ItemId

open class AdatClassFrontend<A : AdatClass>(
    override val backend: PropertyBackend<A>,
    val wireFormat: AdatClassWireFormat<A>,
    initialValue: A?,
    val itemId : ItemId,
    val collectionFrontend: CollectionFrontendBase?
) : FrontendBase() {

    val adatContext = AdatContext<ItemId>(itemId, null, null, store = this, null)

    var value: A? = initialValue?.deepCopy()?.also {
        @Suppress("UNCHECKED_CAST")
        it.adatContext = adatContext as AdatContext<Any>
    }

    override fun removed() {
        backend.context.onRemove(value !!)
    }

    /**
     * AdatClassListFrontend calls PropertyBackend on modify
     * PropertyBackend calls commit when finished
     */
    override fun commit() {
        val newValue = wireFormat.newInstance(backend.values)

        @Suppress("UNCHECKED_CAST")
        newValue.adatContext = adatContext as AdatContext<Any>
        newValue.validateForContext()

        value = newValue
        if (collectionFrontend != null) {
            collectionFrontend.commit(backend.itemId)
        } else {
            backend.context.onItemCommit(newValue)
        }
    }

    fun modify(propertyName: String, propertyValue: Any?) {
        backend.modify(backend.itemId, propertyName, propertyValue)
    }

    override fun update(instance: AdatClass, path: Array<String>, value: Any?) {
        // FIXME only single properties are handled b y AdatClassListFrontend
        check(path.size == 1) { "multi-level paths are not implemented yet" }
        modify(path[0], value)
    }


}