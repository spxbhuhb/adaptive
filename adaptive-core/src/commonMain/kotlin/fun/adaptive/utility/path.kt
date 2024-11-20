package `fun`.adaptive.utility

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.api.adatCompanionOf
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray

fun Path.write(bytes: ByteArray, append: Boolean = false) {
    SystemFileSystem.sink(this, append).buffered().use { it.write(bytes) }
}

fun Path.read(): ByteArray {
    return SystemFileSystem.source(this).buffered().use { it.readByteArray() }
}

fun <A> Path.load(wireFormatProvider: WireFormatProvider, wireFormat: AdatClassWireFormat<A>): A =
    wireFormatProvider.decoder(this.read()).asInstance(wireFormat)

/**
 * Encode [value] with [wireFormatProvider] into a byte array and write it to [this].
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
        .pseudoInstanceStart()
        .rawInstance(value, value.adatCompanion.adatWireFormat as WireFormat<A>)
        .pseudoInstanceEnd()
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

fun Path.ensure(): Path {
    SystemFileSystem.createDirectories(this)
    return this
}

val testPath = Path("./build/tmp/test")