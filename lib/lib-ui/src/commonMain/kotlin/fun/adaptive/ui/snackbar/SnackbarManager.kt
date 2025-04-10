package `fun`.adaptive.ui.snackbar

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.launch
import kotlinx.coroutines.delay

class SnackbarManager : WorkerImpl<SnackbarManager> {

    override suspend fun run() {
        for (snack in pendingSnackChannel) {

            while (activeSnackStore.value.size >= 3) {
                delay(100)
            }

            activeSnackStore.value += snack

            launch {
                delay(3000)
                activeSnackStore.value = activeSnackStore.value.filterNot { it.id == snack.id }
            }
        }
    }

}