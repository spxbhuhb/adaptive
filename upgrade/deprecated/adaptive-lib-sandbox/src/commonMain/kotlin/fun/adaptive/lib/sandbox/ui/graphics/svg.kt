/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.sandbox.ui.graphics

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.width
import sandbox.lib.thermometer

@Adaptive
fun svgExample() {
    box {
        width { 24.dp }
        height { 24.dp }

        svg(Graphics.thermometer)
    }
}