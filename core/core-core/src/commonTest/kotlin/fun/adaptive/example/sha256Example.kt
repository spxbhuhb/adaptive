package `fun`.adaptive.example

import `fun`.adaptive.crypto.sha256

fun sha256Example() {
    val digest = sha256()
    digest.update("Hello World!".encodeToByteArray())
    println(digest.digest().decodeToString())
}