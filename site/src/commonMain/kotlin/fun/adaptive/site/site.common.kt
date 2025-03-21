package `fun`.adaptive.site/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.cookbook.cookbookCommon
import `fun`.adaptive.cookbook.eco
import `fun`.adaptive.grove.groveCommon
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.app.appHomePane
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.model.WsPaneSingularity
import `fun`.adaptive.utility.UUID

suspend fun siteCommon() {
    uiCommon()
    groveRuntimeCommon()
    cookbookCommon()
    groveCommon()
}

fun AbstractAuiAdapter<*, *>.siteCommon() {

    uiCommon()
    groveRuntimeCommon()
    cookbookCommon()
    groveCommon()

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
    appHomePane()

}