package `fun`.adaptive.iot.value.builtin

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.item.AioStatus
import `fun`.adaptive.iot.value.AioValue
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.ui.workspace.model.WsItemType
import kotlinx.datetime.Instant

@Adat
class AvString(
    override val uuid: AioValueId,
    override val timestamp: Instant,
    override val status: AioStatus,
    val value : String
) : AioValue() {

    override val name: String
        get() = "String"

    override val type: WsItemType
        get() = "String"

}