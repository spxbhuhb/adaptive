package `fun`.adaptive.ui.mpw.backends

import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.snackbar.failNotification
import `fun`.adaptive.ui.snackbar.successNotification
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.AbstractPaneAction
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.WsPaneItem
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.firstInstance

abstract class PaneViewBackend<VB : PaneViewBackend<VB>> : SelfObservable<VB>() {

    val paneId: UUID<PaneViewBackend<*>> = uuid4()

    abstract val paneDef: PaneDef

    abstract val workspace: MultiPaneWorkspace

    var name = ""

    open fun accepts(modifiers: Set<EventModifier>, item: WsPaneItem): Boolean {
        return false
    }

    open fun load(modifiers: Set<EventModifier>, item: WsPaneItem) {
        throw UnsupportedOperationException("load for pane $paneDef is not supported")
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

    open fun paneActions(): List<AbstractPaneAction> =
        emptyList()

}