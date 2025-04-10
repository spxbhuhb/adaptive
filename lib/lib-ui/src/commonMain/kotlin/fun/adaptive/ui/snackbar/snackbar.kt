package `fun`.adaptive.ui.snackbar

import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.UUID

/**
 * Creates a [Snack] and adds it to [pendingSnackChannel].
 *
 * [pendingSnackChannel] is an auto list which is handled by the snackbar manager.
 */
fun snackbar(message: String, type: SnackType) {
    val snack = Snack(UUID(), message, type)
    snackStore.add(snack)

    val result = pendingSnackChannel.trySend(snack)
    if (! result.isSuccess) {
        getLogger("snackbar").warning("snack queue is full, snack not shown: $snack")
    }
}

fun successNotification(message: String) {
    snackbar(message, SnackType.Success)
}

fun infoNotification(message: String) {
    snackbar(message, SnackType.Info)
}

fun warningNotification(message: String) {
    snackbar(message, SnackType.Warning)
}

fun failNotification(message: String) {
    snackbar(message, SnackType.Fail)
}