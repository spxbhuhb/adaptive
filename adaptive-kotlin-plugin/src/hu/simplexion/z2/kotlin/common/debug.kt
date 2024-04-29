package hu.simplexion.z2.kotlin.common

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

var enabled = false

val separator = "\n" + "=".padEnd(80, '=') + "\n"

fun debug(message: () -> Any?) {
    if (! enabled) return
    Files.write(Paths.get("/Users/tiz/Desktop/z2-plugin-debug.txt"), "${message()}\n".encodeToByteArray(), StandardOpenOption.APPEND, StandardOpenOption.CREATE)
}