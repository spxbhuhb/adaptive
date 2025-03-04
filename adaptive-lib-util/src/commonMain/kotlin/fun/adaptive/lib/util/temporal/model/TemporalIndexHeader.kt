package `fun`.adaptive.lib.util.temporal.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID

@Adat
class TemporalIndexHeader(
    val version: Int = 1,
    val uuid: UUID<TemporalIndexHeader>
)