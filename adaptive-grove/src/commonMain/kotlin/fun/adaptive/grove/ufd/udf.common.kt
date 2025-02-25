package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.grove.resources.cards
import `fun`.adaptive.grove.resources.palette
import `fun`.adaptive.grove.resources.stroke_partial
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.builtin.menu
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.WorkspacePane
import `fun`.adaptive.ui.workspace.WorkspacePanePosition
import `fun`.adaptive.utility.UUID

const val ufdPalettePaneKey = "grove:ufd:palette"
const val ufdComponentsPaneKey = "grove:ufd:components"
const val ufdInstructionsPaneKey = "grove:ufd:instructions"
const val ufdCenterPaneKey = "grove:ufd:center"

fun AdaptiveAdapter.groveUfdCommon() {
    fragmentFactory += arrayOf(UfdPaneFactory)
}

fun Workspace.groveUfdCommon() {
    contexts += UfdContext(this)

    panes += WorkspacePane(
        UUID(),
        "Palette",
        Graphics.palette,
        WorkspacePanePosition.LeftTop,
        "grove:ufd:palette"
    )

    panes += WorkspacePane(
        UUID(),
        "Components",
        Graphics.cards,
        WorkspacePanePosition.LeftMiddle,
        "grove:ufd:components",
    )

    panes += WorkspacePane(
        UUID(),
        "Instructions",
        Graphics.stroke_partial,
        WorkspacePanePosition.RightTop,
        "grove:ufd:instructions",
    )

    panes += WorkspacePane(
        UUID(),
        "Sheet",
        Graphics.menu,
        WorkspacePanePosition.Center,
        "grove:ufd:center"
    )

}