/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.fragment.AdaptiveUIFragment

interface AdaptiveUIInstruction : AdaptiveInstruction {
    fun apply(uiInstructions: AdaptiveUIFragment.UIInstructions)
}