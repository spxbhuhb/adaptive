package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.wireformat.WireFormatProvider

class CollectionMemoryPersistence<IT : AdatClass>(
    wireFormatProvider: WireFormatProvider,
) : AutoCollectionPersistence<IT>(
    wireFormatProvider
) {

    override fun load(): AutoCollectionExport<IT> {
        return AutoCollectionExport(null, emptyList())
    }

    override fun save(export: AutoCollectionExport<IT>) {
        // no-op for memory
    }
}