package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.instance.AutoInstance
import `fun`.adaptive.auto.internal.persistence.AutoItemExport
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp

abstract class AutoItemBackend<IT : AdatClass>(
    instance: AutoInstance<*, *, *, IT>
) : AutoBackend<IT>(instance) {

    abstract var itemId: ItemId

    abstract val wireFormat: AdatClassWireFormat<*>

    abstract fun getItem(): IT

    abstract fun getItemOrNull(): IT?

    /**
     * @param  withMeta  When true, the `meta` field of [AutoItemExport] is set,
     *                   when false, it will be `null`. Item instances use `true`,
     *                   collection instances use `false`.
     */
    abstract fun export(withMeta: Boolean) : AutoItemExport<IT>

    /**
     * Called by collection backends when the item of this backend is removed from the collection.
     */
    fun removed(fromBackend: Boolean) {
        // FIXME frontend?.removed(fromBackend)
    }

    internal abstract fun encode(): ByteArray

    internal abstract fun propertyTimes() : List<LamportTimestamp>

}