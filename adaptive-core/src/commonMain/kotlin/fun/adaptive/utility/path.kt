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

// ------------------------------------------------------------------------------------
// Writing
// ------------------------------------------------------------------------------------

/**
 * Write [string] to `this` in UTF-8.
 */
fun Path.write(string: String, append: Boolean = false, useTemporaryFile: Boolean = false) {
    write(string.encodeToByteArray(), append, useTemporaryFile)
}

fun Path.write(bytes: ByteArray, append: Boolean = false, useTemporaryFile: Boolean = false) {
    withTemporary(useTemporaryFile) { out ->
        SystemFileSystem.sink(out, append).buffered().use {
            it.write(bytes)
            it.flush()
        }
    }
}

/**
 * Encode [value] with [wireFormatProvider] into a byte array and write it to `this`.
 */
fun <A : AdatClass> Path.save(value: A, wireFormatProvider: WireFormatProvider, useTemporaryFile: Boolean = true) {
    withTemporary(useTemporaryFile) { out ->
        @Suppress("UNCHECKED_CAST")
        wireFormatProvider.encoder()
            .rawInstance(value, value.adatCompanion.adatWireFormat as WireFormat<A>)
            .pack()
            .also {
                out.write(it)
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

fun <A> Path.load(wireFormatProvider: WireFormatProvider, wireFormat: AdatClassWireFormat<A>): A =
    wireFormatProvider.decoder(this.read()).asInstance(wireFormat)

// ------------------------------------------------------------------------------------
// Utility
// ------------------------------------------------------------------------------------

fun Path.exists() = SystemFileSystem.exists(this)

fun Path.delete() = SystemFileSystem.delete(this)

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
 * Copy `this` file to [target].
 *
 * @param  override          When `true`, override [target] if it already exists. Default is `false`.
 * @param  keepModified      When `true`, set the last modification time of [target] to the last
 *                           modification time of `this`. Default is `false`.
 * @param  useTemporaryFile  When true, copy into [target]`.tmp` first and then rename it to [target].
 *                           Default is `false`.
 */
fun Path.copy(target: Path, override: Boolean = false, keepModified: Boolean = false, useTemporaryFile: Boolean = false) {
    // TODO I'm not sure Path.copy is right
    check(! target.exists() || override) { "file $target already exists" }

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
            fromFile.copy(thisFile, override = true, keepModified = true)
            changed = true
        }
    }

    if (remove) {
        for (thisFile in thisFiles) {
            thisFile.delete()
            changed = true
        }
    }

    return changed
}

// ------------------------------------------------------------------------------------
// Testing
// ------------------------------------------------------------------------------------

val testPath = Path("./build/adaptive/test")

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
 * `./build/adaptive/test/some.test.pkg.SomeTest.someTest`
 */
@CallSiteName
fun clearedTestPath(callSiteName: String = "unknown"): Path {

    fun clean(dir: Path) {
        dir.list().forEach {

            check(it.absolute().toString().startsWith(testPath.absolute().toString()))

            if (SystemFileSystem.metadataOrNull(it)?.isDirectory == true) {
                clean(it)
            } else {
                it.delete()
            }
        }
    }

    val testDir = Path(testPath, callSiteName.removeSuffix(".<anonymous>"))
    if (testDir.exists()) {
        clean(testDir)
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