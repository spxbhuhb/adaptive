package old/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.ui.common.fragment.svg
import sandbox.Res
import sandbox.thermometer

@Adaptive
fun svgTest() {
    svg(Res.drawable.thermometer)
}