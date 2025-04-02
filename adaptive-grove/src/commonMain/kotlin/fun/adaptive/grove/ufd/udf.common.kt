package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.grove.resources.*
import `fun`.adaptive.grove.ufd.logic.UfdWsContentController
import `fun`.adaptive.grove.ufd.model.UfdWsContentPaneData
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneController
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun AdaptiveAdapter.groveUfdCommon() {
    fragmentFactory += arrayOf(UfdPaneFactory)
}

fun Workspace.groveUfdCommon() {
    contexts += UfdWsContext(this)

    toolPanes += WsPane(
        UUID(),
        workspace = this,
        Strings.palette,
        Graphics.palette,
        WsPanePosition.LeftTop,
        UfdWsContext.PALETTE_TOOL_KEY,
        data = Unit,
        controller = WsUnitPaneController(this)
    )

    toolPanes += WsPane(
        UUID(),
        workspace = this,
        Strings.components,
        Graphics.cards,
        WsPanePosition.LeftMiddle,
        UfdWsContext.COMPONENTS_TOOL_KEY,
        data = Unit,
        controller = WsUnitPaneController(this)
    )

    toolPanes += WsPane(
        UUID(),
        workspace = this,
        Strings.instructions,
        Graphics.stroke_partial,
        WsPanePosition.RightTop,
        UfdWsContext.INSTRUCTIONS_TOOL_KEY,
        data = Unit,
        controller = WsUnitPaneController(this)
    )

    toolPanes += WsPane(
        UUID(),
        workspace = this,
        Strings.state,
        Graphics.data_table,
        WsPanePosition.RightTop,
        UfdWsContext.STATE_TOOL_KEY,
        data = Unit,
        controller = WsUnitPaneController(this)
    )

    addContentPaneBuilder(UfdWsContext.WSIT_UFD_FRAGMENT) { item ->

        WsPane(
            UUID(),
            workspace = this,
            item.name,
            Graphics.cards,
            WsPanePosition.Center,
            UfdWsContext.CONTENT_PANE_KEY,
            data = UfdWsContentPaneData(),
            controller = UfdWsContentController(this)
        )

    }

    addItemConfig(UfdWsContext.WSIT_UFD_FRAGMENT, Graphics.cards)

}