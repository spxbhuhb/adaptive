package `fun`.adaptive.lib.util.bytearray

import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.WaitStrategy
import `fun`.adaptive.utility.vmNowSecond
import kotlinx.coroutines.*
import kotlinx.io.files.Path
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class SenderByteArrayQueue(
    val host: String,
    val port: Int,
    path: Path,
    chunkSizeLimit: Long,
    barrier: ByteArray,
    persistDequeue: Boolean = true,
    name: String = path.name,
    val sendTimeout: Int = 2000,
    val failReportInterval: Duration = 1.hours,
    val unknownErrorDelay: Duration = 15.minutes,
    val retryStrategy: WaitStrategy = WaitStrategy(200, 1.seconds, 10.seconds)
) {

    val logger = getLogger("SenderByteArrayQueue.$name")

    val queue = ByteArrayQueue(path, chunkSizeLimit, barrier, persistDequeue)

    val scope = CoroutineScope(Dispatchers.IO)

    private var socket = DatagramSocket().also { it.soTimeout = sendTimeout }

    val isEmpty: Boolean
        get() = queue.isEmpty

    fun enqueue(byteArray: ByteArray) {
        queue.enqueue(byteArray)
    }

    fun start() {
        scope.launch {
            try {
                while (isActive) {
                    send() // sends one packet
                }
            } finally {
                socket.close()
            }
        }
    }

    fun stop() {
        scope.cancel()
    }

    suspend fun send() {
        // Using peek to ensure that the byte array is not lost if send fails.
        val byteArray = queue.peek()

        val address = InetAddress.getByName(host)
        val packet = DatagramPacket(byteArray, byteArray.size, address, port)
        val buffer = ByteArray(byteArray.size)
        val responsePacket = DatagramPacket(buffer, buffer.size)
        var lastFail: Long? = vmNowSecond()

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
            lastFail = null

        } catch (_: SocketTimeoutException) {

            // Not much to do here, we'll try it again after a short delay.
            // This should happen very rarely in real scenarios.
            retryStrategy.wait()

            if (lastFail != null && vmNowSecond() - lastFail > failReportInterval.inWholeSeconds) {
                logger.warning("couldn't send curVal updates in $failReportInterval")
            }

        } catch (e: Exception) {
            logger.error(e)

            try {
                socket.close()
            } catch (e: Exception) {
                logger.warning(e)
            }

            delay(unknownErrorDelay)

            try {
                socket = DatagramSocket().also { it.soTimeout = sendTimeout }
            } catch (e: Exception) {
                logger.error(e)
            }
        }
    }
}