package `fun`.adaptive.ui.mpw.backends

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.SingularPaneItem

abstract class SingularContentViewBackend<T : SingularContentViewBackend<T>>(
    override val workspace: MultiPaneWorkspace,
    override val paneDef: PaneDef,
    val content: SingularPaneItem
) : PaneViewBackend<T>() {

    override fun accepts(item: Any, modifiers: Set<EventModifier>): Boolean {
        return (item === this.content)
    }

    override fun load(item: Any, modifiers: Set<EventModifier>) {
        name = paneDef.name
    }

}