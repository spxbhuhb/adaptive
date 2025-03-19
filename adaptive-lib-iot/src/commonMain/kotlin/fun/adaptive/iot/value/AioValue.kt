package `fun`.adaptive.iot.value

import `fun`.adaptive.iot.item.AioStatus
import `fun`.adaptive.ui.workspace.model.WsItem
import kotlinx.datetime.Instant

abstract class AioValue : WsItem() {
    abstract val uuid: AioValueId
    abstract val timestamp: Instant
    abstract val status: AioStatus
}