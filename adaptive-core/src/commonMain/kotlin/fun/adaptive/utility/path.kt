package `fun`.adaptive.utility

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.reflect.CallSiteName
import `fun`.adaptive.resource.defaultResourceReader
import `fun`.adaptive.resource.platform.getResourceReader
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import kotlinx.io.readString

// ------------------------------------------------------------------------------------
// Writing
// ------------------------------------------------------------------------------------

/**
 * Write [string] to `this` in UTF-8.
 */
fun Path.write(string: String, append: Boolean = false, overwrite: Boolean = false, useTemporaryFile: Boolean = false) {
    write(string.encodeToByteArray(), append = append, overwrite = overwrite, useTemporaryFile = useTemporaryFile)
}

fun Path.write(bytes: ByteArray, append: Boolean = false, overwrite: Boolean = false, useTemporaryFile: Boolean = false) {
    val exists = exists()
    check((! exists) || overwrite) { "file $this already exists" }

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
 * Encode [value] with [wireFormatProvider] into a byte array and write it to `this`.
 */
fun <A : AdatClass> save(path: Path, value: A, wireFormatProvider: WireFormatProvider, overwrite: Boolean = false, useTemporaryFile: Boolean = true) {
    check(! path.exists() || overwrite) { "file $path already exists" }

    @Suppress("UNCHECKED_CAST")
    wireFormatProvider.encoder()
        .rawInstance(value, value.adatCompanion.adatWireFormat as WireFormat<A>)
        .pack()
        .also {
            path.write(it, append = false, overwrite = overwrite, useTemporaryFile = useTemporaryFile)
        }
}

/**
 * Copy `this` file to [target].
 *
 * @param  overwrite         When `true`, overwrite [target] if it already exists. Default is `false`.
 * @param  keepModified      When `true`, set the last modification time of [target] to the last
 *                           modification time of `this`. Default is `false`.
 * @param  useTemporaryFile  When true, copy into [target]`.tmp` first and then rename it to [target].
 *                           Default is `false`.
 */
fun Path.copy(target: Path, overwrite: Boolean = false, keepModified: Boolean = false, useTemporaryFile: Boolean = false) {
    // TODO I'm not sure Path.copy is right
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

@Deprecated("Use write(bytes, useTemporaryFile = true) instead.")
fun Path.safeWrite(bytes: ByteArray) {
    val out = Path(parent !!, "$name.tmp")

    SystemFileSystem.sink(out, append = false).buffered().use {
        it.write(bytes)
        it.flush()
    }

    SystemFileSystem.atomicMove(out, this)
}

// ------------------------------------------------------------------------------------
// Reading
// ------------------------------------------------------------------------------------

fun Path.read(): ByteArray =
    SystemFileSystem.source(this).buffered().use { it.readByteArray() }

fun Path.readString(): String =
    SystemFileSystem.source(this).buffered().use { it.readString() }

fun <A> Path.load(wireFormatProvider: WireFormatProvider, wireFormat: AdatClassWireFormat<A>): A =
    wireFormatProvider.decoder(this.read()).asInstance(wireFormat)

// ------------------------------------------------------------------------------------
// Utility
// ------------------------------------------------------------------------------------

fun Path.exists() = SystemFileSystem.exists(this)

fun Path.delete() = SystemFileSystem.delete(this)

@DangerousApi("deletes all directories and files in this path recursively")
fun Path.deleteRecursively() {
    list().forEach {
        if (SystemFileSystem.metadataOrNull(it)?.isDirectory == true) {
            it.deleteRecursively()
        } else {
            it.delete()
        }
    }
}

fun Path.absolute() = SystemFileSystem.resolve(this)

fun Path.list() = SystemFileSystem.list(this)

fun Path.resolve(vararg sub: String) = Path(this, *sub)

val Path.isDirectory
    get() = SystemFileSystem.metadataOrNull(this)?.isDirectory == true

/**
 * Make sure the path composed of this and [sub] exists by creating it if necessary.
 */
fun Path.ensure(vararg sub: String): Path {
    val path = Path(this, *sub)
    SystemFileSystem.createDirectories(path)
    return path
}

/**
 * Call [process] for all files in `this` directory and for all files
 * in all subdirectories of `this` (recursively).
 *
 * [process] is not called for directories, only for files.
 *
 * @param  ignoreHidden  Ignore files with name starting with `.`.
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

/**
 * Call [map] for all files in `this` directory and all files in all
 * subdirectories of `this` (recursively).
 *
 * Collect the return values of [map] into a list.
 *
 * @return  The list of the mapped files.
 */
fun <T> Path.flatMapFiles(map: (Path) -> T) =
    mutableListOf<T>().also { flatMapFiles(map, it) }

private fun <T> Path.flatMapFiles(map: (Path) -> T, out: MutableList<T>) {
    for (file in list()) {
        if (SystemFileSystem.metadataOrNull(file)?.isDirectory == true) {
            file.flatMapFiles(map, out)
        } else {
            out += map(file)
        }
    }
}

// ------------------------------------------------------------------------------------
// Synchronization
// ------------------------------------------------------------------------------------

/**
 * Check if `this` file is the same as [other] by size and last modification.
 *
 * True, when **ALL** these conditions are true:
 *
 * - `this` exists
 * - `other` exists
 * - sizes are the same
 * - last modification time is the same
 *
 * Uses [defaultResourceReader] to read size and last modification.
 */
fun Path.equalsBySizeAndLastModification(other: Path): Boolean {
    if (! this.exists()) return false
    if (! other.exists()) return false

    val thisData = defaultResourceReader.sizeAndLastModified(this)
    val otherData = defaultResourceReader.sizeAndLastModified(other)

    return (thisData == otherData)
}


/**
 * Synchronize the content of `this` directory **FROM** the content of the [other].
 *
 * Two files are considered the same when **ALL** these conditions are true:
 *
 * - sizes are the same
 * - last modification time is the same
 *
 * @param createThis Create directories as needed.
 * @param remove     When true, files that exists in `this`, but not in [other] are removed from `this`.
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

val testPath = Path("./build/adaptive/test/${platformType}")

/**
 * - Compose a unique, fully qualified path for a unit test.
 * - Create the directory if not exists.
 * - Delete the content of the directory if exists.
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
fun clearedTestPath(callSiteName: String = "unknown"): Path {

    check(".." !in callSiteName) { "'..' is not allowed in the path: $callSiteName" }

    val testDir = Path(testPath, callSiteName.removeSuffix(".<anonymous>"))

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

fun Path.withTemporary(useTemporary: Boolean, block: (out: Path) -> Unit) {

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