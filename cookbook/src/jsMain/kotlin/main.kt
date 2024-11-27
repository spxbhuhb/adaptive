/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.auth.authCommon
import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.cookbook.Res
import `fun`.adaptive.cookbook.Routes
import `fun`.adaptive.cookbook.appNavState
import `fun`.adaptive.cookbook.auth.authRecipe
import `fun`.adaptive.cookbook.cookbookCommon
import `fun`.adaptive.cookbook.eco
import `fun`.adaptive.cookbook.graphics.canvas.canvasRecipe
import `fun`.adaptive.cookbook.items
import `fun`.adaptive.cookbook.menu
import `fun`.adaptive.cookbook.ui.dialog.dialogRecipe
import `fun`.adaptive.cookbook.ui.editor.editorRecipe
import `fun`.adaptive.cookbook.ui.event.eventRecipe
import `fun`.adaptive.cookbook.ui.form.formRecipe
import `fun`.adaptive.cookbook.ui.layout.box.boxRecipe
import `fun`.adaptive.cookbook.ui.layout.grid.gridRecipe
import `fun`.adaptive.cookbook.ui.layout.responsive.responsiveMain
import `fun`.adaptive.cookbook.ui.navigation.navigationRecipe
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
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.form.FormFragmentFactory
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.layout.app.default.defaultAppLayout
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

        }
    }
}

private val appNavState = autoItemOrigin(Routes.grid)

private object Routes {
    val dialog = NavState("Dialog")
    val editor = NavState("Editor")
    val form = NavState("Form")
    val grid = NavState("Grid")
    val select = NavState("Select")
    val sidebar = NavState("SideBar")
    val svg = NavState("SVG")
    val text = NavState("Text")
}

private val items = listOf(
    SidebarItem(0, Res.drawable.grid_view, "Dialog", Routes.dialog),
    SidebarItem(0, Res.drawable.grid_view, "Editor", Routes.editor),
    SidebarItem(0, Res.drawable.grid_view, "Form", Routes.form),
    SidebarItem(0, Res.drawable.grid_view, "Grid", Routes.grid),
    SidebarItem(0, Res.drawable.grid_view, "Select", Routes.select),
    SidebarItem(0, Res.drawable.grid_view, "SideBar", Routes.sidebar),
    SidebarItem(0, Res.drawable.grid_view, "SVG", Routes.svg),
    SidebarItem(0, Res.drawable.grid_view, "Text", Routes.text)
)

@Adaptive
fun mainMenu() {
    grid {
        rowTemplate(168.dp, 1.fr) .. maxSize
        box {
            svg(Res.drawable.eco) .. position(60.dp, 32.dp)
            text("Adaptive") .. boldFont .. fontSize(28.sp) .. position(60.dp, 88.dp)
        }
        fullSidebar(items, appNavState)
    }
}

@Adaptive
fun mainContent() {

    val navState = autoItemOrigin(appNavState)

    box {
        maxSize .. padding { 16.dp } .. backgroundColor(0xFAFAFA)

        when (navState) {
            in Routes.auth -> authRecipe(navState)
            in Routes.box -> boxRecipe()
            in Routes.canvas -> canvasRecipe()
            in Routes.dialog -> dialogRecipe()
            in Routes.editor -> editorRecipe()
            in Routes.empty -> box { }
            in Routes.event -> eventRecipe()
            in Routes.form -> formRecipe()
            in Routes.grid -> gridRecipe()
            in Routes.navigation -> navigationRecipe(navState)
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