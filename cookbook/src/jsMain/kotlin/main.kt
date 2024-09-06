/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.backend.backend
import `fun`.adaptive.cookbook.auth.authMain
import `fun`.adaptive.cookbook.components.componentsMain
import `fun`.adaptive.cookbook.intro.introMain
import `fun`.adaptive.cookbook.iot.iotCommon
import `fun`.adaptive.cookbook.iot.iotMain
import `fun`.adaptive.cookbook.layout.desktop.layoutDesktopMain
import `fun`.adaptive.cookbook.layout.mobile.layoutMobileMain
import `fun`.adaptive.cookbook.shared.bodySmall
import `fun`.adaptive.cookbook.shared.cornerRadius8
import `fun`.adaptive.cookbook.shared.inputStyle
import `fun`.adaptive.cookbook.shared.lightBackground
import `fun`.adaptive.cookbook.shared.shadow
import `fun`.adaptive.cookbook.shared.titleSmall
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.ktor.withWebSocketTransport
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.cornerTopRadius
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.flowText
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.input
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.navClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.platform.withJsResources
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val cookbookContent = name("cookbook-content")

fun main() {
    CoroutineScope(Dispatchers.Default).launch {

        iotCommon()

        withJsResources()

        val localBackend = backend { auto() }

        withWebSocketTransport(window.location.origin, serviceImplFactory = localBackend)

        browser(CanvasFragmentFactory, SvgFragmentFactory, backend = localBackend) { adapter ->

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
            }

            adapter.theme["AuiInput"] = inputStyle

//            grid {
//                maxHeight .. padding { 16.dp } .. gap { 16.dp }
//                rowTemplate(40.dp, 1.fr)

                iotMain()

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

@Adaptive
fun recipeList() {
    val filter = ""

    column {
        gap { 16.dp }

        input { filter } .. width { 200.dp }

        flowBox {
            gap { 16.dp }
            recipe("What is Adaptive?", "An introduction to Adaptive.") .. navClick(cookbookContent) { introMain() }
            recipe("Layout - Mobile", "Layouts for small screens.") .. navClick(cookbookContent) { layoutMobileMain() }
            recipe("Layout - Desktop", "Layouts for large screens.") .. navClick(cookbookContent) { layoutDesktopMain() }
            recipe("Components", "Simple components.") .. navClick(cookbookContent) { componentsMain() }
            recipe("Auth", "Basic authorization and authentication: login, logout, password change, user management.") .. navClick(cookbookContent) { authMain() }
            recipe("IoT", "Basic IoT UI") .. navClick(cookbookContent) { iotMain() }
        }
    }

}

@Adaptive
fun recipe(title: String, explanation: String, vararg instructions: AdaptiveInstruction): AdaptiveFragment {

    grid(*instructions) {
        width { 240.dp } .. height { 120.dp } .. cornerRadius8 .. lightBackground .. shadow
        rowTemplate(38.dp, 1.fr)

        box {
            maxSize .. backgroundColor(0xFFF7E1u) .. cornerTopRadius(8.dp) .. paddingLeft { 12.dp }
            alignItems.startCenter

            text(title) .. titleSmall
        }

        flowText(explanation) .. padding { 12.dp } .. bodySmall
    }

    return fragment()
}