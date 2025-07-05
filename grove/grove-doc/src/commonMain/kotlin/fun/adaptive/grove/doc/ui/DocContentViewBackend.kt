package `fun`.adaptive.grove.doc.ui

import `fun`.adaptive.grove.doc.api.GroveDocApi
import `fun`.adaptive.grove.doc.model.GroveDocSpec
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.ui.mpw.model.PaneContentItem
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.value.AvValue

class DocContentViewBackend(
    override val workspace: MultiPaneWorkspace,
    override val paneDef: PaneDef,
    var content : GroveDocContentItem
) : PaneViewBackend<DocContentViewBackend>() {

    val service = getService<GroveDocApi>(backend.transport)

    override fun accepts(item: PaneContentItem, modifiers: Set<EventModifier>): Boolean {
        return item is GroveDocContentItem
    }

    override fun load(item: PaneContentItem, modifiers: Set<EventModifier>) {
        this.content = item as GroveDocContentItem
        name = item.path.lastOrNull() ?: ""
        notifyListeners()
    }

    suspend fun getLoadedValue() : AvValue<GroveDocSpec>? {
        return service.getByPath(content.path)
    }

}