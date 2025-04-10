package `fun`.adaptive.resource.platform

import `fun`.adaptive.resource.DensityQualifier
import `fun`.adaptive.resource.LanguageQualifier
import `fun`.adaptive.resource.RegionQualifier
import `fun`.adaptive.resource.ResourceEnvironment
import `fun`.adaptive.resource.ThemeQualifier

import java.util.Locale

actual fun getResourceEnvironment(): ResourceEnvironment {
    val defaultLocale = Locale.getDefault()

    return ResourceEnvironment(
        LanguageQualifier(defaultLocale.language),
        RegionQualifier(defaultLocale.country),
        ThemeQualifier.INVALID,
        DensityQualifier.INVALID
    )
}