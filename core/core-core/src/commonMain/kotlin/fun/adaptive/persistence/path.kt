package `fun`.adaptive.persistence

import `fun`.adaptive.reflect.CallSiteName
import `fun`.adaptive.resource.ResourceMetadata
import `fun`.adaptive.resource.defaultResourceReader
import `fun`.adaptive.resource.platform.getResourceReader
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.DangerousApi
import `fun`.adaptive.wireformat.json.JsonBufferReader
import `fun`.adaptive.wireformat.json.elements.JsonElement
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import kotlinx.io.readString
import kotlinx.io.readTo
import kotlin.math.min

// ------------------------------------------------------------------------------------
// Writing
// ------------------------------------------------------------------------------------


/**
 * Write [string] to [this] path, encoded in UTF-8.
 *
 * @param string The string content to write
 * @param append When true, append to an existing file instead of overwriting, if the file does not exist, create one. Default is false.
 * @param overwrite When true, overwrite an existing file if it exists. Default is false.
 * @param useTemporaryFile When true, write to a temporary file first then move it to [this] path. Default is false.
 * @throws IllegalStateException if a file exists and neither append nor overwrite is true
 */
fun Path.write(string: String, append: Boolean = false, overwrite: Boolean = false, useTemporaryFile: Boolean = false) {
    write(string.encodeToByteArray(), append = append, overwrite = overwrite, useTemporaryFile = useTemporaryFile)
}

/**
 * Write [bytes] to [this] path.
 *
 * @param bytes The byte array to write
 * @param append When true, append to an existing file instead of overwriting, if the file does not exist, create one. Default is false.
 * @param overwrite When true, overwrite an existing file if it exists. Default is false.
 * @param useTemporaryFile When true, write to a temporary file first then move it to [this] path. Default is false.
 * @throws IllegalStateException if a file exists and neither append nor overwrite is true
 */
fun Path.write(bytes: ByteArray, append: Boolean = false, overwrite: Boolean = false, useTemporaryFile: Boolean = false) {
    val exists = exists()
    check((! exists) || overwrite || append) { "file $this already exists" }

    withTemporary(useTemporaryFile) { out ->
        SystemFileSystem.sink(out, append).buffered().use {
            it.write(bytes)
            it.flush()
            // withTemporary will move the tmp, but atomicMove fails if the file is already there
            if (! append && exists && useTemporaryFile) {
                delete()
            }
        }
    }
}

/**
 * Write a [JsonElement] to [this] path.
 *
 * @param json The JSON element to write
 * @param append When true, append to an existing file instead of overwriting, if the file does not exist, create one. Default is false.
 * @param overwrite When true, overwrite an existing file if it exists. Default is false.
 * @param useTemporaryFile When true, write to a temporary file first then move it to [this] path. Default is false.
 * @throws IllegalStateException if a file exists and neither append nor overwrite is true
 */
fun Path.writeJson(json: JsonElement, append: Boolean = false, overwrite: Boolean = false, useTemporaryFile: Boolean = false) {
    write(json.toString().encodeToByteArray(), append = append, overwrite = overwrite, useTemporaryFile = useTemporaryFile)
}

/**
 * Append the given bytes to the end of the file at [this] path.
 * If the file does not exist, it will be created.
 *
 * @param bytes The byte array to append to the file
 * @throws IllegalStateException if the file cannot be written to
 */
fun Path.append(bytes: ByteArray) {
    write(bytes, append = true, overwrite = false, useTemporaryFile = false)
}

// ------------------------------------------------------------------------------------
// Reading
// ------------------------------------------------------------------------------------

/**
 * Read the entire content of the file at [this] path into a byte array.
 *
 * @return ByteArray containing the file contents
 * @throws IllegalStateException if the file cannot be read
 */
fun Path.read(): ByteArray =
    SystemFileSystem.source(this).buffered().use { it.readByteArray() }

/**
 * Read the entire content of the file at [this] path as a string.
 *
 * @return String containing the file contents
 * @throws IllegalStateException if the file cannot be read
 */
fun Path.readString(): String =
    SystemFileSystem.source(this).buffered().use { it.readString() }

/**
 * Read the content of the file at [this] path and parse it into a low-level
 * [JsonElement] structure.
 *
 * @return  the decoded JSON or null if the file is empty
 *
 * @throws  IllegalArgumentException  if the JSON is invalid
 */
fun Path.readJson(): JsonElement? =
    read().let {
        if (it.isEmpty()) {
            null
        } else {
            JsonBufferReader(it).read()
        }
    }


/**
 * Read a file in chunks and process each chunk with the provided function.
 *
 * @param chunkSize The maximum size of each chunk in bytes. Default is 1MB (1024 * 1024 bytes).
 * @param processFun Function that processes each chunk. Takes the buffer containing the chunk data
 *                   and the actual size of data read into the buffer as parameters.
 * @throws IllegalStateException if the file size cannot be determined
 */
fun Path.process(chunkSize: Long = 1024L * 1024L, processFun: (buffer: ByteArray, size: Int) -> Unit) {
    val fileSize = checkNotNull(SystemFileSystem.metadataOrNull(this)?.size) { "cannot get the size of $this" }
    var remaining = fileSize
    val bufferSize = min(fileSize, chunkSize)

    SystemFileSystem.source(this).buffered().use { source ->
        val buffer = ByteArray(bufferSize.toInt())
        while (remaining > 0) {
            val readSize = min(bufferSize, remaining).toInt()
            source.readTo(buffer, 0, readSize)
            processFun(buffer, readSize)
            remaining -= readSize
        }
    }
}

// ------------------------------------------------------------------------------------
// Random access
// ------------------------------------------------------------------------------------

/**
 * Get a [RandomAccessPersistence] for the file located at the [this] path.
 * The actual instance is platform-dependent, currently only JVM is supported.
 *
 * @param mode  The mode in which the file is opened, use "rw" for read and write.
 */
expect fun Path.getRandomAccess(
    mode: String
): RandomAccessPersistence

// ------------------------------------------------------------------------------------
// Metadata
// ------------------------------------------------------------------------------------

/**
 * Gets the metadata (size and last modification time) for this path.
 *
 * @return A [ResourceMetadata] object containing the size and last modification timestamp
 * 
 * @throws IllegalStateException if the metadata cannot be read
 */
val Path.metadata : ResourceMetadata
    get() = defaultResourceReader.sizeAndLastModified(this)

/**
 * Calculates the SHA-256 hash of the file content at this path.
 *
 * The file is processed in chunks to efficiently handle large files.
 * Each chunk is fed into the SHA-256 digest calculator until the entire
 * file has been processed.
 *
 * @param chunkSize The maximum size of each chunk in bytes. Default is 1MB (1024 * 1024 bytes).
 *
 * @return ByteArray containing the calculated SHA-256 hash value
 *
 * @throws IllegalStateException if the file cannot be read or accessed
 */
fun Path.sha256(chunkSize: Long = 1024L * 1024L): ByteArray {
    val digest = `fun`.adaptive.crypto.sha256()
    process(chunkSize) { buffer, size -> digest.update(buffer, 0, size) }
    return digest.digest()
}

// ------------------------------------------------------------------------------------
// Utility
// ------------------------------------------------------------------------------------


/**
 * Checks if a file or directory exists at [this] path.
 *
 * @return true if the file or directory exists, false otherwise
 */
fun Path.exists() = SystemFileSystem.exists(this)

/**
 * Copy the file at [this] path to [target].
 *
 * @param  overwrite         When `true`, overwrite [target] if it already exists. Default is `false`.
 * @param  keepModified      When `true`, set the last modification time of [target] to the last
 *                           modification time of `this`. Default is `false`.
 * @param  useTemporaryFile  When true, copy into [target]`.tmp` first and then rename it to [target].
 *                           Default is `false`.
 */
fun Path.copy(target: Path, overwrite: Boolean = false, keepModified: Boolean = false, useTemporaryFile: Boolean = false) {
    check(! target.exists() || overwrite) { "file $target already exists" }

    target.withTemporary(useTemporaryFile) { out ->

        SystemFileSystem.sink(out, append = false).buffered().use { sink ->
            SystemFileSystem.source(this).buffered().use { source ->
                source.transferTo(sink)
            }
        }

        if (keepModified) {
            getResourceReader().also {
                it.setFileModificationTime(out, it.sizeAndLastModified(this).lastModified)
            }
        }
    }

}

/**
 * Move this file or directory to the specified [toPath].
 *
 * Attempts to perform an atomic move first. If the atomic move is not supported,
 * falls back to [copy] and [delete] (in this order).
 *
 * @param toPath The destination path where this file/directory should be moved
 */
fun Path.move(toPath: Path) {
    try {
        SystemFileSystem.atomicMove(this, toPath)
    } catch (_: UnsupportedOperationException) {
        copy(toPath, overwrite = true)
        delete()
    }
}

/**
 * Delete [this] file or empty directory.
 *
 * @param mustExists If true, throws an exception if the file does not exist.
 *                   If false, silently returns if a file doesn't exist.
 *
 * @return true if deletion was successful, false otherwise
 */
fun Path.delete(mustExists: Boolean = true) = SystemFileSystem.delete(this, mustExists)

/**
 * Recursively deletes [this] directory and all its contents.
 *
 * WARNING: This is a dangerous operation that will permanently delete all files
 * and subdirectories under this path without confirmation. Use with extreme caution.
 *
 * @throws IllegalStateException if [this] path is not a directory or cannot be accessed
 */
@DangerousApi("deletes all directories and files in this path recursively")
fun Path.deleteRecursively() {
    list().forEach {
        if (SystemFileSystem.metadataOrNull(it)?.isDirectory == true) {
            it.deleteRecursively()
        }
        it.delete()

    }
}

/**
 * Returns the absolute path representation of [this] path.
 *
 * @return A new Path instance representing the absolute path
 */
fun Path.absolute() = SystemFileSystem.resolve(this)

/**
 * Lists all files and directories in [this] directory.
 *
 * @return A sequence of Path objects representing directory contents
 *
 * @throws IllegalStateException if this path is not a directory or cannot be accessed
 */
fun Path.list() = SystemFileSystem.list(this)

/**
 * Resolves one or more path parts against [this] path.
 *
 * @param sub Variable number of path parts to resolve.
 *
 * @return A new [Path] with the resolved parts
 */
fun Path.resolve(vararg sub: String) = Path(this, *sub)

/**
 * Checks if [this] path points to a directory.
 *
 * @return true if this path represents a directory, false otherwise
 */
val Path.isDirectory
    get() = SystemFileSystem.metadataOrNull(this)?.isDirectory == true

/**
 * Ensures that a [this] path exists, creating it and any necessary parent directories if they don't exist.
 *
 * @param sub Variable number of path parts to append to this path
 * @return The resolved Path after ensuring its existence
 */
fun Path.ensure(vararg sub: String): Path {
    val path = Path(this, *sub)
    SystemFileSystem.createDirectories(path)
    return path
}

/**
 * Checks if a directory is empty or if a file size is zero.
 *
 * @return true if the directory has no files in it or if the file size is 0,
 *         false otherwise
 */
fun Path.isEmpty(): Boolean {
    if (isDirectory) {
        return list().firstOrNull { it.name != "." && it.name != ".." } == null
    } else {
        return (SystemFileSystem.metadataOrNull(this)?.size ?: 0L) == 0L
    }
}

/**
 * Calls [process] for all files in [this] directory and for all files
 * in all subdirectories of [this] (recursively).
 *
 * [process] is not called for directories, only for files.
 *
 * @param  ignoreHidden  Ignore files with name starting with `.`. Default is `true`.
 */
fun Path.walkFiles(ignoreHidden: Boolean = true, process: (Path) -> Unit) {
    for (file in list()) {
        if (SystemFileSystem.metadataOrNull(file)?.isDirectory == true) {
            file.walkFiles(ignoreHidden, process)
        } else {
            if (file.name.startsWith(".") && ignoreHidden) continue
            process(file)
        }
    }
}

// ------------------------------------------------------------------------------------
// Synchronization
// ------------------------------------------------------------------------------------

/**
 * Check if the file at [this] path is the same as the [other] by size and last modification.
 *
 * True, when **ALL** these conditions are true:
 *
 * - [this] exists
 * - [other] exists
 * - sizes are the same
 * - last modification time is the same
 */
fun Path.equalsBySizeAndLastModification(other: Path): Boolean {
    if (! this.exists()) return false
    if (! other.exists()) return false

    val thisData = defaultResourceReader.sizeAndLastModified(this)
    val otherData = defaultResourceReader.sizeAndLastModified(other)

    return (thisData == otherData)
}


/**
 * Synchronizes the content of the directory at [this] path **FROM** the content of the [other].
 *
 * Two files are considered the same when **ALL** these conditions are true:
 *
 * - sizes are the same
 * - last modification time is the same
 *
 * @param createThis Create directories as needed.
 * @param remove     When true, files that exists in [this], but not in [other] are removed from [this]. Default is false.
 *
 * @return  True if anything has been changed, false if there have not been any changes.
 */
@DangerousApi("deletes files recursively in this path if they don't exists in the other")
fun Path.syncBySizeAndLastModification(other: Path, createThis: Boolean = true, remove: Boolean = false): Boolean {
    if (! this.exists()) SystemFileSystem.createDirectories(this)

    var changed = false

    val thisFiles = this.list().toMutableSet()

    for (fromFile in other.list()) {
        val thisFile = this.resolve(fromFile.name)
        thisFiles -= thisFile

        if (fromFile.isDirectory) {
            changed = thisFile.syncBySizeAndLastModification(fromFile, createThis, remove) || changed
        } else {
            if (thisFile.equalsBySizeAndLastModification(fromFile)) continue
            fromFile.copy(thisFile, overwrite = true, keepModified = true)
            changed = true
        }
    }

    if (remove) {
        for (thisFile in thisFiles) {
            try {
                if (thisFile.isDirectory) {
                    thisFile.deleteRecursively()
                } else {
                    thisFile.delete()
                }
            } catch (ex: Exception) {
                // FIXME use a proper logger for resource generation
                println("WARNING: could not delete $thisFile")
                throw ex
            }
            changed = true
        }
    }

    return changed
}

// ------------------------------------------------------------------------------------
// Testing
// ------------------------------------------------------------------------------------

/**
 * The path to the directory where files generated by unit tests are stored.
 *
 * Default is `./build/adaptive/test/${GlobalRuntimeContext.platform.name}`.
 *
 */
var globalTestPath = Path("./build/adaptive/test/${GlobalRuntimeContext.platform.name}")

/**
 * Get the path to a directory where a specific unit test can store its temporary files.
 *
 * - Composes a unique, fully qualified path for a unit test.
 * - Creates the directory if it does not exist.
 * - Deletes the content of the directory if it exists.
 *
 * When used with the plugin active (which is almost everywhere, except core, gradle-plugin and kotlin-plugin),
 * [callSiteName] is the fully qualified name of the function that calls [clearedTestPath].
 *
 * For example:
 *
 * ```kotlin
 * package some.test.pkg
 *
 * class SomeTest {
 *     @Test
 *     fun someTest() {
 *         val testDir = clearedTestPath()
 *     }
 * }
 * ```
 *
 * Creates the directory:
 *
 * `./build/adaptive/test/JVM/some.test.pkg.SomeTest.someTest`
 *
 * On iOS the tests are put into a directory like this:
 *
 * `/Users/tiz/Library/Developer/CoreSimulator/Devices/D6554821-AD5A-46BC-9E25-A83F1BA38C9E/data/build/adaptive/test/iOS/`
 */
@CallSiteName
@OptIn(DangerousApi::class) // this is fine, confined into testPath
fun clearedTestPath(callSiteName: String = "unknown", vararg scope: String): Path {

    check(".." !in callSiteName) { "'..' is not allowed in the path: $callSiteName" }

    val testDir = Path(globalTestPath, callSiteName.removeSuffix(".<anonymous>")).resolve(*scope)

    if (testDir.exists()) {
        testDir.deleteRecursively()
    } else {
        testDir.ensure()
    }

    return testDir
}

// ------------------------------------------------------------------------------------
// Helper functions
// ------------------------------------------------------------------------------------

private fun Path.withTemporary(useTemporary: Boolean, block: (out: Path) -> Unit) {

    val out = if (useTemporary) {
        Path(parent !!, "$name.tmp")
    } else {
        this
    }

    try {

        block(out)

        if (useTemporary) {
            SystemFileSystem.atomicMove(out, this)
        }
    } catch (ex: Exception) {
        try {
            if (useTemporary && out.exists()) {
                out.delete()
            }
        } catch (ex2: Exception) {
            ex.addSuppressed(ex2)
        }
        throw ex
    }

}