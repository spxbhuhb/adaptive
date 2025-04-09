package `fun`.adaptive.iot.domain.rht.ui.controller

import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.value.iconFor
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType

class RhtBrowserContentController(
    override val workspace: Workspace
) : WsPaneController<RhtWsItem>() {

    val rhtFilter = storeFor { RhtFilter() }

    override fun accepts(pane: WsPaneType<RhtWsItem>, modifiers: Set<EventModifier>, item: NamedItem): Boolean {
        return (item is RhtWsItem)
    }

    override fun load(pane: WsPaneType<RhtWsItem>, modifiers: Set<EventModifier>, item: NamedItem): WsPaneType<RhtWsItem> {

        return pane.copy(
            name = item.name,
            data = item as RhtWsItem,
            icon = iconFor(item.avItem)
        )
    }

}