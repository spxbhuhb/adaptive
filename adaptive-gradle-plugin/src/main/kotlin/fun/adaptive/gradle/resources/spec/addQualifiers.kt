package `fun`.adaptive.gradle.resources.spec

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock

fun CodeBlock.Builder.addQualifiers(qualifiers: List<String>): CodeBlock.Builder {
    val languageQualifier = ClassName("fun.adaptive.resource", "LanguageQualifier")
    val regionQualifier = ClassName("fun.adaptive.resource", "RegionQualifier")
    val themeQualifier = ClassName("fun.adaptive.resource", "ThemeQualifier")
    val densityQualifier = ClassName("fun.adaptive.resource", "DensityQualifier")

    val languageRegex = Regex("[a-z]{2,3}")
    val regionRegex = Regex("r[A-Z]{2}")

    val qualifiersMap = mutableMapOf<ClassName, String>()

    qualifiers.forEach { q ->
        when (q) {
            "light",
            "dark" -> {
                qualifiersMap[themeQualifier] = q
            }

            "mdpi",
            "hdpi",
            "xhdpi",
            "xxhdpi",
            "xxxhdpi",
            "ldpi" -> {
                qualifiersMap[densityQualifier] = q
            }

            else -> when {
                q.matches(languageRegex) -> {
                    qualifiersMap[languageQualifier] = q
                }

                q.matches(regionRegex) -> {
                    qualifiersMap[regionQualifier] = q
                }

                else -> error("unknown qualifier: '$q'.")
            }
        }
    }

    qualifiersMap[themeQualifier]?.let { q -> add("%T.${q.uppercase()}, ", themeQualifier) }
    qualifiersMap[densityQualifier]?.let { q -> add("%T.${q.uppercase()}, ", densityQualifier) }
    qualifiersMap[languageQualifier]?.let { q -> add("%T(\"$q\"), ", languageQualifier) }
    qualifiersMap[regionQualifier]?.let { q -> add("%T(\"${q.takeLast(2)}\"), ", regionQualifier) }

    return this
}