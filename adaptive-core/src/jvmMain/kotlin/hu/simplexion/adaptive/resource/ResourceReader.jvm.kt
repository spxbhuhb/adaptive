/*
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */
package hu.simplexion.adaptive.resource

import java.io.File
import java.io.InputStream

actual fun getPlatformResourceReader(): ResourceReader = object : ResourceReader {
    override suspend fun read(path: String): ByteArray {
        val resource = getResourceAsStream(path)
        return resource.readBytes()
    }

    override suspend fun readPart(path: String, offset: Long, size: Long): ByteArray {
        val resource = getResourceAsStream(path)
        val result = ByteArray(size.toInt())
        resource.use { input ->
            input.skip(offset)
            input.read(result, 0, size.toInt())
        }
        return result
    }

    override fun getUri(path: String): String {
        val classLoader = getClassLoader()
        val resource = classLoader.getResource(path) ?: run {
            //try to find a font in the android assets
            if (File(path).isFontResource()) {
                classLoader.getResource("assets/$path")
            } else null
        } ?: throw MissingResourceException(path)
        return resource.toURI().toString()
    }

    private fun getResourceAsStream(path: String): InputStream {
        val classLoader = getClassLoader()
        val resource = classLoader.getResourceAsStream(path) ?: run {
            //try to find a font in the android assets
            if (File(path).isFontResource()) {
                classLoader.getResourceAsStream("assets/$path")
            } else null
        } ?: throw MissingResourceException(path)
        return resource
    }

    private fun File.isFontResource(): Boolean {
        return this.parentFile?.name.orEmpty().startsWith("font")
    }

    private fun getClassLoader(): ClassLoader {
        return Thread.currentThread().contextClassLoader ?: this.javaClass.classLoader!!
    }
}