/*
 * Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 *
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package `fun`.adaptive.resource

fun getResourceUrl(windowOrigin: String, windowPathname: String, resourcePath: String): String {
    if (resourcePath.startsWith("http://") || resourcePath.startsWith("https://")) return resourcePath

    val path = WebResourcesConfiguration.getResourcePath(resourcePath)

    return when {
        path.startsWith("/") -> windowOrigin + path
        path.startsWith("http://") || path.startsWith("https://") -> path
        else -> windowOrigin + windowPathname + path
    }
}