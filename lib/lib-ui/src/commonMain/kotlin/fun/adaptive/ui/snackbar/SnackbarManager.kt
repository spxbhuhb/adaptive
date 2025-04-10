package `fun`.adaptive.ui.snackbar

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.launch
import kotlinx.coroutines.delay

class SnackbarManager : WorkerImpl<SnackbarManager> {

    override suspend fun run() {
        for (snack in pendingSnackChannel) {

            while (activeSnackStore.size >= 3) {
                delay(100)
            }

            activeSnackStore.add(snack)

            launch {
                delay(3000)
                activeSnackStore.remove { it.id == snack.id }
            }
        }
    }

}