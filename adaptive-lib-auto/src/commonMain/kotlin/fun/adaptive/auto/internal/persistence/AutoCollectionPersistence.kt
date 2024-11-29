package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.wireformat.WireFormatProvider

abstract class AutoCollectionPersistence<IT : AdatClass>(
    val wireFormatProvider: WireFormatProvider,
) : AutoPersistence<Collection<IT>, IT>() {

    abstract override fun load(): AutoCollectionExport<IT>?

    abstract fun update(export: AutoCollectionExport<IT>)

    abstract fun add(export: AutoItemExport<IT>)

    abstract fun update(export: AutoItemExport<IT>)

    abstract fun remove(itemId: ItemId, item : IT?)

}