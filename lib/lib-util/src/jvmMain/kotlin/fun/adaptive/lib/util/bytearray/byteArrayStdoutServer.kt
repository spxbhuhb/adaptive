package `fun`.adaptive.lib.util.bytearray

import `fun`.adaptive.file.clearedTestPath
import kotlinx.coroutines.runBlocking

fun main() {
    val listener = ListenerByteArrayQueue(12675, clearedTestPath(), 1024L * 1024L, byteArrayOf(0x0A, 0x0A, 0x0A, 0x0A))
    listener.start()

    runBlocking {
        while (true) {
            val message = listener.queue.dequeue()
            println(message.decodeToString())
        }
    }
}