/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveExpectInstruction
import hu.simplexion.adaptive.ui.common.commonUI

class BoundingRect(
    val x : Float,
    val y : Float,
    val width : Float,
    val height : Float
) : AdaptiveExpectInstruction("$commonUI:BoundingRect")