package `fun`.adaptive.cookbook.recipe.ui.workspace

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.workspace.*
import `fun`.adaptive.ui.workspace.WorkspaceTheme.Companion.workspaceTheme
import `fun`.adaptive.utility.UUID

class Context {
    val someData = "Some data from the context"
}

@Adaptive
fun workspaceRecipe(): AdaptiveFragment {

    val workspace = Workspace()
    initPanes(workspace)

    box {
        maxSize .. backgrounds.friendlyOpaque .. padding { 16.dp }

        grid {
            maxSize .. colTemplate(workspaceTheme.width, 1.fr, workspaceTheme.width)
            borders.outline .. backgrounds.surface

            localContext(Context()) {
                wsPaneIcons(left = true, workspace)

                wsMain(workspace)

                wsPaneIcons(left = false, workspace)
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

fun initPanes(workspace: Workspace) {
    workspace.panes.addAll(
        listOf(
            WorkspacePane(
                UUID(),
                "Left Top",
                Graphics.menu,
                WorkspacePanePosition.LeftTop,
                "cookbook:support:lefttop",
                "⌘ P"
            ),
            WorkspacePane(
                UUID(),
                "Left Middle - 1",
                Graphics.account_box,
                WorkspacePanePosition.LeftMiddle,
                "cookbook:support:leftmiddle1",
            ),
            WorkspacePane(
                UUID(),
                "Left Middle - 2",
                Graphics.settings,
                WorkspacePanePosition.LeftMiddle,
                "cookbook:support:leftmiddle2",
            ),
            WorkspacePane(
                UUID(),
                "Left Middle - 3",
                Graphics.more_vert,
                WorkspacePanePosition.LeftMiddle,
                "cookbook:support:leftmiddle3",
            ),
            WorkspacePane(
                UUID(),
                "Left Bottom",
                Graphics.power_settings_new,
                WorkspacePanePosition.LeftBottom,
                "cookbook:support:bottomleft",
                "⌘ P"
            ),
            WorkspacePane(
                UUID(),
                "Right Top",
                Graphics.account_box,
                WorkspacePanePosition.RightTop,
                "cookbook:support:righttop",
                "⌘ P"
            ),
            WorkspacePane(
                UUID(),
                "Right Middle",
                Graphics.account_box,
                WorkspacePanePosition.RightMiddle,
                "cookbook:support:rightmiddle",
                "⌘ P"
            ),
            WorkspacePane(
                UUID(),
                "Right Bottom",
                Graphics.account_circle,
                WorkspacePanePosition.RightBottom,
                "cookbook:support:bottomright",
                "⌘ P"
            ),
            WorkspacePane(
                UUID(),
                "Center",
                Graphics.menu,
                WorkspacePanePosition.Center,
                "cookbook:support:center"
            ),
        )
    )

    workspace.center.value = workspace.panes.first { it.position == WorkspacePanePosition.Center }.uuid
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
    val context = fragment().firstContext<Context>()

    box {
        padding { 16.dp }
        column {
            text("center")
            text(context.someData)
        }
    }
    return fragment()
}