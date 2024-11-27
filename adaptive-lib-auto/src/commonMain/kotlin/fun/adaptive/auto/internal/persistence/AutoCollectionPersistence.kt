package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.wireformat.WireFormatProvider

abstract class AutoCollectionPersistence<IT : AdatClass>(
    val wireFormatProvider: WireFormatProvider,
) : AutoPersistence<Collection<IT>, IT>() {

    abstract override fun load(): AutoCollectionExport<IT>

    abstract fun save(export: AutoCollectionExport<IT>)

}