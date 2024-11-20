package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.wireformat.WireFormatProvider

class ItemMemoryPersistence<IT : AdatClass>(
    wireFormatProvider: WireFormatProvider,
) : AutoItemPersistence<IT>(
    wireFormatProvider
) {

    override fun load(): AutoItemExport<IT> {
        return AutoItemExport(null, null, null, null)
    }

    override fun save(export: AutoItemExport<IT>) {
        // no-op for memory
    }

}