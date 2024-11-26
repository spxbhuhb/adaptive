package `fun`.adaptive.auto.internal.instance

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.AutoItemBackend
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.persistence.AutoItemExport
import `fun`.adaptive.auto.internal.persistence.AutoItemPersistence
import `fun`.adaptive.auto.model.AutoMetadata
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KProperty

class AutoItem<BE : AutoItemBackend<IT>, PT : AutoItemPersistence<IT>, IT : AdatClass>(
    origin: Boolean,
    defaultWireFormat: AdatClassWireFormat<*>?,
    wireFormatProvider: WireFormatProvider,
    scope: CoroutineScope
) : AutoInstance<BE, PT, IT, IT>(
    origin, defaultWireFormat, wireFormatProvider, scope
) {

    val value: IT
        get() = getItem()

    val valueOrNull : IT?
        get() = getItemOrNull()

    fun <V> update(vararg changes: Pair<KProperty<V>, V>) {
        localUpdate(null, changes.map { it.first.name to it.second })
    }

    override fun persistenceInit() {
        persistenceUpdate(backend.itemId, backend.getItem())
    }

    override fun persistenceUpdate(itemId: ItemId, value: IT) {
        persistence.save(
            AutoItemExport<IT>(
                meta = AutoMetadata(connectionInfo!!, null, null),
                itemId = handle.itemId,
                propertyTimes = (backend as PropertyBackend<*>).propertyTimes.toList(),
                item = value
            )
        )
    }

//    fun update(propertyName: String, value: Any?) {
//        backend.remoteUpdate(propertyName, value)
//    }
//
//    fun update(path: Array<String>, value: Any?) {
//        check(path.size == 1) { "multi-level paths are not implemented yet" }
//        backend.remoteUpdate(path[0], value)
//    }
//
//    fun update(new: IT) {
//        backend.remoteUpdate(new)
//    }


}