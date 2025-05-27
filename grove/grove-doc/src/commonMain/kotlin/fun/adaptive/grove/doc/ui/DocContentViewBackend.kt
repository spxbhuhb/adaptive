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
import `fun`.adaptive.value.util.checkValue

class DocContentViewBackend(
    override val workspace: MultiPaneWorkspace,
    override val paneDef: PaneDef,
    var item : GroveDocValue
) : PaneViewBackend<DocContentViewBackend>() {

    override fun accepts(modifiers: Set<EventModifier>, item: WsPaneItem): Boolean {
        return item is AvValue<*> && item.spec is GroveDocSpec && avDomain.node in item.markers
    }

    override fun load(modifiers: Set<EventModifier>, item: WsPaneItem) {
        this.item = checkValue(item).checkSpec()
        name = item.nameLike
    }

}