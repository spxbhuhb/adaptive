package `fun`.adaptive.ui.snackbar

import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.UUID

/**
 * Creates a [Snack] and adds it to [pendingSnacks].
 *
 * [pendingSnacks] is an auto list which is handled by the snackbar manager.
 */
fun snackbar(message: String, type: SnackType) {
    val snack = Snack(UUID(), message, type)
    snacks.add(snack)

    val result = pendingSnacks.trySend(snack)
    if (! result.isSuccess) {
        getLogger("snackbar").warning("snack queue is full, snack not shown: $snack")
    }
}

fun success(message: String) {
    snackbar(message, SnackType.Success)
}

fun info(message: String) {
    snackbar(message, SnackType.Info)
}

fun warning(message: String) {
    snackbar(message, SnackType.Warning)
}

fun fail(message: String) {
    snackbar(message, SnackType.Fail)
}