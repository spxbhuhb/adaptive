package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.AutoItemBackend
import `fun`.adaptive.auto.internal.frontend.AutoItemFrontend
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope

class AutoItem<BE : AutoItemBackend<IT>, FE : AutoItemFrontend<IT>, IT : AdatClass>(
    defaultWireFormat: AdatClassWireFormat<*>?,
    wireFormatProvider: WireFormatProvider,
    scope: CoroutineScope
) : AutoInstance<BE, FE, IT, IT>(
    defaultWireFormat, wireFormatProvider, scope
) {

    val value: IT
        get() = frontend.value

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