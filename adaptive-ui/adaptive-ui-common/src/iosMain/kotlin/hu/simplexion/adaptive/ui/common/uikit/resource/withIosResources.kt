/*
 * Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 *
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package hu.simplexion.adaptive.ui.common.uikit.resource

import hu.simplexion.adaptive.resource.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.*
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle
import platform.posix.memcpy

fun withIosResources() {
    defaultResourceEnvironmentOrNull = getSystemEnvironment()
    defaultResourceReaderOrNull = getPlatformResourceReader()
}

private fun getSystemEnvironment(): ResourceEnvironment {
    val locale = NSLocale.preferredLanguages.firstOrNull()
        ?.let { NSLocale(it as String) }
        ?: NSLocale.currentLocale

    val languageCode = locale.languageCode
    val regionCode = locale.objectForKey(NSLocaleCountryCode) as? String
    val mainScreen = UIScreen.mainScreen
    val isDarkTheme = mainScreen.traitCollection().userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark

    //there is no an API to get a physical screen size and calculate a real DPI
    val density = mainScreen.scale.toFloat()
    return ResourceEnvironment(
        language = LanguageQualifier(languageCode),
        region = RegionQualifier(regionCode.orEmpty()),
        theme = ThemeQualifier.selectByValue(isDarkTheme),
        density = DensityQualifier.selectByDensity(density)
    )
}


@OptIn(ExperimentalForeignApi::class)
private fun getPlatformResourceReader(): ResourceReader = object : ResourceReader {

    override suspend fun read(path: String): ByteArray {
        val data = readData(getPathInBundle(path))
        return ByteArray(data.length.toInt()).apply {
            usePinned { memcpy(it.addressOf(0), data.bytes, data.length) }
        }
    }

    override suspend fun readPart(path: String, offset: Long, size: Long): ByteArray {
        val data = readData(getPathInBundle(path), offset, size)
        return ByteArray(data.length.toInt()).apply {
            usePinned { memcpy(it.addressOf(0), data.bytes, data.length) }
        }
    }

    override fun getUri(path: String): String {
        return NSURL.fileURLWithPath(getPathInBundle(path)).toString()
    }

    private fun readData(path: String): NSData {
        return NSFileManager.defaultManager().contentsAtPath(path) ?: throw MissingResourceException(path)
    }

    private fun readData(path: String, offset: Long, size: Long): NSData {
        val fileHandle = NSFileHandle.fileHandleForReadingAtPath(path) ?: throw MissingResourceException(path)
        fileHandle.seekToOffset(offset.toULong(), null)
        val result = fileHandle.readDataOfLength(size.toULong())
        fileHandle.closeFile()
        return result
    }

    private fun getPathInBundle(path: String): String {
        // todo: support fallback path at bundle root?
        return NSBundle.mainBundle.resourcePath + "/adaptive-resources/" + path
    }

}