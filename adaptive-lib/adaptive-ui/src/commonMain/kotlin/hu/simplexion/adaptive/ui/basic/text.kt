/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.basic

import hu.simplexion.adaptive.base.Adaptive
import hu.simplexion.adaptive.base.Delegated
import hu.simplexion.adaptive.base.manualImplementation

@Delegated
@Adaptive
fun text(text: String) {
    manualImplementation(text)
}