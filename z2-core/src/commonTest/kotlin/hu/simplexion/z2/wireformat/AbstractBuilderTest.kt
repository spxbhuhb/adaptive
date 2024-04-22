package hu.simplexion.z2.wireformat

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

abstract class AbstractBuilderTest(
    private val wireFormatProvider: WireFormatProvider
) : TestValues() {

    @Test
    fun testUnit() {
        var fieldNumber = 1

        val builder = wireFormatProvider.messageBuilder()
            .startInstance()

            .unit(fieldNumber ++, "unitVal", unitVal)
            .unitOrNull(fieldNumber ++, "unitValOrNull", unitVal)
            .unitOrNull(fieldNumber ++, "unitNullVal", null)

            .unitList(fieldNumber ++, "unitListEmptyVal", emptyList())
            .unitList(fieldNumber ++, "unitListVal", unitListVal)
            .unitListOrNull(fieldNumber ++, "unitListValOrNull", unitListVal)
            .unitListOrNull(fieldNumber, "unitListNullVal", null)

            .endInstance()

        val wireFormat = builder.pack()
        val message = wireFormatProvider.toMessage(wireFormat)

        fieldNumber = 1

        assertEquals(unitVal, message.unit(fieldNumber ++, "unitVal"))
        assertEquals(unitVal, message.unitOrNull(fieldNumber ++, "unitValOrNull"))
        assertEquals(null, message.unitOrNull(fieldNumber ++, "unitNullVal"))

        assertContentEquals(emptyList(), message.unitList(fieldNumber ++, "unitListEmptyVal"))
        assertContentEquals(unitListVal, message.unitList(fieldNumber ++, "unitListVal"))
        assertContentEquals(unitListVal, message.unitListOrNull(fieldNumber ++, "unitListValOrNull"))
        assertEquals(null, message.unitListOrNull(fieldNumber, "unitListNullVal"))
    }
    
    @Test
    fun testBoolean() {
        var fieldNumber = 1

        val builder = wireFormatProvider.messageBuilder()
            .startInstance()

            .boolean(fieldNumber ++, "booleanVal", booleanVal)
            .booleanOrNull(fieldNumber ++, "booleanValOrNull", booleanVal)
            .booleanOrNull(fieldNumber ++, "booleanNullVal", null)

            .booleanList(fieldNumber ++, "booleanListEmptyVal", emptyList())
            .booleanList(fieldNumber ++, "booleanListVal", booleanListVal)
            .booleanListOrNull(fieldNumber ++, "booleanListValOrNull", booleanListVal)
            .booleanListOrNull(fieldNumber, "booleanListNullVal", null)

            .endInstance()

        val wireFormat = builder.pack()
        val message = wireFormatProvider.toMessage(wireFormat)

        fieldNumber = 1

        assertEquals(booleanVal, message.boolean(fieldNumber ++, "booleanVal"))
        assertEquals(booleanVal, message.booleanOrNull(fieldNumber ++, "booleanValOrNull"))
        assertEquals(null, message.booleanOrNull(fieldNumber ++, "booleanNullVal"))

        assertContentEquals(emptyList(), message.booleanList(fieldNumber ++, "booleanListEmptyVal"))
        assertContentEquals(booleanListVal, message.booleanList(fieldNumber ++, "booleanListVal"))
        assertContentEquals(booleanListVal, message.booleanListOrNull(fieldNumber ++, "booleanListValOrNull"))
        assertEquals(null, message.booleanListOrNull(fieldNumber, "booleanListNullVal"))
    }

    @Test
    fun testInt() {
        var fieldNumber = 1

        val builder = wireFormatProvider.messageBuilder()
            .startInstance()

            .int(fieldNumber ++, "intVal", intVal)
            .intOrNull(fieldNumber ++, "intValOrNull", intVal)
            .intOrNull(fieldNumber ++, "intNullVal", null)

            .intList(fieldNumber ++, "intListEmptyVal", emptyList())
            .intList(fieldNumber ++, "intListVal", intListVal)
            .intListOrNull(fieldNumber ++, "intListValOrNull", intListVal)
            .intListOrNull(fieldNumber, "intListNullVal", null)

            .endInstance()

        val wireFormat = builder.pack()
        val message = wireFormatProvider.toMessage(wireFormat)

        fieldNumber = 1

        assertEquals(intVal, message.int(fieldNumber ++, "intVal"))
        assertEquals(intVal, message.intOrNull(fieldNumber ++, "intValOrNull"))
        assertEquals(null, message.intOrNull(fieldNumber ++, "intNullVal"))

        assertContentEquals(emptyList(), message.intList(fieldNumber ++, "intListEmptyVal"))
        assertContentEquals(intListVal, message.intList(fieldNumber ++, "intListVal"))
        assertContentEquals(intListVal, message.intListOrNull(fieldNumber ++, "intListValOrNull"))
        assertEquals(null, message.intListOrNull(fieldNumber, "intListNullVal"))
    }

    @Test
    fun testShort() {
        var fieldNumber = 1

        val builder = wireFormatProvider.messageBuilder()
            .startInstance()

            .short(fieldNumber ++, "shortVal", shortVal)
            .shortOrNull(fieldNumber ++, "shortValOrNull", shortVal)
            .shortOrNull(fieldNumber ++, "shortNullVal", null)

            .shortList(fieldNumber ++, "shortListEmptyVal", emptyList())
            .shortList(fieldNumber ++, "shortListVal", shortListVal)
            .shortListOrNull(fieldNumber ++, "shortListValOrNull", shortListVal)
            .shortListOrNull(fieldNumber, "shortListNullVal", null)

            .endInstance()

        val wireFormat = builder.pack()
        val message = wireFormatProvider.toMessage(wireFormat)

        fieldNumber = 1

        assertEquals(shortVal, message.short(fieldNumber ++, "shortVal"))
        assertEquals(shortVal, message.shortOrNull(fieldNumber ++, "shortValOrNull"))
        assertEquals(null, message.shortOrNull(fieldNumber ++, "shortNullVal"))

        assertContentEquals(emptyList(), message.shortList(fieldNumber ++, "shortListEmptyVal"))
        assertContentEquals(shortListVal, message.shortList(fieldNumber ++, "shortListVal"))
        assertContentEquals(shortListVal, message.shortListOrNull(fieldNumber ++, "shortListValOrNull"))
        assertEquals(null, message.shortListOrNull(fieldNumber, "shortListNullVal"))
    }

    @Test
    fun testByte() {
        var fieldNumber = 1

        val builder = wireFormatProvider.messageBuilder()
            .startInstance()

            .byte(fieldNumber ++, "byteVal", byteVal)
            .byteOrNull(fieldNumber ++, "byteValOrNull", byteVal)
            .byteOrNull(fieldNumber ++, "byteNullVal", null)

            .byteList(fieldNumber ++, "byteListEmptyVal", emptyList())
            .byteList(fieldNumber ++, "byteListVal", byteListVal)
            .byteListOrNull(fieldNumber ++, "byteListValOrNull", byteListVal)
            .byteListOrNull(fieldNumber, "byteListNullVal", null)

            .endInstance()

        val wireFormat = builder.pack()
        val message = wireFormatProvider.toMessage(wireFormat)

        fieldNumber = 1

        assertEquals(byteVal, message.byte(fieldNumber ++, "byteVal"))
        assertEquals(byteVal, message.byteOrNull(fieldNumber ++, "byteValOrNull"))
        assertEquals(null, message.byteOrNull(fieldNumber ++, "byteNullVal"))

        assertContentEquals(emptyList(), message.byteList(fieldNumber ++, "byteListEmptyVal"))
        assertContentEquals(byteListVal, message.byteList(fieldNumber ++, "byteListVal"))
        assertContentEquals(byteListVal, message.byteListOrNull(fieldNumber ++, "byteListValOrNull"))
        assertEquals(null, message.byteListOrNull(fieldNumber, "byteListNullVal"))
    }

    @Test
    fun testLong() {
        var fieldNumber = 1

        val builder = wireFormatProvider.messageBuilder()
            .startInstance()

            .long(fieldNumber ++, "longVal", longVal)
            .longOrNull(fieldNumber ++, "longValOrNull", longVal)
            .longOrNull(fieldNumber ++, "longNullVal", null)

            .longList(fieldNumber ++, "longListEmptyVal", emptyList())
            .longList(fieldNumber ++, "longListVal", longListVal)
            .longListOrNull(fieldNumber ++, "longListValOrNull", longListVal)
            .longListOrNull(fieldNumber, "longListNullVal", null)

            .endInstance()

        val wireFormat = builder.pack()
        val message = wireFormatProvider.toMessage(wireFormat)

        fieldNumber = 1

        assertEquals(longVal, message.long(fieldNumber ++, "longVal"))
        assertEquals(longVal, message.longOrNull(fieldNumber ++, "longValOrNull"))
        assertEquals(null, message.longOrNull(fieldNumber ++, "longNullVal"))

        assertContentEquals(emptyList(), message.longList(fieldNumber ++, "longListEmptyVal"))
        assertContentEquals(longListVal, message.longList(fieldNumber ++, "longListVal"))
        assertContentEquals(longListVal, message.longListOrNull(fieldNumber ++, "longListValOrNull"))
        assertEquals(null, message.longListOrNull(fieldNumber, "longListNullVal"))
    }

    @Test
    fun testFloat() {
        var fieldNumber = 1

        val builder = wireFormatProvider.messageBuilder()
            .startInstance()


            .float(fieldNumber ++, "floatVal", floatVal)
            .floatOrNull(fieldNumber ++, "floatValOrNull", floatVal)
            .floatOrNull(fieldNumber ++, "floatNullVal", null)

            .floatList(fieldNumber ++, "floatListEmptyVal", emptyList())
            .floatList(fieldNumber ++, "floatListVal", floatListVal)
            .floatListOrNull(fieldNumber ++, "floatListValOrNull", floatListVal)
            .floatListOrNull(fieldNumber, "floatListNullVal", null)


            .endInstance()

        val wireFormat = builder.pack()
        val message = wireFormatProvider.toMessage(wireFormat)

        fieldNumber = 1

        assertEquals(floatVal, message.float(fieldNumber ++, "floatVal"))
        assertEquals(floatVal, message.floatOrNull(fieldNumber ++, "floatValOrNull"))
        assertEquals(null, message.floatOrNull(fieldNumber ++, "floatNullVal"))

        assertContentEquals(emptyList(), message.floatList(fieldNumber ++, "floatListEmptyVal"))
        assertContentEquals(floatListVal, message.floatList(fieldNumber ++, "floatListVal"))
        assertContentEquals(floatListVal, message.floatListOrNull(fieldNumber ++, "floatListValOrNull"))
        assertEquals(null, message.floatListOrNull(fieldNumber, "floatListNullVal"))

    }

    @Test
    fun testDouble() {
        var fieldNumber = 1

        val builder = wireFormatProvider.messageBuilder()
            .startInstance()

            .double(fieldNumber ++, "doubleVal", doubleVal)
            .doubleOrNull(fieldNumber ++, "doubleValOrNull", doubleVal)
            .doubleOrNull(fieldNumber ++, "doubleNullVal", null)

            .doubleList(fieldNumber ++, "doubleListEmptyVal", emptyList())
            .doubleList(fieldNumber ++, "doubleListVal", doubleListVal)
            .doubleListOrNull(fieldNumber ++, "doubleListValOrNull", doubleListVal)
            .doubleListOrNull(fieldNumber, "doubleListNullVal", null)

            .endInstance()

        val wireFormat = builder.pack()
        val message = wireFormatProvider.toMessage(wireFormat)

        fieldNumber = 1

        assertEquals(doubleVal, message.double(fieldNumber ++, "doubleVal"))
        assertEquals(doubleVal, message.doubleOrNull(fieldNumber ++, "doubleValOrNull"))
        assertEquals(null, message.doubleOrNull(fieldNumber ++, "doubleNullVal"))

        assertContentEquals(emptyList(), message.doubleList(fieldNumber ++, "doubleListEmptyVal"))
        assertContentEquals(doubleListVal, message.doubleList(fieldNumber ++, "doubleListVal"))
        assertContentEquals(doubleListVal, message.doubleListOrNull(fieldNumber ++, "doubleListValOrNull"))
        assertEquals(null, message.doubleListOrNull(fieldNumber, "doubleListNullVal"))
    }

    @Test
    fun testChar() {
        var fieldNumber = 1

        val builder = wireFormatProvider.messageBuilder()
            .startInstance()

            .char(fieldNumber ++, "charVal", charVal)
            .charOrNull(fieldNumber ++, "charValOrNull", charVal)
            .charOrNull(fieldNumber ++, "charNullVal", null)

            .charList(fieldNumber ++, "charListEmptyVal", emptyList())
            .charList(fieldNumber ++, "charListVal", charListVal)
            .charListOrNull(fieldNumber ++, "charListValOrNull", charListVal)
            .charListOrNull(fieldNumber, "charListNullVal", null)

            .endInstance()

        val wireFormat = builder.pack()
        val message = wireFormatProvider.toMessage(wireFormat)

        fieldNumber = 1

        assertEquals(charVal, message.char(fieldNumber ++, "charVal"))
        assertEquals(charVal, message.charOrNull(fieldNumber ++, "charValOrNull"))
        assertEquals(null, message.charOrNull(fieldNumber ++, "charNullVal"))

        assertContentEquals(emptyList(), message.charList(fieldNumber ++, "charListEmptyVal"))
        assertContentEquals(charListVal, message.charList(fieldNumber ++, "charListVal"))
        assertContentEquals(charListVal, message.charListOrNull(fieldNumber ++, "charListValOrNull"))
        assertEquals(null, message.charListOrNull(fieldNumber, "charListNullVal"))
    }

    @Test
    fun testString() {
        var fieldNumber = 1

        val builder = wireFormatProvider.messageBuilder()
            .startInstance()

            .string(fieldNumber ++, "stringVal", stringVal)
            .stringOrNull(fieldNumber ++, "stringValOrNull", stringVal)
            .stringOrNull(fieldNumber ++, "stringNullVal", null)

            .stringList(fieldNumber ++, "stringListEmptyVal", emptyList())
            .stringList(fieldNumber ++, "stringListVal", stringListVal)
            .stringListOrNull(fieldNumber ++, "stringListValOrNull", stringListVal)
            .stringListOrNull(fieldNumber, "stringListNullVal", null)


            .endInstance()

        val wireFormat = builder.pack()
        val message = wireFormatProvider.toMessage(wireFormat)

        fieldNumber = 1

        assertEquals(stringVal, message.string(fieldNumber ++, "stringVal"))
        assertEquals(stringVal, message.stringOrNull(fieldNumber ++, "stringValOrNull"))
        assertEquals(null, message.stringOrNull(fieldNumber ++, "stringNullVal"))

        assertContentEquals(emptyList(), message.stringList(fieldNumber ++, "stringListEmptyVal"))
        assertContentEquals(stringListVal, message.stringList(fieldNumber ++, "stringListVal"))
        assertContentEquals(stringListVal, message.stringListOrNull(fieldNumber ++, "stringListValOrNull"))
        assertEquals(null, message.stringListOrNull(fieldNumber, "stringListNullVal"))
    }

    @Test
    fun testByteArray() {
        var fieldNumber = 1

        val builder = wireFormatProvider.messageBuilder()
            .startInstance()

            .byteArray(fieldNumber ++, "byteArrayVal", byteArrayVal)
            .byteArrayOrNull(fieldNumber ++, "byteArrayValOrNull", byteArrayVal)
            .byteArrayOrNull(fieldNumber ++, "byteArrayNullVal", null)

            .byteArrayList(fieldNumber ++, "byteArrayListEmptyVal", emptyList())
            .byteArrayList(fieldNumber ++, "byteArrayListVal", byteArrayListVal)
            .byteArrayListOrNull(fieldNumber ++, "byteArrayListValOrNull", byteArrayListVal)
            .byteArrayListOrNull(fieldNumber, "byteArrayListNullVal", null)

            .endInstance()

        val wireFormat = builder.pack()
        val message = wireFormatProvider.toMessage(wireFormat)

        fieldNumber = 1

        assertContentEquals(byteArrayVal, message.byteArray(fieldNumber ++, "byteArrayVal"))
        assertContentEquals(byteArrayVal, message.byteArrayOrNull(fieldNumber ++, "byteArrayValOrNull"))
        assertEquals(null, message.byteArrayOrNull(fieldNumber ++, "byteArrayNullVal"))

        assertContentEquals(emptyList(), message.byteArrayList(fieldNumber ++, "byteArrayListEmptyVal"))
        message.byteArrayList(fieldNumber ++, "byteArrayListVal").forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }
        assertNotNull(
            message.byteArrayListOrNull(fieldNumber ++, "byteArrayListValOrNull")
        ).forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }
        assertEquals(null, message.byteArrayListOrNull(fieldNumber, "byteArrayListNullVal"))
    }

    @Test
    fun testUuid() {
        var fieldNumber = 1

        val builder = wireFormatProvider.messageBuilder()
            .startInstance()

            .uuid(fieldNumber ++, "uuidVal", uuidVal)
            .uuidOrNull(fieldNumber ++, "uuidValOrNull", uuidVal)
            .uuidOrNull(fieldNumber ++, "uuidNullVal", null)

            .uuidList(fieldNumber ++, "uuidListEmptyVal", emptyList())
            .uuidList(fieldNumber ++, "uuidListVal", uuidListVal)
            .uuidListOrNull(fieldNumber ++, "uuidListValOrNull", uuidListVal)
            .uuidListOrNull(fieldNumber, "uuidListNullVal", null)


            .endInstance()

        val wireFormat = builder.pack()
        val message = wireFormatProvider.toMessage(wireFormat)

        fieldNumber = 1

        assertEquals(uuidVal, message.uuid(fieldNumber ++, "uuidVal"))
        assertEquals(uuidVal, message.uuidOrNull(fieldNumber ++, "uuidValOrNull"))
        assertEquals(null, message.uuidOrNull<Any>(fieldNumber ++, "uuidNullVal"))

        assertContentEquals(emptyList(), message.uuidList<Any>(fieldNumber ++, "uuidListEmptyVal"))
        assertContentEquals(uuidListVal, message.uuidList(fieldNumber ++, "uuidListVal"))
        assertContentEquals(uuidListVal, message.uuidListOrNull(fieldNumber ++, "uuidListValOrNull"))
        assertEquals(null, message.uuidListOrNull<Any>(fieldNumber, "uuidListNullVal"))
    }

    @Test
    fun testInstance() {
        var fieldNumber = 1

        val builder = wireFormatProvider.messageBuilder()
            .startInstance()

            .instance(fieldNumber ++, "instanceVal", A, instanceVal)
            .instanceOrNull(fieldNumber ++, "instanceValOrNull", A, instanceVal)
            .instanceOrNull(fieldNumber ++, "instanceNullVal", A, null)

            .instanceList(fieldNumber ++, "instanceListEmptyVal", B, emptyList())
            .instanceList(fieldNumber ++, "instanceListVal", B, instanceListVal)
            .instanceListOrNull(fieldNumber ++, "instanceListValOrNull", B, instanceListVal)
            .instanceListOrNull(fieldNumber, "instanceListNullVal", B, null)

            .endInstance()

        val wireFormat = builder.pack()
        val message = wireFormatProvider.toMessage(wireFormat)

        fieldNumber = 1

        assertEquals(instanceVal, message.instance(fieldNumber ++, "instanceVal", A))
        assertEquals(instanceVal, message.instanceOrNull(fieldNumber ++, "instanceValOrNull", A))
        assertEquals(null, message.instanceOrNull(fieldNumber ++, "instanceNullVal", A))

        assertContentEquals(emptyList(), message.instanceList(fieldNumber ++, "instanceListEmptyVal", B))
        assertContentEquals(instanceListVal, message.instanceList(fieldNumber ++, "instanceListVal", B))
        assertContentEquals(instanceListVal, message.instanceListOrNull(fieldNumber ++, "instanceListValOrNull", B))
        assertEquals(null, message.instanceListOrNull(fieldNumber, "instanceListNullVal", B))
    }

    @Test
    fun testUInt() {
        var fieldNumber = 1

        val builder = wireFormatProvider.messageBuilder()
            .startInstance()

            .uInt(fieldNumber ++, "uIntVal", uIntVal)
            .uIntOrNull(fieldNumber ++, "uIntValOrNull", uIntVal)
            .uIntOrNull(fieldNumber ++, "uIntNullVal", null)

            .uIntList(fieldNumber ++, "uIntListEmptyVal", emptyList())
            .uIntList(fieldNumber ++, "uIntListVal", uIntListVal)
            .uIntListOrNull(fieldNumber ++, "uIntListValOrNull", uIntListVal)
            .uIntListOrNull(fieldNumber, "uIntListNullVal", null)

            .endInstance()

        val wireFormat = builder.pack()
        val message = wireFormatProvider.toMessage(wireFormat)

        fieldNumber = 1

        assertEquals(uIntVal, message.uInt(fieldNumber ++, "uIntVal"))
        assertEquals(uIntVal, message.uIntOrNull(fieldNumber ++, "uIntValOrNull"))
        assertEquals(null, message.uIntOrNull(fieldNumber ++, "uIntNullVal"))

        assertContentEquals(emptyList(), message.uIntList(fieldNumber ++, "uIntListEmptyVal"))
        assertContentEquals(uIntListVal, message.uIntList(fieldNumber ++, "uIntListVal"))
        assertContentEquals(uIntListVal, message.uIntListOrNull(fieldNumber ++, "uIntListValOrNull"))
        assertEquals(null, message.uIntListOrNull(fieldNumber, "uIntListNullVal"))
    }

    @Test
    fun testUShort() {
        var fieldNumber = 1

        val builder = wireFormatProvider.messageBuilder()
            .startInstance()

            .uShort(fieldNumber ++, "uShortVal", uShortVal)
            .uShortOrNull(fieldNumber ++, "uShortValOrNull", uShortVal)
            .uShortOrNull(fieldNumber ++, "uShortNullVal", null)

            .uShortList(fieldNumber ++, "uShortListEmptyVal", emptyList())
            .uShortList(fieldNumber ++, "uShortListVal", uShortListVal)
            .uShortListOrNull(fieldNumber ++, "uShortListValOrNull", uShortListVal)
            .uShortListOrNull(fieldNumber, "uShortListNullVal", null)

            .endInstance()

        val wireFormat = builder.pack()
        val message = wireFormatProvider.toMessage(wireFormat)

        fieldNumber = 1

        assertEquals(uShortVal, message.uShort(fieldNumber ++, "uShortVal"))
        assertEquals(uShortVal, message.uShortOrNull(fieldNumber ++, "uShortValOrNull"))
        assertEquals(null, message.uShortOrNull(fieldNumber ++, "uShortNullVal"))

        assertContentEquals(emptyList(), message.uShortList(fieldNumber ++, "uShortListEmptyVal"))
        assertContentEquals(uShortListVal, message.uShortList(fieldNumber ++, "uShortListVal"))
        assertContentEquals(uShortListVal, message.uShortListOrNull(fieldNumber ++, "uShortListValOrNull"))
        assertEquals(null, message.uShortListOrNull(fieldNumber, "uShortListNullVal"))
    }

    @Test
    fun testUByte() {
        var fieldNumber = 1

        val builder = wireFormatProvider.messageBuilder()
            .startInstance()

            .uByte(fieldNumber ++, "uByteVal", uByteVal)
            .uByteOrNull(fieldNumber ++, "uByteValOrNull", uByteVal)
            .uByteOrNull(fieldNumber ++, "uByteNullVal", null)

            .uByteList(fieldNumber ++, "uByteListEmptyVal", emptyList())
            .uByteList(fieldNumber ++, "uByteListVal", uByteListVal)
            .uByteListOrNull(fieldNumber ++, "uByteListValOrNull", uByteListVal)
            .uByteListOrNull(fieldNumber, "uByteListNullVal", null)

            .endInstance()

        val wireFormat = builder.pack()
        val message = wireFormatProvider.toMessage(wireFormat)

        fieldNumber = 1

        assertEquals(uByteVal, message.uByte(fieldNumber ++, "uByteVal"))
        assertEquals(uByteVal, message.uByteOrNull(fieldNumber ++, "uByteValOrNull"))
        assertEquals(null, message.uByteOrNull(fieldNumber ++, "uByteNullVal"))

        assertContentEquals(emptyList(), message.uByteList(fieldNumber ++, "uByteListEmptyVal"))
        assertContentEquals(uByteListVal, message.uByteList(fieldNumber ++, "uByteListVal"))
        assertContentEquals(uByteListVal, message.uByteListOrNull(fieldNumber ++, "uByteListValOrNull"))
        assertEquals(null, message.uByteListOrNull(fieldNumber, "uByteListNullVal"))
    }

    @Test
    fun testULong() {
        var fieldNumber = 1

        val builder = wireFormatProvider.messageBuilder()
            .startInstance()

            .uLong(fieldNumber ++, "uLongVal", uLongVal)
            .uLongOrNull(fieldNumber ++, "uLongValOrNull", uLongVal)
            .uLongOrNull(fieldNumber ++, "uLongNullVal", null)

            .uLongList(fieldNumber ++, "uLongListEmptyVal", emptyList())
            .uLongList(fieldNumber ++, "uLongListVal", uLongListVal)
            .uLongListOrNull(fieldNumber ++, "uLongListValOrNull", uLongListVal)
            .uLongListOrNull(fieldNumber, "uLongListNullVal", null)

            .endInstance()

        val wireFormat = builder.pack()
        val message = wireFormatProvider.toMessage(wireFormat)

        fieldNumber = 1

        assertEquals(uLongVal, message.uLong(fieldNumber ++, "uLongVal"))
        assertEquals(uLongVal, message.uLongOrNull(fieldNumber ++, "uLongValOrNull"))
        assertEquals(null, message.uLongOrNull(fieldNumber ++, "uLongNullVal"))

        assertContentEquals(emptyList(), message.uLongList(fieldNumber ++, "uLongListEmptyVal"))
        assertContentEquals(uLongListVal, message.uLongList(fieldNumber ++, "uLongListVal"))
        assertContentEquals(uLongListVal, message.uLongListOrNull(fieldNumber ++, "uLongListValOrNull"))
        assertEquals(null, message.uLongListOrNull(fieldNumber, "uLongListNullVal"))
    }
}