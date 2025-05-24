package `fun`.adaptive.grove.doc.lib.compiler

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.log.LogLevel

@Adat
class GroveDocNotification(
    val level: LogLevel,
    val message: String,
    val paths: List<String>
)
