import `fun`.adaptive.lib.util.bytearray.ByteArrayQueue
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.clearedTestPath
import `fun`.adaptive.utility.read
import `fun`.adaptive.utility.resolve
import kotlin.test.*

class ByteArrayQueueTest {

    @Test
    fun testInitializationWithoutBarrier() {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf())
        assertTrue(queue.isInitialized)
    }

    @Test
    fun testInitializationWithBarrier() {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(1, 2, 3))
        assertTrue(queue.isInitialized)
    }

    @Test
    fun testEnqueueDequeueWithoutBarrier() {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf())
        val data = "Hello, World!".encodeToByteArray()

        queue.enqueue(data)
        assertFalse(queue.isEmpty)

        val dequeued = queue.dequeue()
        assertNotNull(dequeued)
        assertContentEquals(data, dequeued)
    }

    @Test
    fun testEnqueueDequeueWithBarrier() {
        val testPath = clearedTestPath()
        val barrier = byteArrayOf(1, 2, 3)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = barrier)
        val data = "Barrier Test".encodeToByteArray()

        queue.enqueue(data)
        assertFalse(queue.isEmpty)

        val dequeued = queue.dequeue()
        assertNotNull(dequeued)
        assertContentEquals(data, dequeued)
    }

    @Test
    fun testEmptyQueueDequeueReturnsNull() {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf())
        assertTrue(queue.isEmpty)
        assertNull(queue.dequeue())
    }

    @Test
    fun testChunkRollingWithoutBarrier() {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 10, barrier = byteArrayOf())

        queue.enqueue("12345".encodeToByteArray())
        queue.enqueue("67890".encodeToByteArray())

        assertFalse(queue.isEmpty)
        assertNotNull(queue.dequeue())
        assertNotNull(queue.dequeue())
    }

    @Test
    fun testChunkRollingWithBarrier() {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 10, barrier = byteArrayOf(1, 2, 3))

        queue.enqueue("12345".encodeToByteArray())
        queue.enqueue("67890".encodeToByteArray())

        assertFalse(queue.isEmpty)
        assertNotNull(queue.dequeue())
        assertNotNull(queue.dequeue())
    }

    @Test
    fun testPersistentDequeuePosition() {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), persistDequeue = true)
        val data = "PersistentData".encodeToByteArray()
        queue.enqueue(data)

        val dequeued = queue.dequeue()
        assertNotNull(dequeued)
        assertContentEquals(data, dequeued)

        val dequeueFile = testPath.resolve(ByteArrayQueue.DEQUEUE_NAME).read().decodeToString()
        assertTrue(dequeueFile.contains(';'))
    }

    @Test
    fun testDequeueOrderWithoutBarrier() {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf())
        val first = "First".encodeToByteArray()
        val second = "Second".encodeToByteArray()

        queue.enqueue(first)
        queue.enqueue(second)

        assertContentEquals(first, queue.dequeue())
        assertContentEquals(second, queue.dequeue())
    }

    @Test
    fun testDequeueOrderWithBarrier() {
        val testPath = clearedTestPath()
        val barrier = byteArrayOf(1, 2, 3, 4)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = barrier)
        val first = "First".encodeToByteArray()
        val second = "Second".encodeToByteArray()

        queue.enqueue(first)
        queue.enqueue(second)

        assertContentEquals(first, queue.dequeue())
        assertContentEquals(second, queue.dequeue())
    }

    @Test
    fun testQueueIntegrityAfterMultipleOperations() {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf())

        queue.enqueue("Data1".encodeToByteArray())
        queue.dequeue()
        queue.enqueue("Data2".encodeToByteArray())
        queue.enqueue("Data3".encodeToByteArray())

        assertContentEquals("Data2".encodeToByteArray(), queue.dequeue())
        assertContentEquals("Data3".encodeToByteArray(), queue.dequeue())
        assertNull(queue.dequeue())
    }

    @Test
    fun testInvalidDequeuePositionThrows() {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf())

        val exception = assertFailsWith<IllegalArgumentException> {
            queue.position(UUID(), 0L)
        }

        assertTrue(exception.message !!.contains("unknown chunk"))
    }
}
