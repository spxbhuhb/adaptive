package `fun`.adaptive.lib.util.bytearray

import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.clearedTestPath
import `fun`.adaptive.utility.read
import `fun`.adaptive.utility.resolve
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.test.*

abstract class AbstractByteArrayQueueTest(
    val textual : Boolean
) {

    val pathScope = arrayOf(if (textual) "textual" else "binary")
    
    @Test
    fun testInitializationWithoutBarrier() {
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), textual = textual)
        assertTrue(queue.isInitialized)
    }

    @Test
    fun testInitializationWithBarrier() {
        val testPath = clearedTestPath(scope = pathScope)
        val barrier = if (textual) byteArrayOf(0x0A) else byteArrayOf(1, 2, 3)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = barrier, textual = textual)
        assertTrue(queue.isInitialized)
    }

    @Test
    fun testEnqueueDequeueWithoutBarrier() {
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), textual = textual)
        val data = "Hello, World!".encodeToByteArray()

        queue.enqueue(data)
        assertFalse(queue.isEmpty)

        val dequeued = queue.dequeueOrNull()
        assertNotNull(dequeued)
        assertContentEquals(data, dequeued)
    }

    @Test
    fun testEnqueueDequeueWithBarrier() {
        val testPath = clearedTestPath(scope = pathScope)
        val barrier = if (textual) byteArrayOf(0x0A) else byteArrayOf(1, 2, 3)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = barrier, textual = textual)
        val data = "Barrier Test".encodeToByteArray()

        queue.enqueue(data)
        assertFalse(queue.isEmpty)

        val dequeued = queue.dequeueOrNull()
        assertNotNull(dequeued)
        assertContentEquals(data, dequeued)
    }

    @Test
    fun testEmptyQueueDequeueReturnsNull() {
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), textual = textual)
        assertTrue(queue.isEmpty)
        assertNull(queue.dequeueOrNull())
    }

    @Test
    fun testChunkRollingWithoutBarrier() {
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 10, barrier = byteArrayOf(), textual = textual)

        queue.enqueue("12345".encodeToByteArray())
        queue.enqueue("67890".encodeToByteArray())

        assertFalse(queue.isEmpty)
        assertNotNull(queue.dequeueOrNull())
        assertNotNull(queue.dequeueOrNull())
    }

    @Test
    fun testChunkRollingWithBarrier() {
        val testPath = clearedTestPath(scope = pathScope)
        val barrier = if (textual) byteArrayOf(0x0A) else byteArrayOf(1, 2, 3)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 10, barrier = barrier, textual = textual)

        queue.enqueue("12345".encodeToByteArray())
        queue.enqueue("67890".encodeToByteArray())

        assertFalse(queue.isEmpty)
        assertNotNull(queue.dequeueOrNull())
        assertNotNull(queue.dequeueOrNull())
    }

    @Test
    fun testPersistentDequeuePosition() {
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), persistDequeue = true, textual = textual)
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
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), textual = textual)
        val first = "First".encodeToByteArray()
        val second = "Second".encodeToByteArray()

        queue.enqueue(first)
        queue.enqueue(second)

        assertContentEquals(first, queue.dequeueOrNull())
        assertContentEquals(second, queue.dequeueOrNull())
    }

    @Test
    fun testDequeueOrderWithBarrier() {
        val testPath = clearedTestPath(scope = pathScope)
        val barrier = if (textual) byteArrayOf(0x0A) else byteArrayOf(1, 2, 3, 4)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = barrier, textual = textual)
        val first = "First".encodeToByteArray()
        val second = "Second".encodeToByteArray()

        queue.enqueue(first)
        queue.enqueue(second)

        assertContentEquals(first, queue.dequeueOrNull())
        assertContentEquals(second, queue.dequeueOrNull())
    }

    @Test
    fun testQueueIntegrityAfterMultipleOperations() {
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), textual = textual)

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
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), textual = textual)

        val exception = assertFailsWith<IllegalArgumentException> {
            queue.position(UUID(), 0L)
        }

        assertTrue(exception.message !!.contains("unknown chunk"))
    }

    @Test
    fun testDequeueWithTimeout() = runTest {
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), textual = textual)
        val data = "Timeout Test".encodeToByteArray()

        queue.enqueue(data)
        val dequeued = withTimeoutOrNull(1000) { queue.dequeue() }

        assertNotNull(dequeued)
        assertContentEquals(data, dequeued)
    }

    @Test
    fun testDequeueWithTimeoutNoData() = runTest {
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), textual = textual)

        val dequeued = withTimeoutOrNull(100) { queue.dequeue() }

        assertNull(dequeued)
    }

    @Test
    fun testDequeueMultipleItems() {
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), textual = textual)

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
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), textual = textual)

        val data = "End Test".encodeToByteArray()
        queue.enqueue(data)

        assertContentEquals(data, queue.dequeueOrNull())
        assertNull(queue.dequeueOrNull())  // Ensure the queue is empty
    }

    @Test
    fun testPeekReturnsNextItemWithoutRemoving() = runTest {
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), textual = textual)
        val data = "Peek Test".encodeToByteArray()

        queue.enqueue(data)
        assertContentEquals(data, queue.peek()) // Ensure peek returns the data
        assertTrue(queue.isNotEmpty) // Ensure item is not removed
    }

    @Test
    fun testPeekOrNullReturnsNextItemOrNull() {
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), textual = textual)
        val data = "PeekOrNull Test".encodeToByteArray()

        assertNull(queue.peekOrNull()) // Ensure null is returned when empty

        queue.enqueue(data)
        assertContentEquals(data, queue.peekOrNull()) // Ensure correct item is returned
        assertFalse(queue.isEmpty) // Ensure item is still present
    }

    @Test
    fun testConsumePeekRemovesPeekedItem() {
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), textual = textual)
        val data = "ConsumePeek Test".encodeToByteArray()

        queue.enqueue(data)
        assertContentEquals(data, queue.peekOrNull()) // Ensure peekOrNull gets the data
        queue.dequeueOrNull() // Remove the peeked item
        assertTrue(queue.isEmpty) // Ensure the queue is now empty
        assertNull(queue.dequeueOrNull()) // Ensure item is actually removed
    }

    @Test
    fun testMultiEnqueueDequeuePeek() {
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), textual = textual)

        val first = "First Message".encodeToByteArray()
        val second = "Second Message".encodeToByteArray()
        val third = "Third Message".encodeToByteArray()

        // Enqueue multiple items
        queue.enqueue(first)
        queue.enqueue(second)
        queue.enqueue(third)

        // Peek at the first item without removing it
        assertContentEquals(first, queue.peekOrNull())
        assertTrue(queue.isNotEmpty)

        // Dequeue the first item and ensure it's removed
        assertContentEquals(first, queue.dequeueOrNull())
        assertTrue(queue.isNotEmpty)

        // Peek at the next item (should be second)
        assertContentEquals(second, queue.peekOrNull())

        // Consume peeked item
        queue.dequeueOrNull()
        assertTrue(queue.isNotEmpty)

        // Peek and dequeue the last item
        assertContentEquals(third, queue.peekOrNull())
        assertContentEquals(third, queue.dequeueOrNull())

        // Ensure the queue is empty after all operations
        assertTrue(queue.isEmpty)
        assertNull(queue.peekOrNull())
        assertNull(queue.dequeueOrNull())
    }

    @Test
    fun testInterleavedEnqueueDequeuePeek() {
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), textual = textual)

        val data1 = "Data1".encodeToByteArray()
        val data2 = "Data2".encodeToByteArray()
        val data3 = "Data3".encodeToByteArray()

        queue.enqueue(data1)
        assertContentEquals(data1, queue.peekOrNull()) // Peek at first item
        queue.enqueue(data2)

        assertContentEquals(data1, queue.dequeueOrNull()) // Dequeue first item
        assertContentEquals(data2, queue.peekOrNull()) // Peek at second item

        queue.enqueue(data3)
        assertContentEquals(data2, queue.dequeueOrNull()) // Dequeue second item
        assertContentEquals(data3, queue.peekOrNull()) // Peek at last item

        queue.dequeueOrNull() // Remove last item
        assertTrue(queue.isEmpty) // Queue should be empty
    }

    @Test
    fun testMultiplePeeksWithoutDequeue() {
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 1024, barrier = byteArrayOf(), textual = textual)

        val data1 = "DataA".encodeToByteArray()
        val data2 = "DataB".encodeToByteArray()

        queue.enqueue(data1)
        queue.enqueue(data2)

        assertContentEquals(data1, queue.peekOrNull()) // Peek should return first item
        assertContentEquals(data1, queue.peekOrNull()) // Peek again should still return first item

        assertContentEquals(data1, queue.dequeueOrNull()) // Dequeue first item
        assertContentEquals(data2, queue.peekOrNull()) // Peek should now return second item

        assertContentEquals(data2, queue.dequeueOrNull()) // Dequeue second item
        assertNull(queue.peekOrNull()) // Queue should be empty now
    }

    @Test
    fun testChunkRollingWithMultipleOperations() {
        val testPath = clearedTestPath(scope = pathScope)
        val queue = ByteArrayQueue(testPath, chunkSizeLimit = 20, barrier = byteArrayOf(), textual = textual)

        val data1 = "Chunk1".encodeToByteArray()
        val data2 = "Chunk2".encodeToByteArray()
        val data3 = "Chunk3".encodeToByteArray()

        queue.enqueue(data1)
        queue.enqueue(data2)
        queue.enqueue(data3) // This should cause chunk rolling

        assertContentEquals(data1, queue.peekOrNull())
        assertContentEquals(data1, queue.dequeueOrNull())

        assertContentEquals(data2, queue.peekOrNull())
        queue.dequeueOrNull()

        assertContentEquals(data3, queue.peekOrNull())
        assertContentEquals(data3, queue.dequeueOrNull())

        assertNull(queue.peekOrNull()) // Ensure queue is empty
    }
}
