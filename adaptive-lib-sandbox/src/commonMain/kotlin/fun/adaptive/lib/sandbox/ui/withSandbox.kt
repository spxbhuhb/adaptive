/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.sandbox.ui

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.ui.common.AbstractCommonAdapter
import `fun`.adaptive.ui.common.instruction.sp

fun withSandbox(adapter: AdaptiveAdapter) {
    if (adapter !is AbstractCommonAdapter<*, *>) return

    with(adapter.defaultTextRenderData) {
        fontName = "Noto Sans"
        fontSize = 17.sp
    }
}