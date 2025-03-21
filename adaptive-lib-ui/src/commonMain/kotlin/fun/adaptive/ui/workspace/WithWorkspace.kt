package `fun`.adaptive.ui.workspace

import `fun`.adaptive.ui.snackbar.failNotification
import `fun`.adaptive.ui.snackbar.successNotification

interface WithWorkspace {

    val workspace: Workspace

    fun io(suspendFun: suspend () -> Unit) = workspace.io(suspendFun)

    fun remote(
        successMessage: String,
        errorMessage: String,
        remoteFun: suspend () -> Unit
    ) {
        workspace.io {
            try {
                remoteFun()
                successNotification(successMessage)
            } catch (ex: Exception) {
                workspace.logger.error(ex)
                failNotification(errorMessage)
            }
        }
    }

    fun ui(uiFun: () -> Unit) = uiFun()

    val backend
        get() = workspace.backend

    val transport
        get() = workspace.transport

    val scope
        get() = workspace.scope

}