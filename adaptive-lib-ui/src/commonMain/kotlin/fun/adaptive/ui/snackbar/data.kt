package `fun`.adaptive.ui.snackbar

import `fun`.adaptive.auto.api.autoList
import `fun`.adaptive.foundation.instruction.trace
import kotlinx.coroutines.channels.Channel

/**
 * Snackbars waiting to be shown.
 */
val pendingSnacks = Channel<Snack>(capacity = 100)

/**
 * Snackbars being shown right now.
 */
val activeSnacks = autoList(emptyList<Snack>(), Snack.adatWireFormat)

/**
 * All snackbars created so far.
 */
val snacks = autoList(emptyList<Snack>(), Snack.adatWireFormat)