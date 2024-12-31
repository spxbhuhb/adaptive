/*
 * Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 *
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package `fun`.adaptive.resource

import `fun`.adaptive.resource.platform.getResourceReader
import kotlinx.io.files.Path

val defaultResourceReader: ResourceReader = getResourceReader()

interface ResourceReader {
    suspend fun read(path: String): ByteArray
    suspend fun readPart(path: String, offset: Long, size: Long): ByteArray
    fun getUri(path: String): String
    fun sizeAndLastModified(path: Path): ResourceMetadata
    fun setFileModificationTime(path: Path, timestamp: Long)
}

class MissingResourceException(path: String) : Exception("Missing resource with path: $path")
