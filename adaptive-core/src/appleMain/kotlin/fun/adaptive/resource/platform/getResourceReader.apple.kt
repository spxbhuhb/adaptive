/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.resource.platform

import `fun`.adaptive.resource.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.io.files.Path
import platform.Foundation.*
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
actual fun getResourceReader(): ResourceReader = object : ResourceReader {

    override suspend fun read(path: String): ByteArray {
        val data = readData(getPathInBundle(path))
        return ByteArray(data.length.toInt()).apply {
            usePinned { memcpy(it.addressOf(0), data.bytes, data.length) }
        }
    }

    override suspend fun readPart(path: String, offset: Long, size: Long): ByteArray {
        val data = readData(getPathInBundle(path), offset, size)
        return ByteArray(data.length.toInt()).apply {
            usePinned { memcpy(it.addressOf(0), data.bytes, data.length) }
        }
    }

    override fun getUri(path: String): String {
        return NSURL.fileURLWithPath(getPathInBundle(path)).toString()
    }

    private fun readData(path: String): NSData {
        return NSFileManager.defaultManager().contentsAtPath(path) ?: throw MissingResourceException(path)
    }

    override fun sizeAndLastModified(path: Path): ResourceMetadata {
        TODO("Not yet implemented")
    }

    override fun setFileModificationTime(path: Path, timestamp: Long) {
        TODO("Not yet implemented")
    }

    private fun readData(path: String, offset: Long, size: Long): NSData {
        val fileHandle = NSFileHandle.fileHandleForReadingAtPath(path) ?: throw MissingResourceException(path)
        fileHandle.seekToOffset(offset.toULong(), null)
        val result = fileHandle.readDataOfLength(size.toULong())
        fileHandle.closeFile()
        return result
    }

    private fun getPathInBundle(path: String): String {
        // todo: support fallback path at bundle root?
        return NSBundle.mainBundle.resourcePath + "/adaptive-resources/" + path
    }

}