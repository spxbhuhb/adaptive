/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.api.GroveRuntimeFragmentFactory
import `fun`.adaptive.grove.groveRuntimeCommon
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
        groveRuntimeCommon()

        commonMainStringsStringStore0.load()

        browser(
            CanvasFragmentFactory,
            SvgFragmentFactory,
            GroveRuntimeFragmentFactory,
            PaneFragmentFactory,
            backend = backend { }
        ) { adapter ->

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

object PaneFragmentFactory : FoundationFragmentFactory() {
    init {
        add("grove:palette", ::palette)
        add("grove:emptypanel", ::emptyPanel)
        add("grove:center", ::center)
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
                "grove:palette",
                "⌘ P"
            ),
            WorkspacePane(
                UUID(),
                "Components",
                Graphics.account_box,
                WorkspacePanePosition.LeftTop,
                "grove:emptypanel",
                "⌘ P"
            ),
            WorkspacePane(
                UUID(),
                "Palette",
                Graphics.menu,
                WorkspacePanePosition.LeftMiddle,
                "grove:emptypanel",
            ),
            WorkspacePane(
                UUID(),
                "Components",
                Graphics.account_box,
                WorkspacePanePosition.LeftMiddle,
                "grove:emptypanel",
                "⌘ P"
            ),
            WorkspacePane(
                UUID(),
                "Palette",
                Graphics.menu,
                WorkspacePanePosition.LeftBottom,
                "grove:emptypanel",
                "⌘ P"
            ),
            WorkspacePane(
                UUID(),
                "Components",
                Graphics.account_box,
                WorkspacePanePosition.LeftBottom,
                "grove:emptypanel",
                "⌘ P"
            ),
            WorkspacePane(
                UUID(),
                "Center",
                Graphics.menu,
                WorkspacePanePosition.Center,
                "grove:center"
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