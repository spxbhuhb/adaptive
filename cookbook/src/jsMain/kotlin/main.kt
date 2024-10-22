/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.backend.backend
import `fun`.adaptive.cookbook.Res
import `fun`.adaptive.cookbook.cookbookCommon
import `fun`.adaptive.cookbook.eco
import `fun`.adaptive.cookbook.ui.form.formRecipe
import `fun`.adaptive.cookbook.grid_view
import `fun`.adaptive.cookbook.iot.iotCommon
import `fun`.adaptive.cookbook.ui.dialog.dialogRecipe
import `fun`.adaptive.cookbook.ui.editor.editorRecipe
import `fun`.adaptive.cookbook.ui.select.selectRecipe
import `fun`.adaptive.cookbook.ui.sidebar.sideBarRecipe
import `fun`.adaptive.cookbook.ui.svg.svgRecipe
import `fun`.adaptive.cookbook.ui.text.textRecipe
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.boldFont
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.position
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {
    CoroutineScope(Dispatchers.Default).launch {

        iotCommon()
        cookbookCommon()

        withJsResources()

//        val localBackend = backend(webSocketTransport(window.location.origin)) { auto() }
        val localBackend = backend { auto() }

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

private val appNavState = autoInstance(Routes.editor)

private object Routes {
    val dialog = NavState("Dialog")
    val editor = NavState("Editor")
    val form = NavState("Form")
    val select = NavState("Select")
    val sidebar = NavState("SideBar")
    val svg = NavState("SVG")
    val text = NavState("Text")
}

private val items = listOf(
    SidebarItem(0, Res.drawable.grid_view, "Dialog", Routes.dialog),
    SidebarItem(0, Res.drawable.grid_view, "Editor", Routes.editor),
    SidebarItem(0, Res.drawable.grid_view, "Form", Routes.form),
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

    val navState = autoInstance(appNavState)

    box {
        maxSize .. padding { 16.dp } .. backgroundColor(0xFAFAFA)

        when (navState) {
            in Routes.dialog -> dialogRecipe()
            in Routes.editor -> editorRecipe()
            in Routes.form -> formRecipe()
            in Routes.select -> selectRecipe()
            in Routes.sidebar -> sideBarRecipe()
            in Routes.svg -> svgRecipe()
            in Routes.text -> textRecipe()
            else -> text("unknown route: $navState")
        }
    }
}