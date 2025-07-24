package `fun`.adaptive.ui.snackbar

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID
import kotlin.time.Clock.System.now
import kotlin.time.Instant

@Adat
class Snack(
    val id : UUID<Snack>,
    val message: String,
    val type : SnackType,
    val createdAt : Instant = now(),
    val shownAt : Instant? = null
)