package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace

fun AdaptiveAdapter.groveUfdCommon() {
    fragmentFactory += arrayOf(UfdPaneFactory)
}

fun MultiPaneWorkspace.groveUfdCommon() {
    contexts += UfdWsContext(this)
//
//    toolPanes += PaneDef(
//        UUID(),
//        workspace = this,
//        Strings.palette,
//        Graphics.palette,
//        PanePosition.LeftTop,
//        UfdWsContext.PALETTE_TOOL_KEY,
//        viewBackend = UnitPaneViewBackend(this)
//    )
//
//    toolPanes += PaneDef(
//        UUID(),
//        workspace = this,
//        Strings.components,
//        Graphics.cards,
//        PanePosition.LeftMiddle,
//        UfdWsContext.COMPONENTS_TOOL_KEY,
//        viewBackend = UnitPaneViewBackend(this)
//    )
//
//    toolPanes += PaneDef(
//        UUID(),
//        workspace = this,
//        Strings.instructions,
//        Graphics.stroke_partial,
//        PanePosition.RightTop,
//        UfdWsContext.INSTRUCTIONS_TOOL_KEY,
//        viewBackend = UnitPaneViewBackend(this)
//    )
//
//    toolPanes += PaneDef(
//        UUID(),
//        workspace = this,
//        Strings.state,
//        Graphics.data_table,
//        PanePosition.RightTop,
//        UfdWsContext.STATE_TOOL_KEY,
//        viewBackend = UnitPaneViewBackend(this)
//    )
//
//    addContentPaneBuilder(UfdWsContext.WSIT_UFD_FRAGMENT) { item ->
//
//        PaneDef(
//            UUID(),
//            workspace = this,
//            "item.name",
//            Graphics.cards,
//            PanePosition.Center,
//            UfdWsContext.CONTENT_PANE_KEY,
//            viewBackend = UfdContentViewBackend(this)
//        )
//
//    }
//
//    addItemConfig(UfdWsContext.WSIT_UFD_FRAGMENT, Graphics.cards)

}