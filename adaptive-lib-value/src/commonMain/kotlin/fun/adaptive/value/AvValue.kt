package `fun`.adaptive.value

import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.ui.workspace.model.WsItem
import kotlinx.datetime.Instant

abstract class AvValue : WsItem() {
    abstract val uuid: AvValueId
    abstract val timestamp: Instant
    abstract val status: AvStatus
    abstract val parentId: AvValueId?
}