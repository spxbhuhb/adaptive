/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.common

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

var enabled = false

val separator = "\n" + "=".padEnd(80, '=') + "\n"

fun debug(message: () -> Any?) {
    if (! enabled) return
    Files.write(Paths.get("/Users/tiz/Desktop/adaptive-plugin-debug.txt"), "${message()}\n".encodeToByteArray(), StandardOpenOption.APPEND, StandardOpenOption.CREATE)
}