package `fun`.adaptive.ui.mpw.backends

import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.resource.GraphicsResourceKey
import `fun`.adaptive.resource.StringResourceKey
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.resource.resolve.resolveString
import `fun`.adaptive.ui.generated.resources.empty
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.AbstractPaneAction
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.snackbar.failNotification
import `fun`.adaptive.ui.snackbar.successNotification
import `fun`.adaptive.ui.value.iconCache
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.firstInstance

abstract class PaneViewBackend<VB : PaneViewBackend<VB>> : SelfObservable<VB>() {

    val uuid: UUID<PaneViewBackend<*>> = uuid4()

    abstract val paneDef: PaneDef

    abstract val workspace: MultiPaneWorkspace

    // TODO pane name, icon and tooltip belongs to the pane def, maybe the pane def itself should be changed
    var name : String? = null
    var icon : GraphicsResourceSet? = null
    var tooltip : String? = null

    open fun accepts(item: Any, modifiers: Set<EventModifier>): Boolean {
        return false
    }

    open fun load(item: Any, modifiers: Set<EventModifier>) {
        // devNote { "PaneViewBackend.load($item, $modifiers) - using basic implementation" }
    }

    fun resolveString(key : StringResourceKey) = workspace.resolveString(key)

    fun resolveIcon(key : GraphicsResourceKey) = iconCache[key] ?: Graphics.empty

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

    open fun getPaneActions(): List<AbstractPaneAction> =
        emptyList()

    /**
     * Called when the workspace disposes of the pane. This is different from disposing the
     * fragment. Fragment disposal may happen when the user switches to a different pane and
     * the pane becomes invisible. However, this should not dispose the backend itself as it
     * may have a loaded state that should be preserved.
     */
    open fun dispose() {

    }
}