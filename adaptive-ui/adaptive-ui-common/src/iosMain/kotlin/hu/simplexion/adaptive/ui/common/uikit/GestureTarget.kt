/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.uikit

import hu.simplexion.adaptive.foundation.internal.BoundSupportFunction
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import platform.darwin.NSObject

@OptIn(BetaInteropApi::class)
@ExportObjCClass
class GestureTarget(
    val supportFunction: BoundSupportFunction
) : NSObject() {

    @OptIn(BetaInteropApi::class)
    @ObjCAction
    fun viewTapped() {
        supportFunction.invoke()
    }

}