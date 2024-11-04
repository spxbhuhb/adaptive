package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.api.deepCopy
import `fun`.adaptive.adat.api.validateForContext
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.AutoItemBackend
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.ItemId

open class AdatClassFrontend<IT : AdatClass>(
    override val instance: AutoInstance<*, *, IT, IT>,
    initialValue: IT?,
    val collectionFrontend: AutoCollectionFrontend<IT>?,
    val itemId: ItemId = ItemId.CONNECTING,
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

    override fun load(): Pair<AutoConnectionInfo<IT>?, IT?> {
        throw UnsupportedOperationException("AdatClassFrontend does not persist values, so it cannot load them")
    }

    override fun removed(fromBackend: Boolean) {
        instance.onRemove(value, fromBackend)
    }

    /**
     * AdatClassListFrontend calls PropertyBackend on modify
     * PropertyBackend calls commit when finished
     */
    override fun commit(itemBackend: AutoItemBackend<IT>?, initial: Boolean, fromPeer: Boolean) {
        checkNotNull(itemBackend) { "internal error, item frontend commit without a backend" }

        val oldValue = valueOrNull
        val newValue = itemBackend.getItem()

        @Suppress("UNCHECKED_CAST")
        newValue.adatContext = adatContext as AdatContext<Any>
        newValue.validateForContext()

        valueOrNull = newValue

        if (collectionFrontend != null) {
            collectionFrontend.commit(itemId, newValue, oldValue, initial, fromPeer)
        } else {
            instance.onChange(newValue, oldValue, fromPeer)
        }
    }

}