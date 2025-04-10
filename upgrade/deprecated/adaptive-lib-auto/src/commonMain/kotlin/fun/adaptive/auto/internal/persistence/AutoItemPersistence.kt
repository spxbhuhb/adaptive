package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.wireformat.WireFormatProvider

abstract class AutoItemPersistence<IT : AdatClass>(
    val wireFormatProvider: WireFormatProvider,
) : AutoPersistence<IT, IT>() {

    abstract override fun load(): AutoItemExport<IT>?

    abstract fun save(export: AutoItemExport<IT>)

}