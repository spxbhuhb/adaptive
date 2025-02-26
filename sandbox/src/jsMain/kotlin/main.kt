/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.auto.api.autoCollection
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.cookbook.cookbookCommon
import `fun`.adaptive.cookbook.eco
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveAdapter
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
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.builtin.menu
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.snackbar.SnackbarManager
import `fun`.adaptive.ui.snackbar.activeSnackStore
import `fun`.adaptive.ui.snackbar.snackList
import `fun`.adaptive.ui.snackbar.snackbarTheme
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
        cookbookCommon()
        groveCommon()
        groveRuntimeCommon()

        commonMainStringsStringStore0.load()

        val localBackend = backend {
            auto()
            worker { SnackbarManager() }
        }

        browser(
            CanvasFragmentFactory,
            SvgFragmentFactory,
            GroveRuntimeFragmentFactory,
            backend = localBackend
        ) { adapter ->

            adapter.cookbookCommon()
            adapter.groveCommon()
            adapter.siteCommon()

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
                fontWeight = 300
            }

            val workspace = buildWorkspace()

            localContext(workspace) {
                wsFull(workspace)
            }

            snackContainer()
        }
    }
}

@Adaptive
fun snackContainer() {
    val metrics = mediaMetrics()

    val activeSnacks = autoCollection(activeSnackStore) ?: emptyList()

    val snackbarPosition = position(
        metrics.viewHeight.dp - (snackbarTheme.snackHeight + snackbarTheme.snackGap) * activeSnacks.size,
        metrics.viewWidth.dp - snackbarTheme.snackWidth - 16.dp
    )

    box {
        noPointerEvents .. maxSize .. zIndex { 3000 }
        snackList(activeSnacks) .. noPointerEvents .. snackbarPosition
    }
}

fun buildWorkspace(): Workspace {

    val workspace = Workspace()

    workspace.groveCommon()
    workspace.cookbookCommon()
    workspace.siteCommon()

    workspace.updateSplits()

    return workspace
}

fun AdaptiveAdapter.siteCommon() {
    fragmentFactory += arrayOf(SiteFragmentFactory)
}

fun Workspace.siteCommon() {
    panes += WorkspacePane(
        UUID(),
        "Home",
        Graphics.eco,
        WorkspacePanePosition.Center,
        "site:home",
        direct = true
    )
}

@Adaptive
fun siteHome(): AdaptiveFragment {
    text("site")
    return fragment()
}