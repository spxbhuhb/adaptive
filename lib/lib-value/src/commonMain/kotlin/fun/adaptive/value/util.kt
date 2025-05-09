package `fun`.adaptive.value

import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.item.AvMarker

fun AvValueWorker.firstItem(marker: AvMarker, condition: (AvValue<*>) -> Boolean) =
    queryByMarker(marker).filterIsInstance<AvValue<*>>().first(condition)

fun AvValueWorker.firstItemOrNull(marker: AvMarker, condition: (AvValue<*>) -> Boolean) =
    queryByMarker(marker).filterIsInstance<AvValue<*>>().firstOrNull(condition)
