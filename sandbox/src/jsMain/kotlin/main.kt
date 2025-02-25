/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.backend.backend
import `fun`.adaptive.cookbook.cookbookCommon
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.api.GroveRuntimeFragmentFactory
import `fun`.adaptive.grove.groveCommon
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.sandbox.commonMainStringsStringStore0
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.builtin.menu
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.WorkspacePane
import `fun`.adaptive.ui.workspace.WorkspacePanePosition
import `fun`.adaptive.ui.workspace.wsFull
import `fun`.adaptive.utility.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {

    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()
        groveCommon()
        groveRuntimeCommon()

        commonMainStringsStringStore0.load()

        browser(
            CanvasFragmentFactory,
            SvgFragmentFactory,
            GroveRuntimeFragmentFactory,
            backend = backend { }
        ) { adapter ->

            adapter.cookbookCommon()
            adapter.groveCommon()

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
                fontWeight = 300
            }

            val workspace = buildWorkspace()

            localContext(workspace) {
                wsFull(workspace)
            }
        }
    }
}


fun buildWorkspace(): Workspace {

    val workspace = Workspace()

    workspace.groveCommon()
    workspace.cookbookCommon()

    workspace.updateSplits()

    return workspace
}

fun Workspace.siteCommon() {
    panes += WorkspacePane(
        UUID(),
        "Home",
        Graphics.menu,
        WorkspacePanePosition.Center,
        "site:home"
    )
}

@Adaptive
fun project(): AdaptiveFragment {
    return fragment()
}

@Adaptive
fun site(): AdaptiveFragment {
    return site()
}