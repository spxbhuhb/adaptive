/*
 * Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 *
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package hu.simplexion.adaptive.resource

import platform.Foundation.*
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

actual fun getSystemEnvironment(): ResourceEnvironment {
    val locale = NSLocale.preferredLanguages.firstOrNull()
        ?.let { NSLocale(it as String) }
        ?: NSLocale.currentLocale

    val languageCode = locale.languageCode
    val regionCode = locale.objectForKey(NSLocaleCountryCode) as? String
    val mainScreen = UIScreen.mainScreen
    val isDarkTheme = mainScreen.traitCollection().userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark

    //there is no an API to get a physical screen size and calculate a real DPI
    val density = mainScreen.scale.toFloat()
    return ResourceEnvironment(
        language = LanguageQualifier(languageCode),
        region = RegionQualifier(regionCode.orEmpty()),
        theme = ThemeQualifier.selectByValue(isDarkTheme),
        density = DensityQualifier.selectByDensity(density)
    )
}