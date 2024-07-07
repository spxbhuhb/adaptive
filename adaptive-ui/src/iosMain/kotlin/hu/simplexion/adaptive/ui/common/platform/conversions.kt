/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.platform

import hu.simplexion.adaptive.ui.common.instruction.Color
import platform.UIKit.UIColor

val Color.uiColor: UIColor
    get() {
        // TODO val alpha = ((value shr 24) and 0xFF) / 255.0
        val red = ((value shr 16) and 0xFFu).toInt() / 255.0
        val green = ((value shr 8) and 0xFFu).toInt() / 255.0
        val blue = (value and 0xFFu).toInt() / 255.0

        return UIColor.colorWithRed(red, green, blue, 1.0)
    }