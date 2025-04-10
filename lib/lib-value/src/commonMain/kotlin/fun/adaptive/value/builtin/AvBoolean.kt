package `fun`.adaptive.value.builtin

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.model.NamedItemType
import `fun`.adaptive.utility.UUID.Companion.uuid7
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

@Adat
class AvBoolean(
    override val uuid: AvValueId,
    override val timestamp: Instant,
    override val status: AvStatus,
    override val parentId: AvValueId?,
    val value : Boolean,
) : AvValue() {

    constructor(parentId: AvValueId, value: Boolean) : this(
        uuid7(),
        now(),
        AvStatus.OK,
        parentId,
        value
    )

    override val name: String
        get() = "Boolean"

    override val type: NamedItemType
        get() = "Boolean"

}