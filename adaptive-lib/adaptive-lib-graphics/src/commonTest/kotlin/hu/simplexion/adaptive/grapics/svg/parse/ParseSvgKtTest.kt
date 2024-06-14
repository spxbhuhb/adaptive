/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.svg.parse

import hu.simplexion.adaptive.grapics.canvas.TestCanvas
import hu.simplexion.adaptive.grapics.svg.SvgAdapter
import hu.simplexion.adaptive.grapics.svg.parse.parseSvg
import kotlin.test.Test

class ParseSvgKtTest {
    @Test
    fun basic() {
        val adapter = SvgAdapter(null, TestCanvas())
        adapter.trace = arrayOf(Regex(".*"))
        adapter.rootFragment = parseSvg(adapter, svg)
        adapter.rootFragment.mount()
    }
}