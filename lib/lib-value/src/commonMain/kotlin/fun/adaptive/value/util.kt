package `fun`.adaptive.value

fun AvValueWorker.firstItem(marker: AvMarker, condition: (AvValue<*>) -> Boolean) =
    queryByMarker(marker).filterIsInstance<AvValue<*>>().first(condition)

fun AvValueWorker.firstItemOrNull(marker: AvMarker, condition: (AvValue<*>) -> Boolean) =
    queryByMarker(marker).filterIsInstance<AvValue<*>>().firstOrNull(condition)
