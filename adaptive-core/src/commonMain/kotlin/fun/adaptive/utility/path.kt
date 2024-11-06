package `fun`.adaptive.utility

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.api.adatCompanionOf
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.files.SystemTemporaryDirectory
import kotlinx.io.readByteArray

fun Path.write(bytes: ByteArray, append: Boolean = false) {
    SystemFileSystem.sink(this, append).buffered().use { it.write(bytes) }
}

fun Path.read(): ByteArray {
    return SystemFileSystem.source(this).buffered().use { it.readByteArray() }
}

inline fun <reified A : AdatClass> Path.load(wireFormatProvider: WireFormatProvider) : A =
    wireFormatProvider.decoder(this.read()).asInstance(adatCompanionOf<A>().adatWireFormat)

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
inline fun <reified A : AdatClass> Path.save(value : A, wireFormatProvider: WireFormatProvider, withTemporaryFile: Boolean = true) {
    val out = if (withTemporaryFile) {
        Path(this.parent!!, this.name + ".tmp")
    } else {
        this
    }

    wireFormatProvider.encoder()
        .pseudoInstanceStart()
        .rawInstance(value, adatCompanionOf<A>().adatWireFormat)
        .pseudoInstanceEnd()
        .pack()
        .also {
            out.write(it)
        }

    if (withTemporaryFile) {
        SystemFileSystem.atomicMove(out, this)
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