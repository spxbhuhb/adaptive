package `fun`.adaptive.ui.workspace.logic

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.model.NamedItem

abstract class WsPaneController<D> {

    open fun accepts(pane: WsPaneType<D>, modifiers: Set<EventModifier>, item: NamedItem): Boolean {
        return false
    }

    open fun load(pane: WsPaneType<D>, modifiers: Set<EventModifier>, item: NamedItem): WsPaneType<D> {
        throw UnsupportedOperationException("load for pane $pane is not supported")
    }

}