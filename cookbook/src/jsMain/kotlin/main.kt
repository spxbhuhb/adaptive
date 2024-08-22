/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.common.browser
import `fun`.adaptive.ui.common.fragment.*
import `fun`.adaptive.ui.common.instruction.*
import `fun`.adaptive.ui.common.instruction.AlignItems.Companion.alignItems
import `fun`.adaptive.ui.common.platform.mediaMetrics
import `fun`.adaptive.ui.common.platform.withJsResources

fun main() {

    withJsResources()

    browser { adapter ->

        with(adapter.defaultTextRenderData) {
            fontName = "Noto Sans"
            fontSize = 16.sp
        }

        val media = mediaMetrics()
        //val background = if (media.isLight) lightBackground else darkBackground
        val filter = ""

        grid {
            maxSize .. padding { 16.dp } .. gap { 16.dp }
            rowTemplate(40.dp, 44.dp, 1.fr)

            text("Cookbook")

            input { filter } .. inputStyle .. width { 200.dp }

            box {
                recipe("Auth", "Basic authorization and authentication: login, logout, password change, user management.")
            }
        }
    }
}

@Adaptive
fun recipe(title: String, explanation: String) {
    grid {
        width { 240.dp } .. height { 120.dp } .. cornerRadius8 .. lightBackground .. shadow
        rowTemplate(38.dp, 1.fr)
        onClick { println("hello world") }

        box {
            maxSize .. backgroundColor(0xFFF7E1u) .. cornerTopRadius(8.dp) .. paddingLeft { 12.dp }
            alignItems.startCenter

            text(title) .. titleSmall
        }

        flowText(explanation) .. padding { 12.dp } .. bodySmall
    }
}