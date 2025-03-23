package `fun`.adaptive.value.builtin

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.ui.workspace.model.WsItemType
import kotlinx.datetime.Instant

@Adat
class AvString(
    override val uuid: AvValueId,
    override val timestamp: Instant,
    override val status: AvStatus,
    override val parentId: AvValueId?,
    val value : String
) : AvValue() {

    override val name: String
        get() = "String"

    override val type: WsItemType
        get() = "String"

}