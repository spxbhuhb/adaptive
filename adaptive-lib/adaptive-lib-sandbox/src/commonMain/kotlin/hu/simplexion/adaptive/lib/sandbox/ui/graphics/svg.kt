/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.sandbox.ui.graphics

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.ui.common.fragment.box
import hu.simplexion.adaptive.ui.common.fragment.svg
import hu.simplexion.adaptive.ui.common.instruction.dp
import hu.simplexion.adaptive.ui.common.instruction.height
import hu.simplexion.adaptive.ui.common.instruction.width
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