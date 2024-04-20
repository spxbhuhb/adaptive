package hu.simplexion.z2.wireformat

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

abstract class AbstractBuilderTest(
    private val serializationConfig: SerializationConfig
) : TestValues() {

    @Test
    fun testBuilder() {
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

            .float(fieldNumber ++, "floatVal", floatVal)
            .floatOrNull(fieldNumber ++, "floatValOrNull", floatVal)
            .floatOrNull(fieldNumber ++, "floatNullVal", null)

            .double(fieldNumber ++, "doubleVal", doubleVal)
            .doubleOrNull(fieldNumber ++, "doubleValOrNull", doubleVal)
            .doubleOrNull(fieldNumber ++, "doubleNullVal", null)

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

            .floatList(fieldNumber ++, "floatListEmptyVal", emptyList())
            .floatList(fieldNumber ++, "floatListVal", floatListVal)
            .floatListOrNull(fieldNumber ++, "floatListValOrNull", floatListVal)
            .floatListOrNull(fieldNumber ++, "floatListNullVal", null)

            .doubleList(fieldNumber ++, "doubleListEmptyVal", emptyList())
            .doubleList(fieldNumber ++, "doubleListVal", doubleListVal)
            .doubleListOrNull(fieldNumber ++, "doubleListValOrNull", doubleListVal)
            .doubleListOrNull(fieldNumber ++, "doubleListNullVal", null)

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

        val wireFormat = builder.pack()
        val message = serializationConfig.toMessage(wireFormat)

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

        assertEquals(floatVal, message.float(fieldNumber ++, "floatVal"))
        assertEquals(floatVal, message.floatOrNull(fieldNumber ++, "floatValOrNull"))
        assertEquals(null, message.floatOrNull(fieldNumber ++, "floatNullVal"))

        assertEquals(doubleVal, message.double(fieldNumber ++, "doubleVal"))
        assertEquals(doubleVal, message.doubleOrNull(fieldNumber ++, "doubleValOrNull"))
        assertEquals(null, message.doubleOrNull(fieldNumber ++, "doubleNullVal"))

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

        assertContentEquals(emptyList(), message.floatList(fieldNumber ++, "floatListEmptyVal"))
        assertContentEquals(floatListVal, message.floatList(fieldNumber ++, "floatListVal"))
        assertContentEquals(floatListVal, message.floatListOrNull(fieldNumber ++, "floatListValOrNull"))
        assertEquals(null, message.floatListOrNull(fieldNumber ++, "floatListNullVal"))

        assertContentEquals(emptyList(), message.doubleList(fieldNumber ++, "doubleListEmptyVal"))
        assertContentEquals(doubleListVal, message.doubleList(fieldNumber ++, "doubleListVal"))
        assertContentEquals(doubleListVal, message.doubleListOrNull(fieldNumber ++, "doubleListValOrNull"))
        assertEquals(null, message.doubleListOrNull(fieldNumber ++, "doubleListNullVal"))

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

}