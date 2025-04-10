package `fun`.adaptive.ui.snackbar

import `fun`.adaptive.foundation.value.storeFor
import kotlinx.coroutines.channels.Channel

/**
 * Snackbars waiting to be shown.
 */
val pendingSnackChannel = Channel<Snack>(capacity = 100)

/**
 * Snackbars being shown right now.
 */
val activeSnackStore = storeFor { emptyList<Snack>() }

/**
 * All snackbars created so far.
 */
val snackStore = storeFor { mutableListOf<Snack>() }