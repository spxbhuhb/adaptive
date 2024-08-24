/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.platform

import android.content.res.Configuration
import android.content.res.Resources
import `fun`.adaptive.resource.*
import java.util.*

fun withAndroidResources() {
    defaultResourceEnvironmentOrNull = getSystemEnvironment()
    defaultResourceReaderOrNull = getPlatformResourceReader()
}

private fun getSystemEnvironment(): ResourceEnvironment {
    val locale = Locale.getDefault()
    val configuration = Resources.getSystem().configuration
    val isDarkTheme = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    val dpi = configuration.densityDpi
    return ResourceEnvironment(
        language = LanguageQualifier(locale.language),
        region = RegionQualifier(locale.country),
        theme = ThemeQualifier.selectByValue(isDarkTheme),
        density = DensityQualifier.selectByValue(dpi)
    )
}