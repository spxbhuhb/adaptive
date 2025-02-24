/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.sandbox.commonMainStringsStringStore0
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.builtin.account_box
import `fun`.adaptive.ui.builtin.menu
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.ui.workspace.*
import `fun`.adaptive.ui.workspace.WorkspaceTheme.Companion.workspaceTheme
import `fun`.adaptive.utility.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {

    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()

        commonMainStringsStringStore0.load()

        browser(CanvasFragmentFactory, SvgFragmentFactory, backend = backend { }) { adapter ->

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
                fontWeight = 300
            }

            val workspace = Workspace()

            initPanes(workspace)

            grid {
                maxSize .. colTemplate(workspaceTheme.width, 1.fr, workspaceTheme.width)

                wsPaneIcons(left = true, workspace)

                wsMain(workspace)

                wsPaneIcons(left = false, workspace)
            }
        }
    }
}

fun initPanes(workspace: Workspace) {
    workspace.panes.addAll(
        listOf(
            WorkspacePane(
                UUID(),
                "Palette",
                Graphics.menu,
                WorkspacePanePosition.LeftTop,
                ::palette,
                "⌘ P"
            ),
            WorkspacePane(
                UUID(),
                "Components",
                Graphics.account_box,
                WorkspacePanePosition.LeftTop,
                ::emptyPanel,
                "⌘ P"
            ),
            WorkspacePane(
                UUID(),
                "Palette",
                Graphics.menu,
                WorkspacePanePosition.LeftMiddle,
                ::emptyPanel
            ),
            WorkspacePane(
                UUID(),
                "Components",
                Graphics.account_box,
                WorkspacePanePosition.LeftMiddle,
                ::emptyPanel,
                "⌘ P"
            ),
            WorkspacePane(
                UUID(),
                "Palette",
                Graphics.menu,
                WorkspacePanePosition.LeftBottom,
                ::emptyPanel,
                "⌘ P"
            ),
            WorkspacePane(
                UUID(),
                "Components",
                Graphics.account_box,
                WorkspacePanePosition.LeftBottom,
                ::emptyPanel,
                "⌘ P"
            ),
            WorkspacePane(
                UUID(),
                "Center",
                Graphics.menu,
                WorkspacePanePosition.Center,
                ::center
            ),
        )
    )

    workspace.center.value = workspace.panes.first { it.position == WorkspacePanePosition.Center }.uuid
}


@Adaptive
fun emptyPanel(): AdaptiveFragment {
    box {
        padding { 16.dp }
        text("todo")
    }
    return fragment()
}

@Adaptive
fun palette(): AdaptiveFragment {
    box {
        padding { 16.dp }
        text("palette")
    }
    return fragment()
}

@Adaptive
fun center(): AdaptiveFragment {
    box {
        padding { 16.dp }
        text("center")
    }
    return fragment()
}