package `fun`.adaptive.value

import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.model.NamedItem
import kotlinx.datetime.Instant

abstract class AvValue : NamedItem() {
    abstract val uuid: AvValueId
    abstract val timestamp: Instant
    abstract val status: AvStatus
    abstract val parentId: AvValueId?
}