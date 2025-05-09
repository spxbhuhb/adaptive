package `fun`.adaptive.value.builtin

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.AvValue2
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.model.NamedItemType
import `fun`.adaptive.utility.UUID.Companion.uuid7
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

@Adat
data class AvDouble(
    override val uuid: AvValueId,
    override val timestamp: Instant,
    override val status: AvStatus,
    override val parentId: AvValueId?,
    val value : Double,
) : AvValue2() {

    constructor(parentId: AvValueId, value: Double, timestamp : Instant = now()) : this(
        uuid7(),
        timestamp,
        AvStatus.OK,
        parentId,
        value
    )

    override val name: String
        get() = "Double"

    override val type: NamedItemType
        get() = "Double"

}