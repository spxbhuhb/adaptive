package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoMetadata
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp

class ItemLoad(
    val meta : AutoMetadata<*>?,
    val itemId : ItemId?,
    val propertyTimes : List<LamportTimestamp>?,
    val values : Array<*>?
) {

    companion object {
        val NONE = ItemLoad(null, null, null, null)
    }

}