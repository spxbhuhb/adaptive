/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.backend.backend
import `fun`.adaptive.cookbook.cookbookCommon
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.api.GroveRuntimeFragmentFactory
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.sandbox.commonMainStringsStringStore0
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.wsFull
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
            backend = backend { }
        ) { adapter ->

            adapter.cookbookCommon()

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

    workspace.cookbookCommon()

    workspace.updateSplits()

    return workspace
}