package `fun`.adaptive.lib.util.bytearray

import `fun`.adaptive.file.clearedTestPath
import `fun`.adaptive.file.resolve
import `fun`.adaptive.utility.waitForReal
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.time.Duration.Companion.seconds

class UdpByteArrayTest {

    @Test
    fun testSenderAndListener() = runTest {
        val port = 5555
        val path = clearedTestPath()
        val chunkSizeLimit = 1024L
        val barrier = byteArrayOf(0x0)

        val listener = ListenerByteArrayQueue(port, path.resolve("listener"), chunkSizeLimit, barrier)
        listener.start()

        val sender = SenderByteArrayQueue("127.0.0.1", port, path.resolve("sender"), chunkSizeLimit, barrier)
        val testData = "Hello, UDP!".toByteArray()

        sender.enqueue(testData)
        sender.start()

        waitForReal(1.seconds) { listener.queue.isNotEmpty }

        // Verify that the listener received the correct data
        val receivedData = listener.queue.peek()
        assertContentEquals(testData, receivedData)
    }

    @Test
    fun testMultipleMessages() = runTest {
        val port = 6666
        val path = clearedTestPath()
        val chunkSizeLimit = 1024L
        val barrier = byteArrayOf(0x0)

        val listener = ListenerByteArrayQueue(port, path.resolve("listener"), chunkSizeLimit, barrier)
        listener.start()

        val sender = SenderByteArrayQueue("127.0.0.1", port, path.resolve("sender"), chunkSizeLimit, barrier)
        val messages = listOf("Message 1", "Message 2", "Message 3").map { it.toByteArray() }

        messages.forEach { sender.enqueue(it) }
        sender.start()

        waitForReal(2.seconds) { sender.queue.isEmpty }

        messages.forEach { message ->
            val receivedData = listener.queue.dequeue()
            assertContentEquals(message, receivedData)
        }
    }

    @Test
    fun testSenderRestart() = runTest {
        val port = 7777
        val path = clearedTestPath()
        val chunkSizeLimit = 1024L
        val barrier = byteArrayOf(0x0)

        val listener = ListenerByteArrayQueue(port, path.resolve("listener"), chunkSizeLimit, barrier)
        listener.start()

        var sender = SenderByteArrayQueue("127.0.0.1", port, path.resolve("sender"), chunkSizeLimit, barrier)
        val testData = "Resilient Message".toByteArray()

        sender.enqueue(testData)
        sender.start()

        waitForReal(1.seconds) { listener.queue.isNotEmpty }

        // Restart sender
        sender = SenderByteArrayQueue("127.0.0.1", port, path.resolve("sender"), chunkSizeLimit, barrier)
        sender.enqueue(testData)
        sender.start()

        waitForReal(2.seconds) { sender.queue.isEmpty }

        assertContentEquals(testData, listener.queue.dequeue())
        assertContentEquals(testData, listener.queue.dequeue())
    }

// this test fails with "Address already in use (Bind failed)", no idea how to test it automatically
//    @Test
//    fun testListenerRestart() = runTest(timeout = 40.seconds) {
//        val port = 8888
//        val path = clearedTestPath()
//        val chunkSizeLimit = 1024L
//        val barrier = byteArrayOf(0x0)
//
//        var listener = ListenerByteArrayQueue(port, path.resolve("listener"), chunkSizeLimit, barrier)
//        listener.start()
//
//        val sender = SenderByteArrayQueue("127.0.0.1", port, path.resolve("sender"), chunkSizeLimit, barrier)
//        val testData = "Persistent Message".toByteArray()
//
//        sender.enqueue(testData)
//        sender.start()
//
//        waitForReal(1.seconds) { listener.queue.isNotEmpty }
//        assertContentEquals(testData, listener.queue.dequeue())
//
//        listener.scope.cancel()
//
//        waitForReal(30.seconds)
//
//        // Restart listener
//        listener = ListenerByteArrayQueue(port, path.resolve("listener"), chunkSizeLimit, barrier)
//        listener.start()
//
//        sender.enqueue(testData)
//
//        waitForReal(2.seconds) { listener.queue.isNotEmpty }
//        assertContentEquals(testData, listener.queue.dequeue())
//    }

    @Test
    fun testLargePayload() = runTest {
        val port = 9999
        val path = clearedTestPath()
        val chunkSizeLimit = 65535L // Maximum UDP packet size
        val barrier = byteArrayOf(0x0)

        val listener = ListenerByteArrayQueue(port, path.resolve("listener"), chunkSizeLimit, barrier, sizeLimit = 60000)
        listener.start()

        val sender = SenderByteArrayQueue("127.0.0.1", port, path.resolve("sender"), chunkSizeLimit, barrier)
        val largeData = ByteArray(60000) { it.toByte() }

        sender.enqueue(largeData)
        sender.start()

        waitForReal(2.seconds) { listener.queue.isNotEmpty }
        assertContentEquals(largeData, listener.queue.dequeue())
    }

    @Test
    fun testOutOfOrderMessages() = runTest {
        val port = 11111
        val path = clearedTestPath()
        val chunkSizeLimit = 1024L
        val barrier = byteArrayOf(0x0)

        val listener = ListenerByteArrayQueue(port, path.resolve("listener"), chunkSizeLimit, barrier)
        listener.start()

        val sender = SenderByteArrayQueue("127.0.0.1", port, path.resolve("sender"), chunkSizeLimit, barrier)
        val messages = listOf("First", "Second", "Third").map { it.toByteArray() }

        sender.enqueue(messages[2]) // Send third first
        sender.enqueue(messages[0]) // Send first second
        sender.enqueue(messages[1]) // Send second last
        sender.start()

        waitForReal(2.seconds) { sender.queue.isEmpty }

        assertContentEquals(messages[2], listener.queue.dequeue())
        assertContentEquals(messages[0], listener.queue.dequeue())
        assertContentEquals(messages[1], listener.queue.dequeue())
    }
}