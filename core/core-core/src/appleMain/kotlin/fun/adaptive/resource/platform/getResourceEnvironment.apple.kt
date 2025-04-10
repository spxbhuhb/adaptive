package `fun`.adaptive.resource.platform

import `fun`.adaptive.resource.DensityQualifier
import `fun`.adaptive.resource.LanguageQualifier
import `fun`.adaptive.resource.RegionQualifier
import `fun`.adaptive.resource.ResourceEnvironment
import `fun`.adaptive.resource.ThemeQualifier
import platform.Foundation.NSLocale
import platform.Foundation.NSLocaleCountryCode
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.preferredLanguages
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

actual fun getResourceEnvironment(): ResourceEnvironment {

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