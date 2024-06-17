/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.ui.common.browser
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.platform.mediaMetrics
import hu.simplexion.adaptive.ui.common.platform.withJsResources

fun main() {

    withJsResources()

    browser {
        resize()
    }

}

private val black = Color(0x0u)

@Adaptive
fun resize() {
    val media = mediaMetrics()

    column(padding(10.dp), size((media.viewPortWidth / 2).dp, (media.viewPortHeight / 2).dp), border(black, 1.dp)) {
        text("${media.viewPortWidth} x ${media.viewPortHeight}")
    }

}