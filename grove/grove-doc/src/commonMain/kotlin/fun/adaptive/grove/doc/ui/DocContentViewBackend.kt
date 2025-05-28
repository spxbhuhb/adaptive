package `fun`.adaptive.grove.doc.ui

import `fun`.adaptive.grove.doc.model.GroveDocSpec
import `fun`.adaptive.grove.doc.model.GroveDocValue
import `fun`.adaptive.grove.doc.model.avDomain
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.WsPaneItem
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValue.Companion.checkSpec
import `fun`.adaptive.value.util.asValue
import `fun`.adaptive.value.util.checkValue
import `fun`.adaptive.value.util.isValue

class DocContentViewBackend(
    override val workspace: MultiPaneWorkspace,
    override val paneDef: PaneDef,
    var content : GroveDocValue
) : PaneViewBackend<DocContentViewBackend>() {

    override fun accepts(item: WsPaneItem, modifiers: Set<EventModifier>): Boolean {
        return isValue<GroveDocSpec>(item, avDomain.node)
    }

    override fun load(item: WsPaneItem, modifiers: Set<EventModifier>) {
        this.content = asValue<GroveDocSpec>(item)
        name = item.nameLike
        notifyListeners()
    }

}