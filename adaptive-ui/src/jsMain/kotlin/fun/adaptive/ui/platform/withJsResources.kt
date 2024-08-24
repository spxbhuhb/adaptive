/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.platform

import `fun`.adaptive.resource.*
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.w3c.files.Blob
import kotlin.js.Promise

fun withJsResources() {
    defaultResourceEnvironmentOrNull = getSystemEnvironment()
    defaultResourceReaderOrNull = getPlatformResourceReader()
}

private external class Intl {
    class Locale(locale: String) {
        val language: String
        val region: String
    }
}

private fun getSystemEnvironment(): ResourceEnvironment {
    val locale = Intl.Locale(window.navigator.language)
    val isDarkTheme = window.matchMedia("(prefers-color-scheme: dark)").matches
    //96 - standard browser DPI https://developer.mozilla.org/en-US/docs/Web/API/Window/devicePixelRatio
    val dpi: Int = (window.devicePixelRatio * 96).toInt()
    return ResourceEnvironment(
        language = LanguageQualifier(locale.language),
        region = RegionQualifier(locale.region),
        theme = ThemeQualifier.selectByValue(isDarkTheme),
        density = DensityQualifier.selectByValue(dpi)
    )
}

private fun getPlatformResourceReader(): ResourceReader = object : ResourceReader {

    override suspend fun read(path: String): ByteArray {
        return readAsBlob(path).asByteArray()
    }

    override suspend fun readPart(path: String, offset: Long, size: Long): ByteArray {
        val part = readAsBlob(path).slice(offset.toInt(), (offset + size).toInt())
        return part.asByteArray()
    }

    override fun getUri(path: String): String {
        val location = window.location
        return getResourceUrl(location.origin, location.pathname, path)
    }

    private suspend fun readAsBlob(path: String): Blob {
        val resPath = WebResourcesConfiguration.getResourcePath(path)
        val response = window.fetch(resPath).await()
        if (!response.ok) {
            throw MissingResourceException(resPath)
        }
        return response.blob().await()
    }

    private suspend fun Blob.asByteArray(): ByteArray {
        //https://developer.mozilla.org/en-US/docs/Web/API/Blob/arrayBuffer
        val buffer = asDynamic().arrayBuffer() as Promise<ArrayBuffer>
        return Int8Array(buffer.await()).unsafeCast<ByteArray>()
    }
}