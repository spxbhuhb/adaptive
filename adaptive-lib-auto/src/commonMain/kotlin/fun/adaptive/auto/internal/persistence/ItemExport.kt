package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.model.AutoMetadata
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp

class ItemExport<IT>(
    val meta: AutoMetadata<*>?,
    val itemId: ItemId?,
    val propertyTimes: List<LamportTimestamp>?,
    val value: IT?,
) {

    companion object {
        val NONE = ItemExport(null, null, null, null)

        @Suppress("UNCHECKED_CAST")
        fun <IT : AdatClass> none() = NONE as ItemExport<IT>
    }

}