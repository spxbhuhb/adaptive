/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.auth.authCommon
import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.cookbook.Res
import `fun`.adaptive.cookbook.auth.authRecipe
import `fun`.adaptive.cookbook.cookbookCommon
import `fun`.adaptive.cookbook.eco
import `fun`.adaptive.cookbook.graphics.canvas.canvasRecipe
import `fun`.adaptive.cookbook.grid_view
import `fun`.adaptive.cookbook.menu
import `fun`.adaptive.cookbook.ui.dialog.dialogRecipe
import `fun`.adaptive.cookbook.ui.editor.editorRecipe
import `fun`.adaptive.cookbook.ui.file.fileRecipe
import `fun`.adaptive.cookbook.ui.form.formRecipe
import `fun`.adaptive.cookbook.ui.layout.box.boxRecipe
import `fun`.adaptive.cookbook.ui.layout.grid.gridRecipe
import `fun`.adaptive.cookbook.ui.layout.responsive.responsiveMain
import `fun`.adaptive.cookbook.ui.select.selectRecipe
import `fun`.adaptive.cookbook.ui.sidebar.sideBarRecipe
import `fun`.adaptive.cookbook.ui.snackbar.snackbarRecipe
import `fun`.adaptive.cookbook.ui.svg.svgRecipe
import `fun`.adaptive.cookbook.ui.text.textRecipe
import `fun`.adaptive.cookbook.ui.tree.treeRecipe
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.layout.app.default.defaultAppLayout
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.form.FormFragmentFactory
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.sidebar.SidebarItem
import `fun`.adaptive.ui.platform.withJsResources
import `fun`.adaptive.ui.snackbar.SnackbarManager
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
        val localBackend = backend(transport) {
            auto()
            worker { SnackbarManager() }
        }

        browser(CanvasFragmentFactory, SvgFragmentFactory, FormFragmentFactory, backend = localBackend) { adapter ->

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
                fontWeight = 300
            }


            defaultAppLayout(
                items,
                appNavState,
                Res.drawable.menu,
                Res.drawable.eco,
                Res.drawable.eco,
                "Adaptive"
            ) {
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

private val appNavState = autoInstance(Routes.snackbar)

private object Routes {
    val auth = NavState("Auth")
    val box = NavState("Box")
    val canvas = NavState("Canvas")
    val dialog = NavState("Dialog")
    val editor = NavState("Editor")
    val empty = NavState("Empty")
    val file = NavState("File")
    val form = NavState("Form")
    val grid = NavState("Grid")
    val responsive = NavState("Responsive")
    val select = NavState("Select")
    val sidebar = NavState("SideBar")
    val snackbar = NavState("Snackbar")
    val svg = NavState("SVG")
    val text = NavState("Text")
    val tree = NavState("Tree")
}

private val items = listOf(
    SidebarItem(0, Res.drawable.grid_view, "Auth", Routes.auth),
    SidebarItem(0, Res.drawable.grid_view, "Box", Routes.box),
    SidebarItem(0, Res.drawable.grid_view, "Canvas", Routes.canvas),
    SidebarItem(0, Res.drawable.grid_view, "Dialog", Routes.dialog),
    SidebarItem(0, Res.drawable.grid_view, "Editor", Routes.editor),
    SidebarItem(0, Res.drawable.grid_view, "File", Routes.file),
    SidebarItem(0, Res.drawable.grid_view, "Form", Routes.form),
    SidebarItem(0, Res.drawable.grid_view, "Grid", Routes.grid),
    SidebarItem(0, Res.drawable.grid_view, "Responsive", Routes.responsive),
    SidebarItem(0, Res.drawable.grid_view, "Select", Routes.select),
    SidebarItem(0, Res.drawable.grid_view, "Sidebar", Routes.sidebar),
    SidebarItem(0, Res.drawable.grid_view, "Snackbar", Routes.snackbar),
    SidebarItem(0, Res.drawable.grid_view, "SVG", Routes.svg),
    SidebarItem(0, Res.drawable.grid_view, "Text", Routes.text),
    SidebarItem(0, Res.drawable.grid_view, "Tree", Routes.tree)
)


@Adaptive
fun mainContent() {

    val navState = autoInstance(appNavState)

    box {
        maxSize .. padding { 16.dp } .. backgroundColor(0xFAFAFA)

        when (navState) {
            in Routes.auth -> authRecipe()
            in Routes.box -> boxRecipe()
            in Routes.canvas -> canvasRecipe()
            in Routes.dialog -> dialogRecipe()
            in Routes.editor -> editorRecipe()
            in Routes.file -> fileRecipe()
            in Routes.form -> formRecipe()
            in Routes.empty -> box { }
            in Routes.grid -> gridRecipe()
            in Routes.responsive -> responsiveMain()
            in Routes.select -> selectRecipe()
            in Routes.sidebar -> sideBarRecipe()
            in Routes.snackbar -> snackbarRecipe()
            in Routes.svg -> svgRecipe()
            in Routes.text -> textRecipe()
            in Routes.tree -> treeRecipe()
            else -> text("unknown route: $navState")
        }
    }
}