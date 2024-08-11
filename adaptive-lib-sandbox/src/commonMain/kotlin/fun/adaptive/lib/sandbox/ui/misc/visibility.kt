/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.sandbox.ui.misc

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.common.fragment.text

@Adaptive
fun publicFun() {
    internalFun()
}

@Adaptive
internal fun internalFun() {
    privateFun()
}

@Adaptive
private fun privateFun() {
    text("private fun")
}