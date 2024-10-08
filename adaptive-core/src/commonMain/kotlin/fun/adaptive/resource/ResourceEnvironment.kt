/*
 * Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 *
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package `fun`.adaptive.resource

class ResourceEnvironment(
    val language: LanguageQualifier,
    val region: RegionQualifier,
    val theme: ThemeQualifier,
    val density: DensityQualifier
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ResourceEnvironment

        if (language != other.language) return false
        if (region != other.region) return false
        if (theme != other.theme) return false
        if (density != other.density) return false

        return true
    }

    override fun hashCode(): Int {
        var result = language.hashCode()
        result = 31 * result + region.hashCode()
        result = 31 * result + theme.hashCode()
        result = 31 * result + density.hashCode()
        return result
    }
}

val defaultResourceEnvironment: ResourceEnvironment
    get() = checkNotNull(defaultResourceEnvironmentOrNull) { "no resource environment has been set, use with<platform>Resources() in your bootstrap to set the default one for the given platform" }

var defaultResourceEnvironmentOrNull : ResourceEnvironment? = null

fun Resource.getResourceItemByEnvironment(environment: ResourceEnvironment): ResourceItem {
    //Priority of environments: https://developer.android.com/guide/topics/resources/providing-resources#table2
    items.toList()
        .filterByLocale(environment.language, environment.region)
        .also { if (it.size == 1) return it.first() }
        .filterBy(environment.theme)
        .also { if (it.size == 1) return it.first() }
        .filterBy(environment.density)
        .also { if (it.size == 1) return it.first() }
        .let { items ->
            if (items.isEmpty()) {
                error("Resource with ID='$id' not found")
            } else {
                error("Resource with ID='$id' has more than one file: ${items.joinToString { it.path }}")
            }
        }
}

private fun List<ResourceItem>.filterBy(qualifier: Qualifier): List<ResourceItem> {
    //Android has a slightly different algorithm,
    //but it provides the same result: https://developer.android.com/guide/topics/resources/providing-resources#BestMatch

    //filter items with the requested qualifier
    val withQualifier = filter { item ->
        item.qualifiers.any { it == qualifier }
    }

    if (withQualifier.isNotEmpty()) return withQualifier

    //items with no requested qualifier type (default)
    return filter { item ->
        item.qualifiers.none { it::class == qualifier::class }
    }
}

// we need to filter by language and region together because there is slightly different logic:
// 1) if there is the exact match language+region then use it
// 2) if there is the language WITHOUT region match then use it
// 3) in other cases use items WITHOUT language and region qualifiers at all
// issue: https://github.com/JetBrains/compose-multiplatform/issues/4571
private fun List<ResourceItem>.filterByLocale(language: LanguageQualifier, region: RegionQualifier): List<ResourceItem> {
    val withLanguage = filter { item ->
        item.qualifiers.any { it == language }
    }

    val withExactLocale = withLanguage.filter { item ->
        item.qualifiers.any { it == region }
    }

    //if there are the exact language + the region items
    if (withExactLocale.isNotEmpty()) return withExactLocale

    val withDefaultRegion = withLanguage.filter { item ->
        item.qualifiers.none { it is RegionQualifier }
    }

    //if there are the language without a region items
    if (withDefaultRegion.isNotEmpty()) return withDefaultRegion

    //items without any locale qualifiers
    return filter { item ->
        item.qualifiers.none { it is LanguageQualifier || it is RegionQualifier }
    }
}