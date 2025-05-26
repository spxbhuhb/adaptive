package `fun`.adaptive.ui.workspace.logic

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneItem
import kotlin.reflect.KClass

class WsClassPaneViewBackend<C>(
    override val workspace: MultiPaneWorkspace,
    val kClass : KClass<*>
) : WsPaneViewBackend<WsClassPaneViewBackend<C>>() {

    val item : C
        get() = itemOrNull!!

    var itemOrNull : C? = null

    override fun accepts(pane: WsPane<WsClassPaneViewBackend<C>>, modifiers: Set<EventModifier>, item: WsPaneItem) =
        kClass.isInstance(item)

    @Suppress("UNCHECKED_CAST")
    override fun load(pane: WsPane<WsClassPaneViewBackend<C>>, modifiers: Set<EventModifier>, item: WsPaneItem) : WsPane<WsClassPaneViewBackend<C>> {
        this.itemOrNull = item as C
        return pane.copy()
    }

}