/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.xlsx.internal

import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

internal actual suspend fun ContentMap.pack() : ByteArray {

    val os = ByteArrayOutputStream()

    ZipOutputStream(os).use { zip ->

        zip.setLevel(9)

        val w = zip.bufferedWriter()

        for ((path, content) in this) {
            zip.putNextEntry(ZipEntry(path))
            content(w::write)
            w.flush()
            zip.closeEntry()
        }

    }

    return os.toByteArray()
}