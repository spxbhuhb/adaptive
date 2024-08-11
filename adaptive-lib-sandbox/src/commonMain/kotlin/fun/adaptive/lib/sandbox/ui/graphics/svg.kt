/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.sandbox.ui.graphics

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.common.fragment.box
import `fun`.adaptive.ui.common.fragment.svg
import `fun`.adaptive.ui.common.instruction.dp
import `fun`.adaptive.ui.common.instruction.height
import `fun`.adaptive.ui.common.instruction.width
import sandbox.lib.Res
import sandbox.lib.thermometer

@Adaptive
fun svgExample() {
    box {
        width { 24.dp }
        height { 24.dp }

        svg(Res.drawable.thermometer)
    }
}