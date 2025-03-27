package `fun`.adaptive.ui.workspace.logic

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.model.NamedItem
import kotlin.reflect.KClass

class WsClassPaneController<D : Any>(
    val kClass : KClass<D>
) : WsPaneController<D>() {

    override fun accepts(pane: WsPaneType<D>, modifiers: Set<EventModifier>, item: NamedItem) =
        kClass.isInstance(item)

    @Suppress("UNCHECKED_CAST")
    override fun load(pane: WsPaneType<D>, modifiers: Set<EventModifier>, item: NamedItem) =
        pane.copy(data = item as D)

}