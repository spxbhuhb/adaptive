package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.grove.resources.*
import `fun`.adaptive.grove.ufd.logic.UfdWsContentController
import `fun`.adaptive.grove.ufd.model.UfdWsContentPaneData
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.workspace.Workspace
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
        Strings.palette,
        Graphics.palette,
        WsPanePosition.LeftTop,
        UfdWsContext.PALETTE_TOOL_KEY,
        model = Unit
    )

    toolPanes += WsPane(
        UUID(),
        Strings.components,
        Graphics.cards,
        WsPanePosition.LeftMiddle,
        UfdWsContext.COMPONENTS_TOOL_KEY,
        model = Unit
    )

    toolPanes += WsPane(
        UUID(),
        Strings.instructions,
        Graphics.stroke_partial,
        WsPanePosition.RightTop,
        UfdWsContext.INSTRUCTIONS_TOOL_KEY,
        model = Unit
    )

    toolPanes += WsPane(
        UUID(),
        Strings.state,
        Graphics.data_table,
        WsPanePosition.RightTop,
        UfdWsContext.STATE_TOOL_KEY,
        model = Unit
    )

    addContentPaneBuilder(UfdWsContext.WSIT_UFD_FRAGMENT) { item ->

        WsPane(
            UUID(),
            item.name,
            Graphics.cards,
            WsPanePosition.Center,
            UfdWsContext.CONTENT_PANE_KEY,
            model = UfdWsContentPaneData(),
            controller = UfdWsContentController()
        )

    }

    addItemConfig(UfdWsContext.WSIT_UFD_FRAGMENT, Graphics.cards)

}