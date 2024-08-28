/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.platform

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.instruction.event.OnClick
import `fun`.adaptive.ui.instruction.event.UIEvent
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import platform.UIKit.UIGestureRecognizer
import platform.UIKit.UIGestureRecognizerDelegateProtocol
import platform.UIKit.UIView
import platform.darwin.NSObject

@OptIn(BetaInteropApi::class)
@ExportObjCClass
class GestureTarget(
    val fragment : AbstractAuiFragment<UIView>,
    val instruction: AdaptiveInstruction
) : NSObject() {

    @OptIn(BetaInteropApi::class)
    @ObjCAction
    fun viewTapped() {
        if (instruction is OnClick) {
            // FIXME iOS UIEvent position
            instruction.execute(UIEvent(fragment, null))
        }
    }

}

class Delegate : NSObject(), UIGestureRecognizerDelegateProtocol {

    override fun gestureRecognizerShouldBegin(gestureRecognizer: UIGestureRecognizer): Boolean {
        println("Hello World!")
        return true
    }
}