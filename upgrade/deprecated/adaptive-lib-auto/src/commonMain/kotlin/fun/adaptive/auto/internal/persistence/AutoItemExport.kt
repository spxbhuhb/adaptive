package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.auto.model.AutoMetadata
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp

data class AutoItemExport<IT>(
    override val meta: AutoMetadata?,
    val itemId: ItemId?,
    val propertyTimes: List<LamportTimestamp>?,
    val item: IT?
) : AutoExport<IT>()