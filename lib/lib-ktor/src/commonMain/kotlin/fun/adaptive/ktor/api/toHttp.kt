/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ktor.api

fun String.toHttp(path: String): String {
    var url = this.trim()

    when {
        url.startsWith("ws://") -> url = url.replaceRange(0, 4, "http://")
        url.startsWith("wss://") -> url = url.replaceRange(0, 5, "https://")
        ! url.startsWith("http://") && ! url.startsWith("https://") -> url = "wss://$url"
    }

    return url.removeSuffix("/") + '/' + path.trimStart('/')
}