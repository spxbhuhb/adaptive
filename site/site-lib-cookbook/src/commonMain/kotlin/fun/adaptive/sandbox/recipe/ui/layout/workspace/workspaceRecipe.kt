package `fun`.adaptive.sandbox.recipe.ui.layout.workspace

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.workspace.*
import `fun`.adaptive.ui.workspace.WorkspaceTheme.Companion.DEFAULT
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneViewBackend
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

class Context {
    val someData = "Some data from the context"
}

@Adaptive
fun workspaceRecipe(): AdaptiveFragment {

    val workspace = MultiPaneWorkspace(adapter().backend)
    workspace.contexts += Context()
    initPanes(workspace)

    box {
        maxSize .. backgrounds.friendlyOpaque .. padding { 16.dp }

        grid {
            maxSize .. colTemplate(DEFAULT.width, 1.fr, DEFAULT.width)
            borders.outline .. backgrounds.surface

            localContext(workspace) {
                wsSideBarIcons(left = true, workspace)

                wsMain(workspace)

                wsSideBarIcons(left = false, workspace)
            }
        }
    }

    return fragment()
}

object WorkspaceRecipePaneFragmentFactory : FoundationFragmentFactory() {
    init {
        add("cookbook:support:righttop", ::rightTop)
        add("cookbook:support:rightmiddle", ::rightMiddle)
        add("cookbook:support:bottomright", ::bottomRight)
        add("cookbook:support:lefttop", ::leftTop)
        add("cookbook:support:leftmiddle1", ::leftMiddle1)
        add("cookbook:support:leftmiddle2", ::leftMiddle2)
        add("cookbook:support:leftmiddle3", ::leftMiddle3)
        add("cookbook:support:bottomleft", ::bottomLeft)
        add("cookbook:support:center", ::center)
    }
}

fun initPanes(workspace: MultiPaneWorkspace) {
    workspace.toolPanes.addAll(
        listOf(
            WsPane(
                UUID(),
                workspace = workspace,
                "Left Top",
                Graphics.menu,
                WsPanePosition.LeftTop,
                "cookbook:support:lefttop",
                tooltip = "⌘ P",
                viewBackend = WsUnitPaneViewBackend(workspace)
            ),
            WsPane(
                UUID(),
                workspace = workspace,
                "Left Middle - 1",
                Graphics.account_box,
                WsPanePosition.LeftMiddle,
                "cookbook:support:leftmiddle1",
                viewBackend = WsUnitPaneViewBackend(workspace)
            ),
            WsPane(
                UUID(),
                workspace = workspace,
                "Left Middle - 2",
                Graphics.settings,
                WsPanePosition.LeftMiddle,
                "cookbook:support:leftmiddle2",
                viewBackend = WsUnitPaneViewBackend(workspace)
            ),
            WsPane(
                UUID(),
                workspace = workspace,
                "Left Middle - 3",
                Graphics.more_vert,
                WsPanePosition.LeftMiddle,
                "cookbook:support:leftmiddle3",
                viewBackend = WsUnitPaneViewBackend(workspace)
            ),
            WsPane(
                UUID(),
                workspace = workspace,
                "Left Bottom",
                Graphics.power_settings_new,
                WsPanePosition.LeftBottom,
                "cookbook:support:bottomleft",
                tooltip = "⌘ P",
                viewBackend = WsUnitPaneViewBackend(workspace)
            ),
            WsPane(
                UUID(),
                workspace = workspace,
                "Right Top",
                Graphics.account_box,
                WsPanePosition.RightTop,
                "cookbook:support:righttop",
                tooltip = "⌘ P",
                viewBackend = WsUnitPaneViewBackend(workspace)
            ),
            WsPane(
                UUID(),
                workspace = workspace,
                "Right Middle",
                Graphics.account_box,
                WsPanePosition.RightMiddle,
                "cookbook:support:rightmiddle",
                tooltip = "⌘ P",
                viewBackend = WsUnitPaneViewBackend(workspace)
            ),
            WsPane(
                UUID(),
                workspace = workspace,
                "Right Bottom",
                Graphics.account_circle,
                WsPanePosition.RightBottom,
                "cookbook:support:bottomright",
                tooltip = "⌘ P",
                viewBackend = WsUnitPaneViewBackend(workspace)
            ),
            WsPane(
                UUID(),
                workspace = workspace,
                "Center",
                Graphics.menu,
                WsPanePosition.Center,
                "cookbook:support:center",
                viewBackend = WsUnitPaneViewBackend(workspace)
            ),
        )
    )

    workspace.center.value = workspace.toolPanes.first { it.position == WsPanePosition.Center }.uuid
}


@Adaptive
fun rightTop(): AdaptiveFragment {
    box {
        padding { 16.dp }
        text("right top")
    }
    return fragment()
}

@Adaptive
fun rightMiddle(): AdaptiveFragment {
    box {
        padding { 16.dp }
        text("right middle")
    }
    return fragment()
}

@Adaptive
fun bottomRight(): AdaptiveFragment {
    box {
        padding { 16.dp }
        text("bottom right")
    }
    return fragment()
}

@Adaptive
fun leftTop(): AdaptiveFragment {
    box {
        padding { 16.dp }
        text("left top")
    }
    return fragment()
}

@Adaptive
fun leftMiddle1(): AdaptiveFragment {
    box {
        padding { 16.dp }
        text("left middle - 1")
    }
    return fragment()
}

@Adaptive
fun leftMiddle2(): AdaptiveFragment {
    box {
        padding { 16.dp }
        text("left middle - 2")
    }
    return fragment()
}

@Adaptive
fun leftMiddle3(): AdaptiveFragment {
    box {
        padding { 16.dp }
        text("left middle - 3")
    }
    return fragment()
}

@Adaptive
fun bottomLeft(): AdaptiveFragment {
    box {
        padding { 16.dp }
        text("bottom left")
    }
    return fragment()
}

@Adaptive
fun center(): AdaptiveFragment {
    val context = fragment().wsContext<Context>()

    box {
        padding { 16.dp }
        column {
            text("center")
            text(context.someData)
        }
    }
    return fragment()
}