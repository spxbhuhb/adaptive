package `fun`.adaptive.auth.model

import `fun`.adaptive.adat.Adat
import kotlin.time.Instant

@Adat
class Credential(
    val type: String,
    val value: String,
    var createdAt: Instant
)