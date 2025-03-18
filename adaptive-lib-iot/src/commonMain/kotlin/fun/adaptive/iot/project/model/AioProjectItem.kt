package `fun`.adaptive.iot.project.model

import `fun`.adaptive.iot.common.AioMarkerSet
import `fun`.adaptive.iot.project.AioProjectId
import `fun`.adaptive.iot.project.FriendlyItemId
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.utility.UUID

abstract class AioProjectItem<T> : WsItem() {
    abstract val uuid: UUID<T>
    abstract val projectId: AioProjectId
    abstract val friendlyId: FriendlyItemId
    abstract val markers: AioMarkerSet
}