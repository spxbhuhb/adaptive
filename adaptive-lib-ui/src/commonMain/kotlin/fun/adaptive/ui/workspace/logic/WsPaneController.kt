package `fun`.adaptive.ui.workspace.logic

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.ui.snackbar.failNotification
import `fun`.adaptive.ui.snackbar.successNotification
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.utility.firstInstance

abstract class WsPaneController<D> {

    abstract val workspace : Workspace

    open val adminTool
        get() = false

    open fun accepts(pane: WsPaneType<D>, modifiers: Set<EventModifier>, item: NamedItem): Boolean {
        return false
    }

    open fun load(pane: WsPaneType<D>, modifiers: Set<EventModifier>, item: NamedItem): WsPaneType<D> {
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