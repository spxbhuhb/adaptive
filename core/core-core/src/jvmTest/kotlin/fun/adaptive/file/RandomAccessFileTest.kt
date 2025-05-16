package `fun`.adaptive.file

import kotlinx.io.EOFException
import kotlinx.io.files.Path
import java.io.File
import kotlin.io.path.createTempFile
import kotlin.test.*

class RandomAccessFileTest {
    private lateinit var tempFile: File
    private lateinit var randomAccessFile: RandomAccessFile

    @BeforeTest
    fun setup() {
        tempFile = createTempFile().toFile()
        randomAccessFile = Path(tempFile.absolutePath).getRandomAccess("rw")
    }

    @AfterTest
    fun tearDown() {
        randomAccessFile.close()
        tempFile.delete()
    }

    @Test
    fun testWriteAndRead() {
        val testData = "Hello, World!".toByteArray()
        randomAccessFile.write(testData, 0, testData.size)

        // Reset position to start
        randomAccessFile.seek(0)

        val readBuffer = ByteArray(testData.size)
        val bytesRead = randomAccessFile.read(readBuffer, 0, readBuffer.size, true)

        assertEquals(testData.size, bytesRead)
        assertContentEquals(testData, readBuffer)
    }

    @Test
    fun testSeekAndFilePointer() {
        val testData = "Test seeking functionality".toByteArray()
        randomAccessFile.write(testData, 0, testData.size)

        val position = 5L
        randomAccessFile.seek(position)
        assertEquals(position, randomAccessFile.getFilePointer())

        val readBuffer = ByteArray(testData.size - position.toInt())
        val bytesRead = randomAccessFile.read(readBuffer, 0, readBuffer.size, true)

        assertEquals(testData.size - position.toInt(), bytesRead)
        assertContentEquals(
            testData.sliceArray(position.toInt() until testData.size),
            readBuffer
        )
    }

    @Test
    fun testLength() {
        val testData = "Testing file length".toByteArray()
        randomAccessFile.write(testData, 0, testData.size)
        assertEquals(testData.size.toLong(), randomAccessFile.length())
    }

    @Test
    fun testSetLength() {
        val testData = "Original content".toByteArray()
        randomAccessFile.write(testData, 0, testData.size)

        val newLength = 10L
        randomAccessFile.setLength(newLength)
        assertEquals(newLength, randomAccessFile.length())
    }

    @Test
    fun testReadWithEOFException() {
        val testData = "Short text".toByteArray()
        randomAccessFile.write(testData, 0, testData.size)
        randomAccessFile.seek(0)

        val largerBuffer = ByteArray(testData.size + 10)

        assertFailsWith<EOFException> {
            randomAccessFile.read(largerBuffer, 0, largerBuffer.size, true)
        }
    }

    @Test
    fun testReadWithoutEOFException() {
        val testData = "Short text".toByteArray()
        randomAccessFile.write(testData, 0, testData.size)
        randomAccessFile.seek(0)

        val largerBuffer = ByteArray(testData.size + 10)
        val bytesRead = randomAccessFile.read(largerBuffer, 0, largerBuffer.size, false)

        assertEquals(testData.size, bytesRead)
    }

    @Test
    fun testPartialRead() {
        val testData = "Test partial read".toByteArray()
        randomAccessFile.write(testData, 0, testData.size)
        randomAccessFile.seek(0)

        val buffer = ByteArray(5)
        val bytesRead = randomAccessFile.read(buffer, 0, buffer.size, true)

        assertEquals(5, bytesRead)
        assertContentEquals(testData.sliceArray(0..4), buffer)
    }
}