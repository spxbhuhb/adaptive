package `fun`.adaptive.ui.snackbar

import `fun`.adaptive.auto.api.autoCollectionOrigin
import kotlinx.coroutines.channels.Channel

/**
 * Snackbars waiting to be shown.
 */
val pendingSnackChannel = Channel<Snack>(capacity = 100)

/**
 * Snackbars being shown right now.
 */
val activeSnackStore = autoCollectionOrigin(emptyList<Snack>())

/**
 * All snackbars created so far.
 */
val snackStore = autoCollectionOrigin(emptyList<Snack>())