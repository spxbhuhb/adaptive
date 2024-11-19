package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.AutoItemBackend
import `fun`.adaptive.auto.internal.frontend.AutoItemFrontend
import `fun`.adaptive.auto.internal.persistence.AutoItemPersistence
import `fun`.adaptive.auto.internal.persistence.AutoPersistence
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
        get() = backend.getItem()

    override fun commit(itemBackend: AutoItemBackend<IT>?, initial: Boolean, fromPeer: Boolean) {
        frontend.commit(itemBackend, initial, fromPeer)
    }

    fun update(propertyName: String, value: Any?) {
        backend.update(propertyName, value)
    }

    fun update(path: Array<String>, value: Any?) {
        check(path.size == 1) { "multi-level paths are not implemented yet" }
        backend.update(path[0], value)
    }

    fun update(new: IT) {
        backend.update(new)
    }


}