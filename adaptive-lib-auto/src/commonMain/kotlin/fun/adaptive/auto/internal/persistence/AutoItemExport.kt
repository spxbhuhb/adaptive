package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.model.AutoMetadata
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp

class AutoItemExport<IT>(
    override val meta: AutoMetadata?,
    val itemId: ItemId?,
    val propertyTimes: List<LamportTimestamp>?,
    val item: IT?
) : AutoExport<IT>() {

    companion object {
        val NONE = AutoItemExport(null, null, null, null)

        @Suppress("UNCHECKED_CAST")
        fun <IT : AdatClass> none() = NONE as AutoItemExport<IT>
    }

}