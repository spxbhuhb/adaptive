/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.sandbox.ui

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.instruction.sp

fun withSandbox(adapter: AdaptiveAdapter) {
    if (adapter !is AbstractCommonAdapter<*, *>) return

    with(adapter.defaultTextRenderData) {
        fontName = "Noto Sans"
        fontSize = 17.sp
    }
}