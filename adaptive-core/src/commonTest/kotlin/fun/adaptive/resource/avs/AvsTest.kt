package `fun`.adaptive.resource.avs

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class AvsTest {

    @Test
    fun basic() {
        val expected = "Hello World!".encodeToByteArray()
        val writer = AvsWriter()
        writer += expected
        val binary = writer.pack()

        val reader = AvsReader(binary)
        assertContentEquals(expected, reader[0])
    }

    @Test
    fun empty() {
        val writer = AvsWriter()
        val binary = writer.pack()
        val reader = AvsReader(binary)
        assertEquals(0, reader.size)
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun two() {
        val v1 = "12".encodeToByteArray()
        val v2 = "345".encodeToByteArray()

        val writer = AvsWriter()
        writer += v1
        writer += v2
        val binary = writer.pack()

        println(binary.toHexString(format = HexFormat.Default))

        val reader = AvsReader(binary)

        assertContentEquals(v1, reader[0])
        assertContentEquals(v2, reader[1])

        val readerList = reader.toList()

        listOf(v1, v2).forEachIndexed { index, bytes ->
            assertContentEquals(bytes, readerList[index])
        }
    }

    @Test
    fun three() {
        val v1 = "123".encodeToByteArray()
        val v2 = "456".encodeToByteArray()
        val v3 = "789".encodeToByteArray()

        val writer = AvsWriter()
        writer += v1
        writer += v2
        writer += v3
        val binary = writer.pack()

        val reader = AvsReader(binary)

        assertContentEquals(v1, reader[0])
        assertContentEquals(v2, reader[1])
        assertContentEquals(v3, reader[2])

        val readerList = reader.toList()

        listOf(v1, v2, v3).forEachIndexed { index, bytes ->
            assertContentEquals(bytes, readerList[index])
        }
    }
}