package `fun`.adaptive.lib.util.zip

import kotlinx.coroutines.await
import org.khronos.webgl.ArrayBuffer
import org.w3c.fetch.Response

internal suspend fun deflate(input: dynamic): ArrayBuffer {
    val readableStream = ReadableStream(js("{ start : function(controller) { controller.enqueue(input); controller.close(); } }"))
    val compressionStream = CompressionStream("deflate-raw")
    val compressedStream = readableStream.pipeThrough(compressionStream)

    return Response(compressedStream).arrayBuffer().await()
}

external class ReadableStream(options: dynamic) {
    fun pipeThrough(transform: dynamic, options: dynamic = definedExternally): dynamic
}

external class CompressionStream(algorithm: String) {
    val readable: ReadableStream
}