/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.parse

import `fun`.adaptive.foundation.testing.AdaptiveTestAdapter
import `fun`.adaptive.graphics.canvas.TestCanvas
import `fun`.adaptive.graphics.svg.SvgAdapter
import kotlin.test.Test

class ParseSvgKtTest {
    @Test
    fun basic() {
        val canvas = TestCanvas()
        val fragment = parseSvg(SvgAdapter(AdaptiveTestAdapter(), canvas), svg2)
        fragment.draw()
        //canvas.trace.forEach { println(it) }
    }
}