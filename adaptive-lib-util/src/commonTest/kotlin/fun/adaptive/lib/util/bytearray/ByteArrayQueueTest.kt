import `fun`.adaptive.lib.util.bytearray.ByteArrayQueue
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.clearedTestPath
import `fun`.adaptive.utility.read
import `fun`.adaptive.utility.resolve
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
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

        val dequeued = queue.dequeueOrNull()
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

        val dequeued = queue.dequeueOrNull()
        assertNotNull(dequeued)
        assertContentEquals(data, dequeued)
    }

    @Test
    fun testEmptyQueueDequeueReturnsNull() {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf())
        assertTrue(queue.isEmpty)
        assertNull(queue.dequeueOrNull())
    }

    @Test
    fun testChunkRollingWithoutBarrier() {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 10, barrier = byteArrayOf())

        queue.enqueue("12345".encodeToByteArray())
        queue.enqueue("67890".encodeToByteArray())

        assertFalse(queue.isEmpty)
        assertNotNull(queue.dequeueOrNull())
        assertNotNull(queue.dequeueOrNull())
    }

    @Test
    fun testChunkRollingWithBarrier() {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 10, barrier = byteArrayOf(1, 2, 3))

        queue.enqueue("12345".encodeToByteArray())
        queue.enqueue("67890".encodeToByteArray())

        assertFalse(queue.isEmpty)
        assertNotNull(queue.dequeueOrNull())
        assertNotNull(queue.dequeueOrNull())
    }

    @Test
    fun testPersistentDequeuePosition() {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), persistDequeue = true)
        val data = "PersistentData".encodeToByteArray()
        queue.enqueue(data)

        val dequeued = queue.dequeueOrNull()
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

        assertContentEquals(first, queue.dequeueOrNull())
        assertContentEquals(second, queue.dequeueOrNull())
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

        assertContentEquals(first, queue.dequeueOrNull())
        assertContentEquals(second, queue.dequeueOrNull())
    }

    @Test
    fun testQueueIntegrityAfterMultipleOperations() {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf())

        queue.enqueue("Data1".encodeToByteArray())
        queue.dequeueOrNull()
        queue.enqueue("Data2".encodeToByteArray())
        queue.enqueue("Data3".encodeToByteArray())

        assertContentEquals("Data2".encodeToByteArray(), queue.dequeueOrNull())
        assertContentEquals("Data3".encodeToByteArray(), queue.dequeueOrNull())
        assertNull(queue.dequeueOrNull())
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

    @Test
    fun testDequeueWithTimeout() = runTest {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf())
        val data = "Timeout Test".encodeToByteArray()

        queue.enqueue(data)
        val dequeued = withTimeoutOrNull(1000) { queue.dequeue() }

        assertNotNull(dequeued)
        assertContentEquals(data, dequeued)
    }

    @Test
    fun testDequeueWithTimeoutNoData() = runTest {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf())

        val dequeued = withTimeoutOrNull(100) { queue.dequeue() }

        assertNull(dequeued)
    }

    @Test
    fun testDequeueMultipleItems() {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf())

        val first = "First Item".encodeToByteArray()
        val second = "Second Item".encodeToByteArray()
        val third = "Third Item".encodeToByteArray()

        queue.enqueue(first)
        queue.enqueue(second)
        queue.enqueue(third)

        assertContentEquals(first, queue.dequeueOrNull())
        assertContentEquals(second, queue.dequeueOrNull())
        assertContentEquals(third, queue.dequeueOrNull())
        assertNull(queue.dequeueOrNull())
    }

    @Test
    fun testDequeueAfterEnd() {
        val testPath = clearedTestPath()
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf())

        val data = "End Test".encodeToByteArray()
        queue.enqueue(data)

        assertContentEquals(data, queue.dequeueOrNull())
        assertNull(queue.dequeueOrNull())  // Ensure the queue is empty
    }
}
