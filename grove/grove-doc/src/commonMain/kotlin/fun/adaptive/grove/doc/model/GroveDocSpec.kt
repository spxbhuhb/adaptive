package `fun`.adaptive.grove.doc.model

import `fun`.adaptive.adat.Adat
import kotlinx.datetime.Instant

@Adat
class GroveDocSpec(
    val lastUpdate : Instant?,
    val content : String
)