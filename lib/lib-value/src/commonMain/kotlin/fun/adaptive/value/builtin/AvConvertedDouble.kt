package `fun`.adaptive.value.builtin

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.model.NamedItemType
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.value.AvValue2
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvStatus
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

@Adat
data class AvConvertedDouble(
    override val uuid: AvValueId,
    override val timestamp: Instant,
    override val status: AvStatus,
    override val parentId: AvValueId?,
    val originalValue: Double,
    val convertedValue: Double,
) : AvValue2() {

    constructor(parentId: AvValueId, sourceValue: Double, value: Double) : this(
        uuid7(),
        now(),
        AvStatus.OK,
        parentId,
        sourceValue,
        value
    )

    override val name: String
        get() = "ConvertedDouble"

    override val type: NamedItemType
        get() = "ConvertedDouble"

}