package `fun`.adaptive.utility

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray

fun Path.write(bytes: ByteArray) {
    SystemFileSystem.sink(this).buffered().use { it.write(bytes) }
}

fun Path.read(): ByteArray {
    return SystemFileSystem.source(this).buffered().use { it.readByteArray() }
}

fun Path.exists() = SystemFileSystem.exists(this)

val testPath = Path("./build/tmp/test")

fun ensureTestPath() {
    SystemFileSystem.createDirectories(testPath)
}