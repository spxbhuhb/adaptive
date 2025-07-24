package `fun`.adaptive.grove.doc.model

import `fun`.adaptive.adat.Adat
import kotlin.time.Instant

@Adat
class GroveDocSpec(
    val repoPath : String,
    val lastUpdate : Instant?,
    val content : String
)