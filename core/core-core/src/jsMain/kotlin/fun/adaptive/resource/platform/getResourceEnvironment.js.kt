/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.resource.platform

import `fun`.adaptive.resource.DensityQualifier
import `fun`.adaptive.resource.LanguageQualifier
import `fun`.adaptive.resource.RegionQualifier
import `fun`.adaptive.resource.ResourceEnvironment
import `fun`.adaptive.resource.ThemeQualifier
import kotlinx.browser.window

actual fun getResourceEnvironment(): ResourceEnvironment {
    val locale = Intl.Locale(window.navigator.language)
    val isDarkTheme = window.matchMedia("(prefers-color-scheme: dark)").matches
    //96 - standard browser DPI https://developer.mozilla.org/en-US/docs/Web/API/Window/devicePixelRatio
    val dpi: Int = (window.devicePixelRatio * 96).toInt()
    return ResourceEnvironment(
        language = LanguageQualifier(locale.language),
        region = RegionQualifier(locale.region),
        theme = ThemeQualifier.Companion.selectByValue(isDarkTheme),
        density = DensityQualifier.Companion.selectByValue(dpi)
    )
}

private external class Intl {
    class Locale(locale: String) {
        val language: String
        val region: String
    }
}