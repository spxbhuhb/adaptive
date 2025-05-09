package `fun`.adaptive.value

import `fun`.adaptive.utility.UUID.Companion.uuid7
import kotlinx.datetime.Instant

fun AvValueWorker.firstItem(marker: AvMarker, condition: (AvValue<*>) -> Boolean) =
    queryByMarker(marker).filterIsInstance<AvValue<*>>().first(condition)

fun AvValueWorker.firstItemOrNull(marker: AvMarker, condition: (AvValue<*>) -> Boolean) =
    queryByMarker(marker).filterIsInstance<AvValue<*>>().firstOrNull(condition)

fun avBoolean(
    value: Boolean,
    timestamp: Instant,
    uuid: AvValueId = uuid7(),
    parentId: AvValueId? = null,
    status: Set<String>? = null
) =
    AvValue(
        name = "<anonymous>",
        type = "kotlin.Boolean",
        uuid = uuid,
        timestamp = timestamp,
        parentId = parentId,
        statusOrNull = status,
        spec = value
    )

fun avDouble(
    value: Double,
    timestamp: Instant,
    uuid: AvValueId = uuid7(),
    parentId: AvValueId? = null,
    status: Set<String>? = null
) =
    AvValue(
        name = "<anonymous>",
        type = "kotlin.Double",
        uuid = uuid,
        timestamp = timestamp,
        parentId = parentId,
        statusOrNull = status,
        spec = value
    )

fun avString(
    value: String,
    timestamp: Instant,
    uuid: AvValueId,
    parentId: AvValueId? = null,
    status: Set<String>? = null
) =
    AvValue(
        name = "<anonymous>",
        type = "kotlin.String",
        uuid = uuid,
        timestamp = timestamp,
        parentId = parentId,
        statusOrNull = status,
        spec = value
    )