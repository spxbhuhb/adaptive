/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.ui.common.fragment.AdaptiveUIFragment

class Font(val fontName: String) : AdaptiveUIInstruction {

    override fun apply(uiInstructions: AdaptiveUIFragment.UIInstructions) {
        uiInstructions.fontName = fontName
    }

}

class FontSize(val fontSize: Float) : AdaptiveUIInstruction{

    override fun apply(uiInstructions: AdaptiveUIFragment.UIInstructions) {
        uiInstructions.fontSize = fontSize
    }

}