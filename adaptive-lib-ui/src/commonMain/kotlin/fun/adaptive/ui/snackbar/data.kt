package `fun`.adaptive.ui.snackbar

import `fun`.adaptive.auto.api.autoCollectionOrigin
import kotlinx.coroutines.channels.Channel

/**
 * Snackbars waiting to be shown.
 */
val pendingSnacks = Channel<Snack>(capacity = 100)

/**
 * Snackbars being shown right now.
 */
val activeSnacks = autoCollectionOrigin(emptyList<Snack>())

/**
 * All snackbars created so far.
 */
val snacks = autoCollectionOrigin(emptyList<Snack>())