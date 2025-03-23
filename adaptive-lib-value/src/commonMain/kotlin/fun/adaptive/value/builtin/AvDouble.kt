package `fun`.adaptive.value.builtin

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.ui.workspace.model.WsItemType
import kotlinx.datetime.Instant

@Adat
class AvDouble(
    override val uuid: AvValueId,
    override val timestamp: Instant,
    override val status: AvStatus,
    override val parentId: AvValueId?,
    val value : Double,
) : AvValue() {

    override val name: String
        get() = "Double"

    override val type: WsItemType
        get() = "Double"

}