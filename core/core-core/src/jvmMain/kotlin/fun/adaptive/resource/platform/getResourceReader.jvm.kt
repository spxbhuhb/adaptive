/*
 * Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 *
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package `fun`.adaptive.resource.platform

import `fun`.adaptive.resource.ResourceMetadata
import `fun`.adaptive.resource.MissingResourceException
import `fun`.adaptive.resource.ResourceReader
import kotlinx.io.files.Path
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes

actual fun getResourceReader(): ResourceReader = object : ResourceReader {

    override suspend fun read(path: String): ByteArray {
        val resource = getResourceAsStream(path)
        return resource.readBytes()
    }

    override suspend fun readPart(path: String, offset: Long, size: Long): ByteArray {
        val resource = getResourceAsStream(path)
        val result = ByteArray(size.toInt())
        resource.use { input ->
            input.skip(offset)
            input.read(result, 0, size.toInt())
        }
        return result
    }

    override fun getUri(path: String): String {
        val classLoader = getClassLoader()
        val resource = classLoader.getResource(path) ?: run {
            //try to find a font in the android assets
            if (File(path).isFontResource()) {
                classLoader.getResource("assets/$path")
            } else null
        } ?: throw MissingResourceException(path)
        return resource.toURI().toString()
    }

    override fun sizeAndLastModified(path: Path): ResourceMetadata {
        val attributes: BasicFileAttributes = Files.readAttributes(Paths.get(path.toString()), BasicFileAttributes::class.java)
        return ResourceMetadata(
            size = attributes.size(),
            lastModified = attributes.lastModifiedTime().toMillis()
        )
    }

    override fun setFileModificationTime(path: Path, timestamp: Long) {
        File(path.toString()).setLastModified(timestamp)
    }

    private fun getResourceAsStream(path: String): InputStream {
        val classLoader = getClassLoader()
        val resource = classLoader.getResourceAsStream(path) ?: run {
            //try to find a font in the android assets
            if (File(path).isFontResource()) {
                classLoader.getResourceAsStream("assets/$path")
            } else null
        } ?: throw MissingResourceException(path)
        return resource
    }

    private fun File.isFontResource(): Boolean {
        return this.parentFile?.name.orEmpty().startsWith("font")
    }

    private fun getClassLoader(): ClassLoader {
        return Thread.currentThread().contextClassLoader ?: this.javaClass.classLoader !!
    }
}