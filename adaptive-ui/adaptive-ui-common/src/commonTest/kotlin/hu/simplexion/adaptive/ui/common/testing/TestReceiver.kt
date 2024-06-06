/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.testing

import hu.simplexion.adaptive.ui.common.layout.RawFrame

class TestReceiver(
    val testFrame : RawFrame = RawFrame.NaF
) {
    val children = mutableListOf<TestReceiver>()

    var testTop = 0f
    var testLeft = 0f
    var testWidth = 0f
    var testHeight = 0f

}