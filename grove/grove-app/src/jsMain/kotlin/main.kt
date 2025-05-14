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
import `fun`.adaptive.grove.sheet.SheetFragmentFactory
import `fun`.adaptive.grove.ufd.UfdPaneFactory
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.wsFull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {
    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()
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

            box {
                maxSize

                localContext(workspace) {
                    wsFull(workspace)
                }
            }
        }
    }
}

private fun buildWorkspace(): MultiPaneWorkspace {
    TODO()
//    val workspace = Workspace()
//
//    with(workspace) {
//        groveCommon()
//
//        leftTop.value = toolPanes.first { it.key == ufdPalettePaneKey }.uuid
//        leftMiddle.value = toolPanes.first { it.position == WsPanePosition.LeftMiddle }.uuid
//        //rightTop.value = panes.first { it.position == WorkspacePanePosition.RightTop }.uuid
//        //center.value = panes.first { it.position == WorkspacePanePosition.Center }.uuid
//
//        updateSplits()
//    }
//
//    return workspace
}