package `fun`.adaptive.ui.mpw.backends

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.Pane
import `fun`.adaptive.ui.mpw.model.WsPaneItem
import kotlin.reflect.KClass

class ClassPaneViewBackend<C>(
    override val workspace: MultiPaneWorkspace,
    val kClass : KClass<*>
) : PaneViewBackend<ClassPaneViewBackend<C>>() {

    val item : C
        get() = itemOrNull!!

    var itemOrNull : C? = null

    override fun accepts(pane: Pane<ClassPaneViewBackend<C>>, modifiers: Set<EventModifier>, item: WsPaneItem) =
        kClass.isInstance(item)

    @Suppress("UNCHECKED_CAST")
    override fun load(pane: Pane<ClassPaneViewBackend<C>>, modifiers: Set<EventModifier>, item: WsPaneItem) : Pane<ClassPaneViewBackend<C>> {
        this.itemOrNull = item as C
        return pane.copy()
    }

}