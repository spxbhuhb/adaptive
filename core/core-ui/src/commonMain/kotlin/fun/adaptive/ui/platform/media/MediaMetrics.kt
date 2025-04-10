/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.platform.media

import `fun`.adaptive.resource.ThemeQualifier

/**
 * Contains information about the current media.
 */
data class MediaMetrics(
    val viewWidth: Double,
    val viewHeight: Double,
    val theme: ThemeQualifier,
    val manualTheme: ThemeQualifier?
) {
    val isSmall
        get() = viewWidth < 600.0 || viewHeight < 600.0

    val isMedium
        get() = ! isSmall && ! isLarge

    val isLarge
        get() = viewWidth > 1024.0 || viewHeight > 1024.0

    val isLight
        get() = (manualTheme == ThemeQualifier.LIGHT || (manualTheme == null && theme == ThemeQualifier.LIGHT))

    val isDark
        get() = (manualTheme == ThemeQualifier.DARK || (manualTheme == null && theme == ThemeQualifier.DARK))

}