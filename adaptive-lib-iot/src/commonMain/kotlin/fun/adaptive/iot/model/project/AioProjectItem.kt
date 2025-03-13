package `fun`.adaptive.iot.model.project

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.utility.UUID

abstract class AioProjectItem<T> : AdatClass {
    abstract val uuid: UUID<T>
    abstract val projectId: AioProjectId
    abstract val friendlyId: FriendlyItemId
    abstract val name: String
}