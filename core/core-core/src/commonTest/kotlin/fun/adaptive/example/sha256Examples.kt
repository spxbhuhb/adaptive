@file:OptIn(ExperimentalStdlibApi::class)

package `fun`.adaptive.example

import `fun`.adaptive.crypto.sha256
import `fun`.adaptive.persistence.getRandomAccess
import `fun`.adaptive.persistence.sha256
import kotlinx.io.files.Path

fun sha256Example() {
    val digest = sha256()
    digest.update("Hello World!".encodeToByteArray())
    val digestBytes = digest.digest()

    println(digestBytes.toHexString())
}

fun sha256PathExample() {
    val path = Path("tmp/test.txt")
    val digestBytes = path.sha256()

    println(digestBytes.toHexString())
}

fun sha256RandomAccessExample() {
    val randomAccess = Path("tmp/test.txt").getRandomAccess("r")
    val digestBytes = randomAccess.sha256()

    println(digestBytes.toHexString())
}