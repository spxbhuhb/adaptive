package `fun`.adaptive.ui.snackbar

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.log.LogLevel
import `fun`.adaptive.utility.UUID
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

@Adat
class Snack(
    val id : UUID<Snack>,
    val message: String,
    val type : SnackType,
    val createdAt : Instant = now(),
    val shownAt : Instant? = null
)