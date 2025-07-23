package `fun`.adaptive.resource.platform

import `fun`.adaptive.resource.MissingResourceException
import `fun`.adaptive.resource.ResourceMetadata
import `fun`.adaptive.resource.ResourceReader
import `fun`.adaptive.resource.WebResourcesConfiguration
import `fun`.adaptive.resource.getResourceUrl
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.io.files.Path
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.w3c.fetch.Response
import org.w3c.files.Blob
import kotlin.js.Promise

actual fun getResourceReader(): ResourceReader = object : ResourceReader {

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

    override fun sizeAndLastModified(path: Path): ResourceMetadata {
        TODO("Not yet implemented, technically could ask the server for the information I think")
    }

    override fun setFileModificationTime(path: Path, timestamp: Long) {
        throw UnsupportedOperationException("File modification time is not supported on JS")
    }

    private suspend fun readAsBlob(path: String): Blob {
        val resPath = WebResourcesConfiguration.getResourcePath(path)
        val response : Response = window.fetch(resPath).await()
        if (! response.ok) {
            throw MissingResourceException(resPath)
        }
        return response.blob().await()
    }

    private suspend fun Blob.asByteArray(): ByteArray {
        //https://developer.mozilla.org/en-US/docs/Web/API/Blob/arrayBuffer
        val buffer = this.arrayBuffer() as Promise<ArrayBuffer>
        return Int8Array(buffer.await()).unsafeCast<ByteArray>()
    }

}