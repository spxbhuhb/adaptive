package `fun`.adaptive.lib.util.bytearray

import `fun`.adaptive.utility.clearedTestPath
import `fun`.adaptive.utility.exists
import `fun`.adaptive.utility.list
import `fun`.adaptive.utility.readString
import `fun`.adaptive.utility.resolve
import kotlin.test.*

class ByteArrayQueueTest {

    private val chunkSizeLimit = 1024L
    private val barrier = ByteArray(4) { 0 }

    @Test
    fun `initialize should setup queue correctly`() {
        val tempPath = clearedTestPath()
        val queue = ByteArrayQueue(tempPath, chunkSizeLimit, barrier)
        queue.initialize()
        assertTrue(queue.isInitialized, "Queue should be initialized")
    }

    @Test
    fun `enqueue should add data to queue`() {
        val tempPath = clearedTestPath()
        val queue = ByteArrayQueue(tempPath, chunkSizeLimit, barrier)

        queue.initialize()
        val data = "Hello, Queue!".encodeToByteArray()
        queue.enqueue(data)

        assertTrue(tempPath.list().isNotEmpty(), "Queue should create chunk files")
    }

    @Test
    fun `dequeue should retrieve enqueued data`() {
        val tempPath = clearedTestPath()
        val queue = ByteArrayQueue(tempPath, chunkSizeLimit, barrier)

        queue.initialize()
        val data = "Hello, Queue!".encodeToByteArray()
        queue.enqueue(data)
        val dequeued = queue.dequeue()

        assertNotNull(dequeued, "Dequeued data should not be null")
        assertContentEquals(data, dequeued, "Dequeued data should match enqueued data")
    }

    @Test
    fun `dequeue should return null if queue is empty`() {
        val tempPath = clearedTestPath()
        val queue = ByteArrayQueue(tempPath, chunkSizeLimit, barrier)

        queue.initialize()
        assertNull(queue.dequeue(), "Dequeue should return null when queue is empty")
    }

    @Test
    fun `position should move to correct chunk and position`() {
        val tempPath = clearedTestPath()
        val queue = ByteArrayQueue(tempPath, chunkSizeLimit, barrier)

        queue.initialize()
        val data = "Test Position".encodeToByteArray()
        queue.enqueue(data)

        val chunkName = queue.chunkIds.first()
        queue.position(chunkName, 0L)
        val dequeued = queue.dequeue()

        assertNotNull(dequeued, "Dequeued data should not be null after repositioning")
        assertContentEquals(data, dequeued, "Data should match after repositioning")
    }

    @Test
    fun `rollEnqueueChunk should create new chunk when size limit is exceeded`() {
        val tempPath = clearedTestPath()
        val queue = ByteArrayQueue(tempPath, chunkSizeLimit, barrier)

        queue.initialize()
        val largeData = ByteArray((chunkSizeLimit / 2).toInt())
        queue.enqueue(largeData)
        queue.enqueue(largeData)

        assertTrue(queue.chunkIds.size > 1, "Queue should roll to a new chunk when size limit is exceeded")
    }

    @Test
    fun `rollDequeueChunk should move to next chunk when end is reached`() {
        val tempPath = clearedTestPath()
        val queue = ByteArrayQueue(tempPath, chunkSizeLimit, barrier)

        queue.initialize()
        val firstChunkData = "First Chunk".encodeToByteArray()
        val secondChunkData = "Second Chunk".encodeToByteArray()

        queue.enqueue(firstChunkData)
        queue.enqueue(secondChunkData)

        assertContentEquals(firstChunkData, queue.dequeue(), "First chunk should be dequeued first")
        assertContentEquals(secondChunkData, queue.dequeue(), "Second chunk should be dequeued after first")
    }

    @Test
    fun `dequeue should persist position when persistDequeue is true`() {
        val tempPath = clearedTestPath()
        val queue = ByteArrayQueue(tempPath, chunkSizeLimit, barrier, persistDequeue = true)

        queue.initialize()
        val data = "Persistent Data".encodeToByteArray()
        queue.enqueue(data)

        val dequeued = queue.dequeue()
        assertNotNull(dequeued, "Dequeued data should not be null")
        assertContentEquals(data, dequeued, "Dequeued data should match enqueued data")

        val persistedPositionFile = tempPath.resolve("dequeue.bin")
        assertTrue(persistedPositionFile.exists(), "Dequeue position file should be created")
        val persistedData = persistedPositionFile.readString()
        assertTrue(persistedData.contains(";"), "Dequeue position file should contain valid data")
    }

    @Test
    fun `dequeue should return with null when the queue is empty`() {
        val tempPath = clearedTestPath()
        val queue = ByteArrayQueue(tempPath, chunkSizeLimit, barrier, persistDequeue = true)

        queue.initialize()

        queue.enqueue("some data".encodeToByteArray())
        queue.dequeue() // drop queued entry

        assertNull(queue.dequeue(), "Dequeue should return null when chunk end is reached")
        assertNull(queue.dequeue(), "Dequeue should return null when chunk end is reached")
    }
}