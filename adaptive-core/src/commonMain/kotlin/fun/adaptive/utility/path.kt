package `fun`.adaptive.utility

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.reflect.CallSiteName
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray

fun Path.write(string: String, append: Boolean = false) {
    SystemFileSystem.sink(this, append).buffered().use { it.write(string.encodeToByteArray()) }
}

fun Path.write(bytes: ByteArray, append: Boolean = false) {
    SystemFileSystem.sink(this, append).buffered().use {
        it.write(bytes)
        it.flush()
    }
}

/**
 * Write out [bytes] into a temporary file (`<name>.tmp`) and the move the temporary file to
 * `this` path. This method ensures that the content of the file written is complete.
 *
 * The move uses `SystemFileSystem.atomicMove` to move the file.
 */
fun Path.safeWrite(bytes: ByteArray) {
    val out = Path(parent !!, "$name.tmp")

    SystemFileSystem.sink(out, append = false).buffered().use {
        it.write(bytes)
        it.flush()
    }

    SystemFileSystem.atomicMove(out, this)
}

fun Path.read(): ByteArray {
    return SystemFileSystem.source(this).buffered().use { it.readByteArray() }
}

fun <A> Path.load(wireFormatProvider: WireFormatProvider, wireFormat: AdatClassWireFormat<A>): A =
    wireFormatProvider.decoder(this.read()).asInstance(wireFormat)

/**
 * Encode [value] with [wireFormatProvider] into a byte array and write it to [path].
 *
 * When [withTemporaryFile] is true (which is the default):
 *
 * - creates a `<this>.tmp` file
 * - writes the bytes into that temporary file
 * - atomically moves the temporary file to `<this>`
 *
 * Reason for this is to avoid empty or partial file content.
 */
fun <A : AdatClass> save(path: Path, value: A, wireFormatProvider: WireFormatProvider, withTemporaryFile: Boolean = true) {
    val out = if (withTemporaryFile) {
        Path(path.parent !!, path.name + ".tmp")
    } else {
        path
    }

    @Suppress("UNCHECKED_CAST")
    wireFormatProvider.encoder()
        .rawInstance(value, value.adatCompanion.adatWireFormat as WireFormat<A>)
        .pack()
        .also {
            out.write(it)
        }

    if (withTemporaryFile) {
        SystemFileSystem.atomicMove(out, path)
    }
}

fun Path.exists() = SystemFileSystem.exists(this)

fun Path.delete() = SystemFileSystem.delete(this)

fun Path.absolute() = SystemFileSystem.resolve(this)

fun Path.list() = SystemFileSystem.list(this)

fun Path.ensure(vararg sub: String): Path {
    val path = Path(this, *sub)
    SystemFileSystem.createDirectories(path)
    return path
}

val testPath = Path("./build/tmp/adaptiveTest")

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

    val testDir = Path(testPath, callSiteName)
    if (testDir.exists()) {
        clean(testDir)
    } else {
        testDir.ensure()
    }

    return testDir
}