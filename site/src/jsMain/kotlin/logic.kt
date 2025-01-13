/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.resource.ThemeQualifier
import `fun`.adaptive.ui.AbstractAuiAdapter

val mainContent = name("main content")

fun AdaptiveAdapter.switchTheme() {
    if (this !is AbstractAuiAdapter<*, *>) return
    if (mediaMetrics.isDark) {
        manualTheme = ThemeQualifier.LIGHT
    } else {
        manualTheme = ThemeQualifier.DARK
    }
}