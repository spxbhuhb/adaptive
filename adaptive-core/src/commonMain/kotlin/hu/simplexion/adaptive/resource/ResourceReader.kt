/*
 * Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 *
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package hu.simplexion.adaptive.resource

class MissingResourceException(path: String) : Exception("Missing resource with path: $path")

/**
 * Reads the content of the resource file at the specified path and returns it as a byte array.
 *
 * @param path The path of the file to read in the resource's directory.
 * @return The content of the file as a byte array.
 */
suspend fun readResourceBytes(path: String): ByteArray = DefaultResourceReader.read(path)

/**
 * Provides the platform dependent URI for a given resource path.
 *
 * @param path The path to the file in the resource's directory.
 * @return The URI string of the specified resource.
 */
fun getResourceUri(path: String): String = DefaultResourceReader.getUri(path)

interface ResourceReader {
    suspend fun read(path: String): ByteArray
    suspend fun readPart(path: String, offset: Long, size: Long): ByteArray
    fun getUri(path: String): String
}

expect fun getPlatformResourceReader(): ResourceReader

val DefaultResourceReader = getPlatformResourceReader()