package `fun`.adaptive.persistence

import kotlinx.io.EOFException
import kotlinx.io.files.Path
import java.io.File
import kotlin.io.path.createTempFile
import kotlin.test.*

class RandomAccessFileTest {
    private lateinit var tempFile: File
    private lateinit var randomAccessPersistence: RandomAccessPersistence

    @BeforeTest
    fun setup() {
        tempFile = createTempFile().toFile()
        randomAccessPersistence = Path(tempFile.absolutePath).getRandomAccess("rw")
    }

    @AfterTest
    fun tearDown() {
        randomAccessPersistence.close()
        tempFile.delete()
    }

    @Test
    fun testWriteAndRead() {
        val testData = "Hello, World!".toByteArray()
        randomAccessPersistence.write(testData, 0, testData.size)

        // Reset position to start
        randomAccessPersistence.setPosition(0)

        val readBuffer = ByteArray(testData.size)
        val bytesRead = randomAccessPersistence.read(readBuffer, 0, readBuffer.size, true)

        assertEquals(testData.size, bytesRead)
        assertContentEquals(testData, readBuffer)
    }

    @Test
    fun testSeekAndFilePointer() {
        val testData = "Test seeking functionality".toByteArray()
        randomAccessPersistence.write(testData, 0, testData.size)

        val position = 5L
        randomAccessPersistence.setPosition(position)
        assertEquals(position, randomAccessPersistence.getPosition())

        val readBuffer = ByteArray(testData.size - position.toInt())
        val bytesRead = randomAccessPersistence.read(readBuffer, 0, readBuffer.size, true)

        assertEquals(testData.size - position.toInt(), bytesRead)
        assertContentEquals(
            testData.sliceArray(position.toInt() until testData.size),
            readBuffer
        )
    }

    @Test
    fun testLength() {
        val testData = "Testing file length".toByteArray()
        randomAccessPersistence.write(testData, 0, testData.size)
        assertEquals(testData.size.toLong(), randomAccessPersistence.getSize())
    }

    @Test
    fun testSetLength() {
        val testData = "Original content".toByteArray()
        randomAccessPersistence.write(testData, 0, testData.size)

        val newLength = 10L
        randomAccessPersistence.setSize(newLength)
        assertEquals(newLength, randomAccessPersistence.getSize())
    }

    @Test
    fun testReadWithEOFException() {
        val testData = "Short text".toByteArray()
        randomAccessPersistence.write(testData, 0, testData.size)
        randomAccessPersistence.setPosition(0)

        val largerBuffer = ByteArray(testData.size + 10)

        assertFailsWith<EOFException> {
            randomAccessPersistence.read(largerBuffer, 0, largerBuffer.size, true)
        }
    }

    @Test
    fun testReadWithoutEOFException() {
        val testData = "Short text".toByteArray()
        randomAccessPersistence.write(testData, 0, testData.size)
        randomAccessPersistence.setPosition(0)

        val largerBuffer = ByteArray(testData.size + 10)
        val bytesRead = randomAccessPersistence.read(largerBuffer, 0, largerBuffer.size, false)

        assertEquals(testData.size, bytesRead)
    }

    @Test
    fun testPartialRead() {
        val testData = "Test partial read".toByteArray()
        randomAccessPersistence.write(testData, 0, testData.size)
        randomAccessPersistence.setPosition(0)

        val buffer = ByteArray(5)
        val bytesRead = randomAccessPersistence.read(buffer, 0, buffer.size, true)

        assertEquals(5, bytesRead)
        assertContentEquals(testData.sliceArray(0..4), buffer)
    }
}