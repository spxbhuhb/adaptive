package `fun`.adaptive.site/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.cookbook.cookbookCommon
import `fun`.adaptive.cookbook.eco
import `fun`.adaptive.grove.groveCommon
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.snackbar.SnackbarManager
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.WorkspacePane
import `fun`.adaptive.ui.workspace.WorkspacePanePosition
import `fun`.adaptive.utility.UUID

suspend fun siteCommon() {
    uiCommon()
    cookbookCommon()
    groveCommon()
    groveRuntimeCommon()
}

fun siteClientBackend() =
    backend {
        auto()
        worker { SnackbarManager() }
    }

fun AbstractAuiAdapter<*, *>.siteCommon() {

    cookbookCommon()
    groveCommon()
    groveRuntimeCommon()

    fragmentFactory += arrayOf(SiteFragmentFactory)

    with(defaultTextRenderData) {
        fontName = "Open Sans"
        fontSize = 16.sp
        fontWeight = 300
    }
}

fun Workspace.siteCommon() {

    cookbookCommon()
    groveCommon()

    panes += WorkspacePane(
        UUID(),
        "Home",
        Graphics.eco,
        WorkspacePanePosition.Center,
        "site:home",
        direct = true
    )
}