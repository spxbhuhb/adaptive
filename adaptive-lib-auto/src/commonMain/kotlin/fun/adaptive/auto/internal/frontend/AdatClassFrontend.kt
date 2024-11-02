package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.api.deepCopy
import `fun`.adaptive.adat.api.validateForContext
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.auto.model.ItemId

open class AdatClassFrontend<IT : AdatClass>(
    override val instance: AutoInstance<*, *, IT, IT>,
    val wireFormat: AdatClassWireFormat<IT>,
    initialValue: IT?,
    val itemId: ItemId,
    val itemBackend: PropertyBackend<IT>,
    val collectionFrontend: AutoCollectionFrontend<IT>?,
) : AutoItemFrontend<IT>() {

    override val persistent: Boolean
        get() = false

    val adatContext = AdatContext<ItemId>(itemId, null, null, store = this, null)

    override val value: IT
        get() = checkNotNull(valueOrNull) { "value not yet initialized" }

    override var valueOrNull: IT? = initialValue?.deepCopy()?.also {
        @Suppress("UNCHECKED_CAST")
        it.adatContext = adatContext as AdatContext<Any>
    }

    override fun load(): Pair<AutoConnectionInfo<IT>?,IT?> {
        throw UnsupportedOperationException("AdatClassFrontend does not persist values, so it cannot load them")
    }

    override fun removed(fromBackend: Boolean) {
        instance.onRemove(value, fromBackend)
    }

    /**
     * AdatClassListFrontend calls PropertyBackend on modify
     * PropertyBackend calls commit when finished
     */
    override fun commit(initial: Boolean, fromBackend: Boolean) {
        val oldValue = valueOrNull
        val newValue = wireFormat.newInstance(itemBackend.values)

        @Suppress("UNCHECKED_CAST")
        newValue.adatContext = adatContext as AdatContext<Any>
        newValue.validateForContext()

        valueOrNull = newValue

        if (collectionFrontend != null) {
            collectionFrontend.commit(itemBackend.itemId, newValue, oldValue, initial, fromBackend)
        } else {
            instance.onChange(newValue, oldValue, fromBackend)
        }
    }

    fun modify(propertyName: String, propertyValue: Any?) {
        itemBackend.modify(itemBackend.itemId, propertyName, propertyValue)
    }

    override fun update(instance: AdatClass, path: Array<String>, value: Any?) {
        check(path.size == 1) { "multi-level paths are not implemented yet" }
        modify(path[0], value)
    }

    override fun update(original: AdatClass, new: AdatClass) {
        itemBackend.update(new.toArray())
    }

    override fun update(new: AdatClass) {
        itemBackend.update(new.toArray())
    }


}