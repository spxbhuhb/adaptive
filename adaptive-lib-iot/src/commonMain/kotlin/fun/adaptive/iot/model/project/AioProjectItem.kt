package `fun`.adaptive.iot.model.project

import `fun`.adaptive.iot.model.AioProjectId
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.utility.UUID

abstract class AioProjectItem<T> : WsItem() {
    abstract val uuid: UUID<T>
    abstract val projectId: AioProjectId
    abstract val friendlyId: FriendlyItemId
}