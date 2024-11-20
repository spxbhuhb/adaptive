package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.AutoItemBackend
import `fun`.adaptive.auto.internal.persistence.AutoItemPersistence
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope

class AutoItem<BE : AutoItemBackend<IT>, PT : AutoItemPersistence<IT>, IT : AdatClass>(
    defaultWireFormat: AdatClassWireFormat<*>?,
    wireFormatProvider: WireFormatProvider,
    scope: CoroutineScope
) : AutoInstance<BE, PT, IT, IT>(
    defaultWireFormat, wireFormatProvider, scope
) {

    val value: IT
        get() = getItem()
//
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