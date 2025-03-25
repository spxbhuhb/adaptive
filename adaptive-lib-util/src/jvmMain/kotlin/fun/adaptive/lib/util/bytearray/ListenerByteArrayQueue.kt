package `fun`.adaptive.lib.util.bytearray

import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.WaitStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.io.files.Path
import java.net.DatagramPacket
import java.net.DatagramSocket
import kotlin.time.Duration.Companion.seconds

class ListenerByteArrayQueue(
    port: Int,
    path: Path,
    chunkSizeLimit: Long,
    barrier: ByteArray,
    persistEnqueue: Boolean = true,
    val sizeLimit: Int = 1000,
    val retryStrategy: WaitStrategy = WaitStrategy(200, 1.seconds, 10.seconds),
    name: String = path.name
) {
    val logger = getLogger("ListenerByteArrayQueue.$name")

    val queue = ByteArrayQueue(path, chunkSizeLimit, barrier, persistEnqueue)

    val scope = CoroutineScope(Dispatchers.IO)
    private val socket = DatagramSocket(port)

    fun start() {
        scope.launch {
            try {
                listen()
            } finally {
                socket.close()
            }
        }
    }

    // FIXME replace the quick and dirty, non-interruptible byte array queue listener
    // https://liakh-aliaksandr.medium.com/java-sockets-i-o-blocking-non-blocking-and-asynchronous-fb7f066e4ede
    private suspend fun listen() {
        val buffer = ByteArray(sizeLimit)
        val packet = DatagramPacket(buffer, buffer.size)

        while (scope.isActive) {
            try {
                socket.receive(packet)
                val data = packet.data.copyOf(packet.length)

                queue.enqueue(data)

                socket.send(
                    DatagramPacket(data, data.size, packet.address, packet.port)
                )

                retryStrategy.reset()

            } catch (e: Exception) {
                logger.error(e)
                retryStrategy.wait()
            }
        }
    }
}