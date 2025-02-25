/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.api.GroveRuntimeFragmentFactory
import `fun`.adaptive.grove.fragment.GroveFragmentFactory
import `fun`.adaptive.grove.groveCommon
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.grove.sheet.SheetFragmentFactory
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.ufd.UfdContext
import `fun`.adaptive.grove.ufd.UfdPaneFactory
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.WorkspacePanePosition
import `fun`.adaptive.ui.workspace.wsFull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {
    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()
        groveRuntimeCommon()
        groveCommon()

        browser(backend = backend { }) { adapter ->

            adapter.groveCommon()

            adapter.fragmentFactory += arrayOf(
                CanvasFragmentFactory,
                SvgFragmentFactory,
                GroveFragmentFactory,
                GroveRuntimeFragmentFactory,
                SheetFragmentFactory,
                UfdPaneFactory
            )

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
                fontWeight = 300
            }

            val workspace = buildWorkspace()

            val controller = SheetViewController(false, true, true)
            controller.extensions += UfdContext(workspace)

            box {
                maxSize

                localContext(workspace) {
                    localContext(controller) {
                        wsFull(workspace)
                    }
                }
            }
        }
    }
}

private fun buildWorkspace(): Workspace {
    val workspace = Workspace()

    with(workspace) {
        groveCommon()
        leftTop.value = workspace.panes.first { it.position == WorkspacePanePosition.LeftTop }.uuid
        leftMiddle.value = workspace.panes.first { it.position == WorkspacePanePosition.LeftMiddle }.uuid
        rightTop.value = workspace.panes.first { it.position == WorkspacePanePosition.RightTop }.uuid
        center.value = workspace.panes.first { it.position == WorkspacePanePosition.Center }.uuid
        updateSplits()
    }

    return workspace
}