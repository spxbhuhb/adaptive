/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.backend.backend
import `fun`.adaptive.cookbook.iot.iotCommon
import `fun`.adaptive.cookbook.shared.inputStyle
import `fun`.adaptive.cookbook.ui.boundInput.boundInputRecipe
import `fun`.adaptive.cookbook.ui.button.buttonRecipe
import `fun`.adaptive.cookbook.ui.dialog.dialogRecipe
import `fun`.adaptive.cookbook.ui.filter.quickFilterRecipe
import `fun`.adaptive.cookbook.ui.grid.gridAlignRecipe
import `fun`.adaptive.cookbook.ui.select.selectRecipe
import `fun`.adaptive.cookbook.ui.sidebar.sideBarRecipe
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.form.FormFragmentFactory
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.platform.withJsResources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val cookbookContent = name("cookbook-content")

fun main() {
    CoroutineScope(Dispatchers.Default).launch {

        iotCommon()

        withJsResources()

//        val localBackend = backend(webSocketTransport(window.location.origin)) { auto() }
        val localBackend = backend { auto() }

        browser(CanvasFragmentFactory, SvgFragmentFactory, FormFragmentFactory, backend = localBackend) { adapter ->

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
            }

            adapter.theme["AuiInput"] = inputStyle

//            grid {
//                maxHeight .. padding { 16.dp } .. gap { 16.dp }
//                rowTemplate(40.dp, 1.fr)

//            formMain()
//            dialogMain()

//            iotMain()
//              box {
//                  hoverMain()
//              }

//            boundInputRecipe()

            //            dialogRecipe()

//            buttonRecipe()
//            gridAlignRecipe()
//              selectRecipe()
            sideBarRecipe()
//            quickFilterRecipe()

//            navMain()

//            projectWizardMain()

//                text("Cookbook")
//
//                slot(cookbookContent) {
//                    route { authMain() }
//                    route { introMain() }
//                    route { layoutMobileMain() }
//                    route { layoutDesktopMain() }
//                    route { componentsMain() }
//                    route { iotMain() }
//
//                    recipeList()
//                }
//           }
        }
    }
}