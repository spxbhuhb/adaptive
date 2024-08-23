/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.cookbook.auth.authMain
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
import `fun`.adaptive.foundation.instruction.Trace
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.common.browser
import `fun`.adaptive.ui.common.fragment.*
import `fun`.adaptive.ui.common.instruction.*
import `fun`.adaptive.ui.common.platform.mediaMetrics
import `fun`.adaptive.ui.common.platform.withJsResources

val cookbookContent = name("cookbook-content")

fun main() {

    withJsResources()

    browser { adapter ->

        with(adapter.defaultTextRenderData) {
            fontName = "Noto Sans"
            fontSize = 16.sp
        }

        val media = mediaMetrics()

        grid {
            height { media.viewHeight.dp } .. width { media.viewWidth.dp } .. padding { 16.dp } .. gap { 16.dp }
            rowTemplate(40.dp, 1.fr)

            text("Cookbook")

            slot(cookbookContent, Trace(".*")) {
                route { authMain() }
                route { layoutMobileMain() }
                route { layoutDesktopMain() }

                recipeList()
            }
        }
    }
}

@Adaptive
fun recipeList() {
    val filter = ""

    column {
        gap { 16.dp }

        input { filter } .. inputStyle .. width { 200.dp }

        flowBox {
            gap { 16.dp }
            recipe("Layout - Mobile", "Layouts for small screens.") .. navClick(cookbookContent) { layoutMobileMain() }
            recipe("Layout - Desktop", "Layouts for large screens.") .. navClick(cookbookContent) { layoutDesktopMain() }
            recipe("Auth", "Basic authorization and authentication: login, logout, password change, user management.") .. navClick(cookbookContent) { authMain() }
        }
    }

}

@Adaptive
fun recipe(title: String, explanation: String, vararg instructions: AdaptiveInstruction) : AdaptiveFragment {

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