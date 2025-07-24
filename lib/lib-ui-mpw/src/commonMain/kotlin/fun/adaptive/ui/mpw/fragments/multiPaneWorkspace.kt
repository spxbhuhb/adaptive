package `fun`.adaptive.ui.mpw.fragments

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.splitpane.horizontalSplitDivider
import `fun`.adaptive.ui.splitpane.verticalSplitDivider
import `fun`.adaptive.ui.mpw.AbstractSideBarAction
import `fun`.adaptive.ui.mpw.MultiPaneTheme.Companion.DEFAULT
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.utility.UUID

@Adaptive
fun multiPaneWorkspace(workspace: MultiPaneWorkspace) : AdaptiveFragment {

    workspace.workspaceFragment = fragment()

    val metrics = mediaMetrics()

    when {
        metrics.isLarge -> multiPaneWorkspaceLarge(workspace)
        else -> multiPaneWorkspaceSmall(workspace)
    }

    return fragment()
}