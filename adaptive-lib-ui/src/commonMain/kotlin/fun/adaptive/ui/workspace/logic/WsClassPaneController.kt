package `fun`.adaptive.ui.workspace.logic

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.model.WsItem
import kotlin.reflect.KClass

class WsClassPaneController<D : Any>(
    val kClass : KClass<D>
) : WsPaneController<D>() {

    override fun accepts(pane: WsPaneType<D>, modifiers: Set<EventModifier>, item: WsItem) =
        kClass.isInstance(item)

    @Suppress("UNCHECKED_CAST")
    override fun load(pane: WsPaneType<D>, modifiers: Set<EventModifier>, item: WsItem) =
        pane.copy(data = item as D)

}