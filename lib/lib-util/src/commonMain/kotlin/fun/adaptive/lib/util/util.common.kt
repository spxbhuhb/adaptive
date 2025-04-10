package `fun`.adaptive.lib.util

import `fun`.adaptive.lib.util.temporal.model.TemporalIndexHeader
import `fun`.adaptive.lib.util.temporal.model.TemporalIndexEntry
import `fun`.adaptive.wireformat.WireFormatRegistry

fun utilCommon() {
    val r = WireFormatRegistry
    r += TemporalIndexHeader
    r += TemporalIndexEntry
}