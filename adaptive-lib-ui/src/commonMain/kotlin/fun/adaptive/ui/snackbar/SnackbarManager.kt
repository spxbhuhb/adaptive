package `fun`.adaptive.ui.snackbar

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.launch
import kotlinx.coroutines.delay

class SnackbarManager : WorkerImpl<SnackbarManager> {

    override suspend fun run() {
        for (snack in pendingSnacks) {

            while (activeSnacks.size >= 3) {
                delay(100)
            }

            activeSnacks.add(snack)

            launch {
                delay(3000)
                activeSnacks.remove { it.id == snack.id }
            }
        }
    }

}