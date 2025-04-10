/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ktor.api

fun String.toWs(path: String): String {
    var url = this.trim()

    when {
        url.startsWith("http://") -> url = url.replaceRange(0, 7, "ws://")
        url.startsWith("https://") -> url = url.replaceRange(0, 8, "wss://")
        ! url.startsWith("ws://") && ! url.startsWith("wss://") -> url = "wss://$url"
    }

    return url.trimEnd('/') + '/' + path.trimStart('/')
}