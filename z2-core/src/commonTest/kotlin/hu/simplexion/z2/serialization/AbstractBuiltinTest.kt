package hu.simplexion.z2.serialization

import hu.simplexion.z2.serialization.protobuf.enumListToOrdinals
import hu.simplexion.z2.util.UUID
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

abstract class AbstractBuiltinTest(
    private val serializationConfig: SerializationConfig
) {

    private val booleanVal = true
    private val intVal = 123
    private val longVal = 1234L
    private val stringVal = "abc"
    private val byteArrayVal = byteArrayOf(9, 8, 7)
    private val uuidVal = UUID<Any>()
    private val instanceVal = A(true, 12, "hello")
    private val enumVal = E.V1

    private val booleanListVal = listOf(true, false, true)
    private val intListVal = listOf(1, 2, 3)
    private val longListVal = listOf(1L, 2L, 3L, 4L)
    private val stringListVal = listOf("a", "b", "c")
    private val byteArrayListVal = listOf(byteArrayOf(1), byteArrayOf(2), byteArrayOf(3))
    private val uuidListVal = listOf(UUID<Any>(), UUID(), UUID())
    private val enumListVal = listOf(E.V2, E.V1)

    private val instanceListVal = listOf(
        B(A(true, 123, "a", mutableListOf(1, 2, 3)), "AA"),
        B(A(false, 456, "b", mutableListOf(4, 5, 6)), "BB"),
        B(A(true, 789, "c", mutableListOf(7, 8, 9)), "CC")
    )

    @Test
    fun testBuiltins() {
        var fieldNumber = 1

        val builder = serializationConfig.messageBuilder()
            .startInstance()

            .boolean(fieldNumber ++, "booleanVal", booleanVal)
            .booleanOrNull(fieldNumber ++, "booleanValOrNull", booleanVal)
            .booleanOrNull(fieldNumber ++, "booleanNullVal", null)

            .int(fieldNumber ++, "intVal", intVal)
            .intOrNull(fieldNumber ++, "intValOrNull", intVal)
            .intOrNull(fieldNumber ++, "intNullVal", null)

            .long(fieldNumber ++, "longVal", longVal)
            .longOrNull(fieldNumber ++, "longValOrNull", longVal)
            .longOrNull(fieldNumber ++, "longNullVal", null)

            .string(fieldNumber ++, "stringVal", stringVal)
            .stringOrNull(fieldNumber ++, "stringValOrNull", stringVal)
            .stringOrNull(fieldNumber ++, "stringNullVal", null)

            .byteArray(fieldNumber ++, "byteArrayVal", byteArrayVal)
            .byteArrayOrNull(fieldNumber ++, "byteArrayValOrNull", byteArrayVal)
            .byteArrayOrNull(fieldNumber ++, "byteArrayNullVal", null)

            .uuid(fieldNumber ++, "uuidVal", uuidVal)
            .uuidOrNull(fieldNumber ++, "uuidValOrNull", uuidVal)
            .uuidOrNull(fieldNumber ++, "uuidNullVal", null)

            .instance(fieldNumber ++, "instanceVal", A, instanceVal)
            .instanceOrNull(fieldNumber ++, "instanceValOrNull", A, instanceVal)
            .instanceOrNull(fieldNumber ++, "instanceNullVal", A, null)

            .booleanList(fieldNumber ++, "booleanListEmptyVal", emptyList())
            .booleanList(fieldNumber ++, "booleanListVal", booleanListVal)
            .booleanListOrNull(fieldNumber ++, "booleanListValOrNull", booleanListVal)
            .booleanListOrNull(fieldNumber ++, "booleanListNullVal", null)

            .intList(fieldNumber ++, "intListEmptyVal", emptyList())
            .intList(fieldNumber ++, "intListVal", intListVal)
            .intListOrNull(fieldNumber ++, "intListValOrNull", intListVal)
            .intListOrNull(fieldNumber ++, "intListNullVal", null)

            .longList(fieldNumber ++, "longListEmptyVal", emptyList())
            .longList(fieldNumber ++, "longListVal", longListVal)
            .longListOrNull(fieldNumber ++, "longListValOrNull", longListVal)
            .longListOrNull(fieldNumber ++, "longListNullVal", null)

            .stringList(fieldNumber ++, "stringListEmptyVal", emptyList())
            .stringList(fieldNumber ++, "stringListVal", stringListVal)
            .stringListOrNull(fieldNumber ++, "stringListValOrNull", stringListVal)
            .stringListOrNull(fieldNumber ++, "stringListNullVal", null)

            .byteArrayList(fieldNumber ++, "byteArrayListEmptyVal", emptyList())
            .byteArrayList(fieldNumber ++, "byteArrayListVal", byteArrayListVal)
            .byteArrayListOrNull(fieldNumber ++, "byteArrayListValOrNull", byteArrayListVal)
            .byteArrayListOrNull(fieldNumber ++, "byteArrayListNullVal", null)

            .uuidList(fieldNumber ++, "uuidListEmptyVal", emptyList())
            .uuidList(fieldNumber ++, "uuidListVal", uuidListVal)
            .uuidListOrNull(fieldNumber ++, "uuidListValOrNull", uuidListVal)
            .uuidListOrNull(fieldNumber ++, "uuidListNullVal", null)

            .instanceList(fieldNumber ++, "instanceListEmptyVal", B, emptyList())
            .instanceList(fieldNumber ++, "instanceListVal", B, instanceListVal)
            .instanceListOrNull(fieldNumber ++, "instanceListValOrNull", B, instanceListVal)
            .instanceListOrNull(fieldNumber, "instanceListNullVal", B, null)

            .endInstance()

        val wireformat = builder.pack()
        val message = serializationConfig.toMessage(wireformat)

        fieldNumber = 1

        assertEquals(booleanVal, message.boolean(fieldNumber ++, "booleanVal"))
        assertEquals(booleanVal, message.booleanOrNull(fieldNumber ++, "booleanValOrNull"))
        assertEquals(null, message.booleanOrNull(fieldNumber ++, "booleanNullVal"))

        assertEquals(intVal, message.int(fieldNumber ++, "intVal"))
        assertEquals(intVal, message.intOrNull(fieldNumber ++, "intValOrNull"))
        assertEquals(null, message.intOrNull(fieldNumber ++, "intNullVal"))

        assertEquals(longVal, message.long(fieldNumber ++, "longVal"))
        assertEquals(longVal, message.longOrNull(fieldNumber ++, "longValOrNull"))
        assertEquals(null, message.longOrNull(fieldNumber ++, "longNullVal"))

        assertEquals(stringVal, message.string(fieldNumber ++, "stringVal"))
        assertEquals(stringVal, message.stringOrNull(fieldNumber ++, "stringValOrNull"))
        assertEquals(null, message.stringOrNull(fieldNumber ++, "stringNullVal"))

        assertContentEquals(byteArrayVal, message.byteArray(fieldNumber ++, "byteArrayVal"))
        assertContentEquals(byteArrayVal, message.byteArrayOrNull(fieldNumber ++, "byteArrayValOrNull"))
        assertEquals(null, message.byteArrayOrNull(fieldNumber ++, "byteArrayNullVal"))

        assertEquals(uuidVal, message.uuid(fieldNumber ++, "uuidVal"))
        assertEquals(uuidVal, message.uuidOrNull(fieldNumber ++, "uuidValOrNull"))
        assertEquals(null, message.uuidOrNull<Any>(fieldNumber ++, "uuidNullVal"))

        assertEquals(instanceVal, message.instance(fieldNumber ++, "instanceVal", A))
        assertEquals(instanceVal, message.instanceOrNull(fieldNumber ++, "instanceValOrNull", A))
        assertEquals(null, message.instanceOrNull(fieldNumber ++, "instanceNullVal", A))

        assertContentEquals(emptyList(), message.booleanList(fieldNumber ++, "booleanListEmptyVal"))
        assertContentEquals(booleanListVal, message.booleanList(fieldNumber ++, "booleanListVal"))
        assertContentEquals(booleanListVal, message.booleanListOrNull(fieldNumber ++, "booleanListValOrNull"))
        assertEquals(null, message.booleanListOrNull(fieldNumber ++, "booleanListNullVal"))

        assertContentEquals(emptyList(), message.intList(fieldNumber ++, "intListEmptyVal"))
        assertContentEquals(intListVal, message.intList(fieldNumber ++, "intListVal"))
        assertContentEquals(intListVal, message.intListOrNull(fieldNumber ++, "intListValOrNull"))
        assertEquals(null, message.intListOrNull(fieldNumber ++, "intListNullVal"))

        assertContentEquals(emptyList(), message.longList(fieldNumber ++, "longListEmptyVal"))
        assertContentEquals(longListVal, message.longList(fieldNumber ++, "longListVal"))
        assertContentEquals(longListVal, message.longListOrNull(fieldNumber ++, "longListValOrNull"))
        assertEquals(null, message.longListOrNull(fieldNumber ++, "longListNullVal"))

        assertContentEquals(emptyList(), message.stringList(fieldNumber ++, "stringListEmptyVal"))
        assertContentEquals(stringListVal, message.stringList(fieldNumber ++, "stringListVal"))
        assertContentEquals(stringListVal, message.stringListOrNull(fieldNumber ++, "stringListValOrNull"))
        assertEquals(null, message.stringListOrNull(fieldNumber ++, "stringListNullVal"))

        assertContentEquals(emptyList(), message.byteArrayList(fieldNumber ++, "byteArrayListEmptyVal"))
        message.byteArrayList(fieldNumber ++, "byteArrayListVal").forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }
        assertNotNull(
            message.byteArrayListOrNull(fieldNumber ++, "byteArrayListValOrNull")
        ).forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }
        assertEquals(null, message.byteArrayListOrNull(fieldNumber ++, "byteArrayListNullVal"))

        assertContentEquals(emptyList(), message.uuidList<Any>(fieldNumber ++, "uuidListEmptyVal"))
        assertContentEquals(uuidListVal, message.uuidList(fieldNumber ++, "uuidListVal"))
        assertContentEquals(uuidListVal, message.uuidListOrNull(fieldNumber ++, "uuidListValOrNull"))
        assertEquals(null, message.uuidListOrNull<Any>(fieldNumber ++, "uuidListNullVal"))

        assertContentEquals(emptyList(), message.instanceList(fieldNumber ++, "instanceListEmptyVal", B))
        assertContentEquals(instanceListVal, message.instanceList(fieldNumber ++, "instanceListVal", B))
        assertContentEquals(instanceListVal, message.instanceListOrNull(fieldNumber ++, "instanceListValOrNull", B))
        assertEquals(null, message.instanceListOrNull(fieldNumber, "instanceListNullVal", B))
    }

    @Test
    fun standaloneTest() {

        val standaloneValue = serializationConfig.standaloneValue()

        fun message(builder: MessageBuilder.() -> Unit): Message =
            serializationConfig.toMessage(serializationConfig.messageBuilder().apply(builder).pack())

//        assertEquals(Unit, standaloneValue.decodeUnit(message { }))

        assertEquals(booleanVal, standaloneValue.decodeBoolean(message { boolean(1, "val", booleanVal) }))
        assertEquals(null, standaloneValue.decodeBooleanOrNull(message { booleanOrNull(1, "val", null) }))
        assertEquals(booleanVal, standaloneValue.decodeBooleanOrNull(message { booleanOrNull(1, "val", booleanVal) }))

        assertEquals(intVal, standaloneValue.decodeInt(message { int(1, "val", intVal) }))
        assertEquals(null, standaloneValue.decodeIntOrNull(message { intOrNull(1, "val", null) }))
        assertEquals(intVal, standaloneValue.decodeIntOrNull(message { intOrNull(1, "val", intVal) }))

        assertEquals(longVal, standaloneValue.decodeLong(message { long(1, "val", longVal) }))
        assertEquals(null, standaloneValue.decodeLongOrNull(message { longOrNull(1, "val", null) }))
        assertEquals(longVal, standaloneValue.decodeLongOrNull(message { longOrNull(1, "val", longVal) }))

        assertEquals(stringVal, standaloneValue.decodeString(message { string(1, "val", stringVal) }))
        assertEquals(null, standaloneValue.decodeStringOrNull(message { stringOrNull(1, "val", null) }))
        assertEquals(stringVal, standaloneValue.decodeStringOrNull(message { stringOrNull(1, "val", stringVal) }))

        assertContentEquals(byteArrayVal, standaloneValue.decodeByteArray(message { byteArray(1, "val", byteArrayVal) }))
        assertEquals(null, standaloneValue.decodeByteArrayOrNull(message { byteArrayOrNull(1, "val", null) }))
        assertContentEquals(byteArrayVal, standaloneValue.decodeByteArrayOrNull(message { byteArrayOrNull(1, "val", byteArrayVal) }))

        assertEquals(uuidVal, standaloneValue.decodeUuid(message { uuid(1, "val", uuidVal) }))
        assertEquals(null, standaloneValue.decodeUuidOrNull(message { uuidOrNull(1, "val", null) }))
        assertEquals(uuidVal, standaloneValue.decodeUuidOrNull(message { uuidOrNull(1, "val", uuidVal) }))

        assertEquals(instanceVal, standaloneValue.decodeInstance(message { instance(1, "val", A, instanceVal) }, A))
        assertEquals(null, standaloneValue.decodeInstanceOrNull(message { instanceOrNull(1, "val", A, null) }, A))
        assertEquals(instanceVal, standaloneValue.decodeInstanceOrNull(message { instanceOrNull(1, "val", A, instanceVal) }, A))

        // FIXME enum encode/decode should be serializer dependent
        assertEquals(enumVal, standaloneValue.decodeEnum(message { int(1, "val", enumVal.ordinal) }, E.entries))
        assertEquals(null, standaloneValue.decodeEnumOrNull(message { intOrNull(1, "val", null) }, E.entries))
        assertEquals(enumVal, standaloneValue.decodeEnumOrNull(message { intOrNull(1, "val", enumVal.ordinal) }, E.entries))

        assertEquals(booleanListVal, standaloneValue.decodeBooleanList(message { booleanList(1, "val", booleanListVal) }))
        assertEquals(null, standaloneValue.decodeBooleanListOrNull(message { booleanListOrNull(1, "val", null) }))
        assertEquals(booleanListVal, standaloneValue.decodeBooleanListOrNull(message { booleanListOrNull(1, "val", booleanListVal) }))

        assertEquals(intListVal, standaloneValue.decodeIntList(message { intList(1, "val", intListVal) }))
        assertEquals(null, standaloneValue.decodeIntListOrNull(message { intListOrNull(1, "val", null) }))
        assertEquals(intListVal, standaloneValue.decodeIntListOrNull(message { intListOrNull(1, "val", intListVal) }))

        assertEquals(longListVal, standaloneValue.decodeLongList(message { longList(1, "val", longListVal) }))
        assertEquals(null, standaloneValue.decodeLongListOrNull(message { longListOrNull(1, "val", null) }))
        assertEquals(longListVal, standaloneValue.decodeLongListOrNull(message { longListOrNull(1, "val", longListVal) }))

        assertEquals(stringListVal, standaloneValue.decodeStringList(message { stringList(1, "val", stringListVal) }))
        assertEquals(null, standaloneValue.decodeStringListOrNull(message { stringListOrNull(1, "val", null) }))
        assertEquals(stringListVal, standaloneValue.decodeStringListOrNull(message { stringListOrNull(1, "val", stringListVal) }))

        standaloneValue.decodeByteArrayList(message { byteArrayList(1, "val", byteArrayListVal) }).forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }
        assertEquals(null, standaloneValue.decodeByteArrayListOrNull(message { byteArrayListOrNull(1, "val", null) }))
        standaloneValue.decodeByteArrayListOrNull(message { byteArrayListOrNull(1, "val", byteArrayListVal) }) !!.forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }

        assertEquals(uuidListVal, standaloneValue.decodeUuidList(message { uuidList(1, "val", uuidListVal) }))
        assertEquals(null, standaloneValue.decodeUuidListOrNull(message { uuidListOrNull(1, "val", null) }))
        assertEquals(uuidListVal, standaloneValue.decodeUuidListOrNull(message { uuidListOrNull(1, "val", uuidListVal) }))

        assertEquals(instanceListVal, standaloneValue.decodeInstanceList(message { instanceList(1, "val", B, instanceListVal) }, B))
        assertEquals(null, standaloneValue.decodeInstanceListOrNull(message { instanceListOrNull(1, "val", B, null) }, B))
        assertEquals(instanceListVal, standaloneValue.decodeInstanceListOrNull(message { instanceListOrNull(1, "val", B, instanceListVal) }, B))

        assertEquals(enumListVal, standaloneValue.decodeEnumList(message { intList(1, "val", enumListToOrdinals(enumListVal)) }, E.entries))
        assertEquals(null, standaloneValue.decodeEnumListOrNull(message { intListOrNull(1, "val", null) }, E.entries))
        assertEquals(enumListVal, standaloneValue.decodeEnumListOrNull(message { intListOrNull(1, "val", enumListToOrdinals(enumListVal)) }, E.entries))
    }
}