/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.auth.api.SessionApi
import `fun`.adaptive.auth.authCommon
import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.backend.backend
import `fun`.adaptive.cookbook.Res
import `fun`.adaptive.cookbook.auth.authRecipe
import `fun`.adaptive.cookbook.cookbookCommon
import `fun`.adaptive.cookbook.eco
import `fun`.adaptive.cookbook.grid_view
import `fun`.adaptive.cookbook.shared.textSmall
import `fun`.adaptive.cookbook.ui.dialog.dialogRecipe
import `fun`.adaptive.cookbook.ui.editor.editorRecipe
import `fun`.adaptive.cookbook.ui.file.fileRecipe
import `fun`.adaptive.cookbook.ui.form.formRecipe
import `fun`.adaptive.cookbook.ui.grid.gridRecipe
import `fun`.adaptive.cookbook.ui.select.selectRecipe
import `fun`.adaptive.cookbook.ui.sidebar.sideBarRecipe
import `fun`.adaptive.cookbook.ui.svg.svgRecipe
import `fun`.adaptive.cookbook.ui.text.textRecipe
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.boldFont
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.form.FormFragmentFactory
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.sidebar.SidebarItem
import `fun`.adaptive.ui.navigation.sidebar.fullSidebar
import `fun`.adaptive.ui.navigation.sidebar.theme.fullSidebarTheme
import `fun`.adaptive.ui.platform.withJsResources
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {

    CoroutineScope(Dispatchers.Default).launch {

        clientId()

        authCommon()
        cookbookCommon()

        withJsResources()

        val transport = webSocketTransport(window.location.origin)
        val localBackend = backend(transport) { auto() }

//        val localBackend = backend { auto() }

        browser(CanvasFragmentFactory, SvgFragmentFactory, FormFragmentFactory, backend = localBackend) { adapter ->

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
                fontWeight = 300
            }

            grid {
                maxSize .. colTemplate(fullSidebarTheme.width, 1.fr)
                mainMenu()
                mainContent()
            }

//            iotMain()
//              box {
//                  hoverMain()
//              }

//            boundInputRecipe()

//            buttonRecipe()
//            gridAlignRecipe()
//            quickFilterRecipe()

//            projectWizardMain()
        }
    }
}

private val appNavState = autoInstance(Routes.file)

private object Routes {
    val auth = NavState("Auth")
    val dialog = NavState("Dialog")
    val editor = NavState("Editor")
    val empty = NavState("Empty")
    val file = NavState("File")
    val form = NavState("Form")
    val grid = NavState("Grid")
    val select = NavState("Select")
    val sidebar = NavState("SideBar")
    val svg = NavState("SVG")
    val text = NavState("Text")
}

private val items = listOf(
    SidebarItem(0, Res.drawable.grid_view, "Auth", Routes.auth),
    SidebarItem(0, Res.drawable.grid_view, "Dialog", Routes.dialog),
    SidebarItem(0, Res.drawable.grid_view, "Editor", Routes.editor),
    SidebarItem(0, Res.drawable.grid_view, "File", Routes.file),
    SidebarItem(0, Res.drawable.grid_view, "Form", Routes.form),
    SidebarItem(0, Res.drawable.grid_view, "Grid", Routes.grid),
    SidebarItem(0, Res.drawable.grid_view, "Select", Routes.select),
    SidebarItem(0, Res.drawable.grid_view, "SideBar", Routes.sidebar),
    SidebarItem(0, Res.drawable.grid_view, "SVG", Routes.svg),
    SidebarItem(0, Res.drawable.grid_view, "Text", Routes.text)
)

@Adaptive
fun mainMenu() {
    val session = fetch { getService<SessionApi>(adapter().transport).getSession() }

    grid {
        rowTemplate(168.dp, 1.fr, 60.dp) .. maxSize
        box {
            svg(Res.drawable.eco) .. position(60.dp, 32.dp)
            text("Adaptive") .. boldFont .. fontSize(28.sp) .. position(60.dp, 88.dp)
        }
        fullSidebar(items, appNavState)
        column {
            maxWidth .. padding { 24.dp } .. gap { 12.dp }
            text("Session: ${session?.id}")
//            text(AppReleaseInfo.version) .. textColors.onSurfaceVariant .. textSmall
//            text(AppReleaseInfo.commit) .. textColors.onSurfaceVariant .. textSmall
        }
    }
}

@Adaptive
fun mainContent() {

    val navState = autoInstance(appNavState)

    box {
        maxSize .. padding { 16.dp } .. backgroundColor(0xFAFAFA)

        when (navState) {
            in Routes.auth -> authRecipe()
            in Routes.dialog -> dialogRecipe()
            in Routes.editor -> editorRecipe()
            in Routes.file -> fileRecipe()
            in Routes.form -> formRecipe()
            in Routes.empty -> box { }
            in Routes.grid -> gridRecipe()
            in Routes.select -> selectRecipe()
            in Routes.sidebar -> sideBarRecipe()
            in Routes.svg -> svgRecipe()
            in Routes.text -> textRecipe()
            else -> text("unknown route: $navState")
        }
    }
}