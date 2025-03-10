package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.grove.resources.cards
import `fun`.adaptive.grove.resources.components
import `fun`.adaptive.grove.resources.data_table
import `fun`.adaptive.grove.resources.instructions
import `fun`.adaptive.grove.resources.palette
import `fun`.adaptive.grove.resources.state
import `fun`.adaptive.grove.resources.stroke_partial
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.builtin.menu
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

const val ufdPalettePaneKey = "grove:ufd:palette"
const val ufdComponentsPaneKey = "grove:ufd:components"
const val ufdInstructionsPaneKey = "grove:ufd:instructions"
const val ufdStatePaneKey = "grove:ufd:state"

const val ufdCenterPaneKey = "grove:ufd:center"

fun AdaptiveAdapter.groveUfdCommon() {
    fragmentFactory += arrayOf(UfdPaneFactory)
}

fun Workspace.groveUfdCommon() {
    contexts += UfdContext(this)

    toolPanes += WsPane(
        UUID(),
        Strings.palette,
        Graphics.palette,
        WsPanePosition.LeftTop,
        ufdPalettePaneKey,
        model = Unit
    )

    toolPanes += WsPane(
        UUID(),
        Strings.components,
        Graphics.cards,
        WsPanePosition.LeftMiddle,
        ufdComponentsPaneKey,
        model = Unit
    )

    toolPanes += WsPane(
        UUID(),
        Strings.instructions,
        Graphics.stroke_partial,
        WsPanePosition.RightTop,
        ufdInstructionsPaneKey,
        model = Unit
    )

    toolPanes += WsPane(
        UUID(),
        Strings.state,
        Graphics.data_table,
        WsPanePosition.RightTop,
        ufdStatePaneKey,
        model = Unit
    )

    toolPanes += WsPane(
        UUID(),
        "",
        Graphics.menu,
        WsPanePosition.Center,
        ufdCenterPaneKey,
        model = Unit
    )

}