package `fun`.adaptive.ui.mpw.backends

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.snackbar.failNotification
import `fun`.adaptive.ui.snackbar.successNotification
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.Pane
import `fun`.adaptive.ui.mpw.model.WsPaneItem
import `fun`.adaptive.utility.firstInstance

abstract class PaneViewBackend<VB : PaneViewBackend<VB>> {

    abstract val workspace : MultiPaneWorkspace

    open fun accepts(pane: Pane<VB>, modifiers: Set<EventModifier>, item: WsPaneItem): Boolean {
        return false
    }

    open fun load(pane: Pane<VB>, modifiers: Set<EventModifier>, item: WsPaneItem): Pane<VB> {
        throw UnsupportedOperationException("load for pane $pane is not supported")
    }

    fun io(suspendFun: suspend () -> Unit) = workspace.io(suspendFun)

    inline fun <reified T> context() =
        workspace.contexts.firstInstance<T>()

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