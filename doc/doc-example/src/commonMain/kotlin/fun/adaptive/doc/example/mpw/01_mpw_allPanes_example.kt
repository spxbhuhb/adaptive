package `fun`.adaptive.doc.example.mpw

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.runtime.NoBackendWorkspace
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.mpw.MultiPaneTheme.Companion.DEFAULT
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.UnitPaneViewBackend
import `fun`.adaptive.ui.mpw.fragments.multiPaneWorkspaceMain
import `fun`.adaptive.ui.mpw.fragments.multiPaneWorkspaceSideBarIcons
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.utility.UUID

/**
 * # All panes
 *
 * This example shows a manually built workspace with all the panes. You typically
 * don't create the workspace manually but use the application framework from
 * `lib-app`.
 */
@Adaptive
fun mpwAllPanesExample(): AdaptiveFragment {

    val workspace = MultiPaneWorkspace(
        adapter().backend,
        NoBackendWorkspace(),
        toolSizeDefault = 100.dp
    )

    initPanes(workspace)

    box {
        maxWidth .. height { 600.dp } .. backgrounds.friendlyOpaque .. padding { 16.dp }

        grid {
            maxSize .. colTemplate(DEFAULT.width, 1.fr, DEFAULT.width)
            borders.outline .. backgrounds.surface

            localContext(workspace) {
                multiPaneWorkspaceSideBarIcons(left = true, workspace)

                multiPaneWorkspaceMain(workspace)

                multiPaneWorkspaceSideBarIcons(left = false, workspace)
            }
        }
    }

    return fragment()
}

object WorkspaceRecipePaneFragmentFactory : FoundationFragmentFactory() {
    init {
        add("example:support:righttop", ::rightTop)
        add("example:support:rightmiddle", ::rightMiddle)
        add("example:support:bottomright", ::bottomRight)
        add("example:support:lefttop", ::leftTop)
        add("example:support:leftmiddle1", ::leftMiddle1)
        add("example:support:leftmiddle2", ::leftMiddle2)
        add("example:support:leftmiddle3", ::leftMiddle3)
        add("example:support:bottomleft", ::bottomLeft)
        add("example:support:center", ::center)
    }
}

fun initPanes(workspace: MultiPaneWorkspace) {
    workspace.toolPanes.addAll(
        listOf(
            UnitPaneViewBackend(
                workspace,
                PaneDef(
                    UUID(),
                    "Left Top",
                    Graphics.menu,
                    PanePosition.LeftTop,
                    "example:support:lefttop",
                    tooltip = "⌘ P",
                )
            ),
            UnitPaneViewBackend(
                workspace,
                PaneDef(
                    UUID(),
                    "Left Middle - 1",
                    Graphics.account_box,
                    PanePosition.LeftMiddle,
                    "example:support:leftmiddle1",
                )
            ),
            UnitPaneViewBackend(
                workspace,
                PaneDef(
                    UUID(),
                    "Left Middle - 2",
                    Graphics.settings,
                    PanePosition.LeftMiddle,
                    "example:support:leftmiddle2",
                )
            ),
            UnitPaneViewBackend(
                workspace,
                PaneDef(
                    UUID(),
                    "Left Middle - 3",
                    Graphics.more_vert,
                    PanePosition.LeftMiddle,
                    "example:support:leftmiddle3",
                )
            ),
            UnitPaneViewBackend(
                workspace,
                PaneDef(
                    UUID(),
                    "Left Bottom",
                    Graphics.power_settings_new,
                    PanePosition.LeftBottom,
                    "example:support:bottomleft",
                    tooltip = "⌘ P",
                )
            ),
            UnitPaneViewBackend(
                workspace,
                PaneDef(
                    UUID(),
                    "Right Top",
                    Graphics.account_box,
                    PanePosition.RightTop,
                    "example:support:righttop",
                    tooltip = "⌘ P",
                )
            ),
            UnitPaneViewBackend(
                workspace,
                PaneDef(
                    UUID(),
                    "Right Middle",
                    Graphics.account_box,
                    PanePosition.RightMiddle,
                    "example:support:rightmiddle",
                    tooltip = "⌘ P",
                )
            ),
            UnitPaneViewBackend(
                workspace,
                PaneDef(
                    UUID(),
                    "Right Bottom",
                    Graphics.account_circle,
                    PanePosition.RightBottom,
                    "example:support:bottomright",
                    tooltip = "⌘ P",
                )
            ),
            UnitPaneViewBackend(
                workspace,
                PaneDef(
                    UUID(),
                    "Center",
                    Graphics.menu,
                    PanePosition.Center,
                    "example:support:center",
                )
            ),
        )
    )

    workspace.leftTop.value = workspace.toolPanes.first { it.paneDef.position == PanePosition.LeftTop }.uuid
    workspace.leftMiddle.value = workspace.toolPanes.first { it.paneDef.position == PanePosition.LeftMiddle }.uuid
    workspace.leftBottom.value = workspace.toolPanes.first { it.paneDef.position == PanePosition.LeftBottom }.uuid

    workspace.rightTop.value = workspace.toolPanes.first { it.paneDef.position == PanePosition.RightTop }.uuid
    workspace.rightMiddle.value = workspace.toolPanes.first { it.paneDef.position == PanePosition.RightMiddle }.uuid
    workspace.rightBottom.value = workspace.toolPanes.first { it.paneDef.position == PanePosition.RightBottom }.uuid

    workspace.updateSplits()
}


@Adaptive
fun rightTop(): AdaptiveFragment {
    box {
        maxWidth .. scroll .. padding { 16.dp }
        text("right top")
    }
    return fragment()
}

@Adaptive
fun rightMiddle(): AdaptiveFragment {
    box {
        maxWidth .. scroll .. padding { 16.dp }
        text("right middle")
    }
    return fragment()
}

@Adaptive
fun bottomRight(): AdaptiveFragment {
    box {
        maxWidth .. scroll .. padding { 16.dp }
        text("bottom right")
    }
    return fragment()
}

@Adaptive
fun leftTop(): AdaptiveFragment {
    box {
        maxWidth .. scroll .. padding { 16.dp }
        text("left top")
    }
    return fragment()
}

@Adaptive
fun leftMiddle1(): AdaptiveFragment {
    box {
        maxWidth .. scroll .. padding { 16.dp }
        text("left middle - 1")
    }
    return fragment()
}

@Adaptive
fun leftMiddle2(): AdaptiveFragment {
    box {
        maxWidth .. scroll .. padding { 16.dp }
        text("left middle - 2")
    }
    return fragment()
}

@Adaptive
fun leftMiddle3(): AdaptiveFragment {
    box {
        maxWidth .. scroll .. padding { 16.dp }
        text("left middle - 3")
    }
    return fragment()
}

@Adaptive
fun bottomLeft(): AdaptiveFragment {
    box {
        maxWidth .. scroll .. padding { 16.dp }
        text("bottom left")
    }
    return fragment()
}

@Adaptive
fun center(): AdaptiveFragment {
    box {
        maxWidth .. scroll .. padding { 16.dp }
        column {
            text("center")
        }
    }
    return fragment()
}