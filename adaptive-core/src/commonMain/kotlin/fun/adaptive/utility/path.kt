package `fun`.adaptive.utility

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

fun Path.read(): ByteArray {
    return SystemFileSystem.source(this).buffered().use { it.readByteArray() }
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