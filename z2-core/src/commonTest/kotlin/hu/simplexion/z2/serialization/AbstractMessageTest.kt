package hu.simplexion.z2.serialization

import hu.simplexion.z2.util.UUID
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

open class AbstractMessageTest(
    private val serializationConfig: SerializationConfig
) {

    @Test
    fun testPrimitives() {
        singlePrimitive(true, { boolean(1, "val", it) }) { boolean(1, "val") }
        singlePrimitive(false, { boolean(1, "val", it) }) { boolean(1, "val") }

        singlePrimitive(123, { int(1, "val", it) }) { int(1, "val") }
        singlePrimitive(- 123, { int(1, "val", it) }) { int(1, "val") }

        singlePrimitive(123, { long(1, "val", it) }) { long(1, "val") }
        singlePrimitive(- 123, { long(1, "val", it) }) { long(1, "val") }

        singlePrimitive("hello", { string(1, "val", it) }) { string(1, "val") }

        singlePrimitive("hello".encodeToByteArray(), { byteArray(1, "val", it) }) { byteArray(1, "val") }

        singlePrimitive(UUID<Any>(), { uuid(1, "val", it) }) { uuid(1, "val") }
    }

    @Test
    fun testBooleanList() {
        val expected = listOf(true, false, true)
        val wireFormat = serializationConfig.messageBuilder().booleanList(1, "val", expected).pack()
        val message = serializationConfig.toMessage(wireFormat)
        val actual = message.booleanList(1, "val")
        assertContentEquals(expected, actual)
    }

    @Test
    fun testIntList() {
        val expected = listOf(1, 4, 7)
        val wireFormat = serializationConfig.messageBuilder().intList(1, "val", expected).pack()
        val message = serializationConfig.toMessage(wireFormat)
        val actual = message.intList(1, "val")
        assertContentEquals(expected, actual)
    }

    @Test
    fun testLongList() {
        val expected = listOf(1L, 4L, 7L)
        val wireFormat = serializationConfig.messageBuilder().longList(1, "val", expected).pack()
        val message = serializationConfig.toMessage(wireFormat)
        val actual = message.longList(1, "val")
        assertContentEquals(expected, actual)
    }

    @Test
    fun testStringList() {
        val expected = listOf("a", "b", "c")
        val wireFormat = serializationConfig.messageBuilder().stringList(1, "val", expected).pack()
        val message = serializationConfig.toMessage(wireFormat)
        val actual = message.stringList(1, "val")
        assertContentEquals(expected, actual)
    }

    @Test
    fun testByteArrayList() {
        val expected = listOf(byteArrayOf(1), byteArrayOf(2), byteArrayOf(3))
        val wireFormat = serializationConfig.messageBuilder().byteArrayList(1, "val", expected).pack()
        val message = serializationConfig.toMessage(wireFormat)
        val actual = message.byteArrayList(1, "val")
        for (i in expected.indices) {
            assertContentEquals(expected[i], actual[i])
        }
    }

    @Test
    fun testUuidList() {
        val expected = listOf<UUID<Any>>(UUID(), UUID(), UUID())
        val wireFormat = serializationConfig.messageBuilder().uuidList(1, "val", expected).pack()
        val message = serializationConfig.toMessage(wireFormat)
        val actual = message.uuidList<Any>(1, "val")
        assertContentEquals(expected, actual)
    }

    @Test
    fun testSimpleClass() =
        instance(A, A) { A(true, 12, "hello", mutableListOf(4, 5, 6)) }

    @Test
    fun testNestedClass() =
        instance(B, B) { B(A(true, 123, "a", mutableListOf(4, 5, 6)), "b") }

    @Test
    fun testListOfNestedClasses() {
        val expected = listOf(
            B(A(true, 123, "a", mutableListOf(1, 2, 3)), "aa"),
            B(A(false, 456, "b", mutableListOf(4, 5, 6)), "bb"),
            B(A(true, 789, "c", mutableListOf(7, 8, 9)), "cc")
        )

        val wireFormat = serializationConfig.messageBuilder().instanceList(1, "val", B, expected).pack()
        //println(wireFormat.dumpProto())
        val message = serializationConfig.toMessage(wireFormat)
        val actual = message.instanceList(1, "val", B)
        assertContentEquals(expected, actual)
    }

    // ----------------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------------

    fun <T> singlePrimitive(expected: T, write: MessageBuilder.(value: T) -> Unit, read: Message.() -> T) {
        val wireFormat = serializationConfig.messageBuilder().apply { write(expected) }.pack()
        val message = serializationConfig.toMessage(wireFormat)

        val actual = message.read()
        if (expected is ByteArray && actual is ByteArray) {
            assertContentEquals(expected, actual)
        } else {
            assertEquals(expected, actual)
        }
    }

    fun <T> instance(encoder: InstanceEncoder<T>, decoder: InstanceDecoder<T>, builder: () -> T) {
        val expected = builder()
        val wireFormat = encoder.encodeInstance(serializationConfig.messageBuilder(), expected)
        val message = serializationConfig.toMessage(wireFormat)
        val actual = decoder.decodeInstance(message)
        assertEquals(expected, actual)
    }

}