package `fun`.adaptive.lib.util.bytearray

import `fun`.adaptive.utility.WaitStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.io.files.Path
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException
import kotlin.time.Duration.Companion.seconds

class SenderByteArrayQueue(
    val host: String,
    val port: Int,
    path: Path,
    chunkSizeLimit: Long,
    barrier: ByteArray,
    persistDequeue: Boolean = true,
    sendTimeout: Int = 2000,
    val retryStrategy: WaitStrategy = WaitStrategy(200, 1.seconds, 10.seconds)
) {

    val queue = ByteArrayQueue(path, chunkSizeLimit, barrier, persistDequeue)

    val scope = CoroutineScope(Dispatchers.IO)

    private val socket = DatagramSocket().also { it.soTimeout = sendTimeout }

    fun enqueue(byteArray: ByteArray) {
        queue.enqueue(byteArray)
    }

    fun start() {
        scope.launch {
            try {
                while (isActive) {
                    send()
                }
            } finally {
                socket.close()
            }
        }
    }

    suspend fun send() {
        // Using peek to ensure that the byte array is not lost if send fails.
        val byteArray = queue.peek()

        val address = InetAddress.getByName(host)
        val packet = DatagramPacket(byteArray, byteArray.size, address, port)
        val buffer = ByteArray(byteArray.size)
        val responsePacket = DatagramPacket(buffer, buffer.size)

        while (true) {
            try {
                socket.send(packet)
                socket.receive(responsePacket)

                while (! responsePacket.data.contentEquals(byteArray)) {
                    // We've got some other data, it might be a response to a previous send.
                    // Let's try to read until we get the proper response. Worst case is that
                    // receive goes to timeout. Then - as we used peek, not dequeue, the next
                    // run will try to send the same byte array again.
                    socket.receive(responsePacket)
                }

                // At this point we've got a correct response. We can remove this byte array
                // from the queue safely as it has been acknowledged. If we've got some
                // incorrect responses, the loop above will finish with an exception.

                queue.dequeue()
                retryStrategy.reset()

                break

            } catch (_: SocketTimeoutException) {
                // Not much to do here, we'll try it again after a short delay.
                // This should happen very rarely in real scenarios.
                retryStrategy.wait()
            }
        }
    }
}