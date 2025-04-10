/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.testing

import `fun`.adaptive.ui.fragment.layout.RawFrame

class TestReceiver(
    val testFrame : RawFrame? = null
) {
    val children = mutableListOf<TestReceiver>()

    var testTop = 0.0
    var testLeft = 0.0
    var testWidth = 0.0
    var testHeight = 0.0

    var textContent = ""
}