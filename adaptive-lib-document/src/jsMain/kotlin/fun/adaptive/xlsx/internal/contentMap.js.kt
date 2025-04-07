/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.xlsx.internal

import `fun`.adaptive.lib.util.zip.ZipBuilder

internal actual suspend fun ContentMap.pack(): ByteArray {

    val zipBuilder = ZipBuilder()

    for ((path, content) in this) {
        zipBuilder.add(path, buildString { content(::append) }.encodeToByteArray())
    }

    return zipBuilder.finalize()
}