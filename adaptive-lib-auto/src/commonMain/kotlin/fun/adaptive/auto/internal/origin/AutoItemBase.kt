package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.internal.frontend.AutoItemFrontend
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope

class AutoItemBase<BE : AutoBackend<IT>, FE : AutoItemFrontend<IT>, IT : AdatClass>(
    defaultWireFormat: AdatClassWireFormat<*>?,
    wireFormatProvider: WireFormatProvider,
    scope: CoroutineScope
) : AutoInstance<BE, FE, IT, IT>(
    defaultWireFormat, wireFormatProvider, scope
) {

    val value: IT
        get() = frontend.value

}