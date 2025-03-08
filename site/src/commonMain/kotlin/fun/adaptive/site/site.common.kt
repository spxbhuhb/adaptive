package `fun`.adaptive.site/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.cookbook.cookbookCommon
import `fun`.adaptive.cookbook.eco
import `fun`.adaptive.grove.groveCommon
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.ui.workspace.model.Workspace
import `fun`.adaptive.ui.workspace.model.WorkspacePane
import `fun`.adaptive.ui.workspace.model.WorkspacePanePosition
import `fun`.adaptive.utility.UUID

const val siteHomeKey = "site:home"

suspend fun siteCommon() {
    uiCommon()
    cookbookCommon()
    groveCommon()
    groveRuntimeCommon()
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

    toolPanes += WorkspacePane(
        UUID(),
        "Home",
        Graphics.eco,
        WorkspacePanePosition.Center,
        siteHomeKey,
        direct = true
    )
}