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
import `fun`.adaptive.ui.workspace.model.Workspace
import `fun`.adaptive.ui.workspace.model.WorkspacePane
import `fun`.adaptive.ui.workspace.model.WorkspacePanePosition
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

    toolPanes += WorkspacePane(
        UUID(),
        Strings.palette,
        Graphics.palette,
        WorkspacePanePosition.LeftTop,
        ufdPalettePaneKey
    )

    toolPanes += WorkspacePane(
        UUID(),
        Strings.components,
        Graphics.cards,
        WorkspacePanePosition.LeftMiddle,
        ufdComponentsPaneKey,
    )

    toolPanes += WorkspacePane(
        UUID(),
        Strings.instructions,
        Graphics.stroke_partial,
        WorkspacePanePosition.RightTop,
        ufdInstructionsPaneKey
    )

    toolPanes += WorkspacePane(
        UUID(),
        Strings.state,
        Graphics.data_table,
        WorkspacePanePosition.RightTop,
        ufdStatePaneKey
    )

    toolPanes += WorkspacePane(
        UUID(),
        "",
        Graphics.menu,
        WorkspacePanePosition.Center,
        ufdCenterPaneKey
    )

}