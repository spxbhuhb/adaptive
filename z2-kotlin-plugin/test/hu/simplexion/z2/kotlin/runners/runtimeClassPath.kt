package hu.simplexion.z2.kotlin.runners

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.getLastModifiedTime
import kotlin.io.path.name
import kotlin.streams.toList

fun runtimeClassPath() : List<File> =
    listOf(
        Files.list(Paths.get("../z2-core/build/libs/"))
            .filter { it.name.startsWith("z2-core-") && it.name.endsWith("-all.jar") }
            .toList()
            .maxBy { it.getLastModifiedTime() }
            .toFile()
    )