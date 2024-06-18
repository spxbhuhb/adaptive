/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.platform

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.instruction.AdaptiveUIEvent
import hu.simplexion.adaptive.ui.common.instruction.OnClick
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import platform.UIKit.UIView
import platform.darwin.NSObject

@OptIn(BetaInteropApi::class)
@ExportObjCClass
class GestureTarget(
    val fragment : AbstractCommonFragment<UIView>,
    val instruction: AdaptiveInstruction
) : NSObject() {

    @OptIn(BetaInteropApi::class)
    @ObjCAction
    fun viewTapped() {
        if (instruction is OnClick) {
            instruction.execute(AdaptiveUIEvent(fragment, null))
        }
    }

}