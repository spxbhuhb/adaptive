package `fun`.adaptive.value

import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvMarker

fun AvValueWorker.firstItem(marker: AvMarker, condition: (AvItem<*>) -> Boolean) =
    queryByMarker(marker).filterIsInstance<AvItem<*>>().first(condition)

fun AvValueWorker.firstItemOrNull(marker: AvMarker, condition: (AvItem<*>) -> Boolean) =
    queryByMarker(marker).filterIsInstance<AvItem<*>>().firstOrNull(condition)
