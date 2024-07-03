/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.runners

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.name

fun runtimeClassPath() : List<File> {
    val result = mutableListOf<File>()

    Files.list(Paths.get("../adaptive-core/build/libs/"))
        .filter { it.name.startsWith("adaptive-core-jvm-") && it.name.endsWith(".jar") }
        .forEach { result += it.toFile() }

    check(result.isNotEmpty()) { "Runtime JAR does not exist. Please run :adaptive-core:build" }

    val latest = result.maxBy { it.lastModified() }

    result.clear()
    result += latest

    result += File(System.getProperty("adaptive.kotlin.test.kotlinx-coroutines-core"))
    result += File(System.getProperty("adaptive.kotlin.test.kotlinx-datetime"))
    result += File(System.getProperty("adaptive.org.jetbrains.exposed-core"))

    return result
}