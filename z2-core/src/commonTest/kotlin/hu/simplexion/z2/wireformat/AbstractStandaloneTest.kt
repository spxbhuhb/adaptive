package hu.simplexion.z2.wireformat

import hu.simplexion.z2.utility.UUID
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

abstract class AbstractStandaloneTest(
    private val wireFormatProvider: WireFormatProvider
) : TestValues() {

    val sv = wireFormatProvider.standalone()

    fun message(wireFormat: () -> ByteArray): Message =
        wireFormatProvider.toMessage(wireFormat())

    @Test
    fun testUnit() {
        assertEquals(unitVal, sv.decodeUnit(message { sv.encodeUnit(unitVal) }))
        assertEquals(null, sv.decodeUnitOrNull(message { sv.encodeUnit(null) }))
        assertEquals(unitVal, sv.decodeUnitOrNull(message { sv.encodeUnit(unitVal) }))

        assertEquals(unitListVal, sv.decodeUnitList(message { sv.encodeUnitList(unitListVal) }))
        assertEquals(null, sv.decodeUnitListOrNull(message { sv.encodeUnitList(null) }))
        assertEquals(unitListVal, sv.decodeUnitListOrNull(message { sv.encodeUnitList(unitListVal) }))
    }

    @Test
    fun testBoolean() {
        assertEquals(booleanVal, sv.decodeBoolean(message { sv.encodeBoolean(booleanVal) }))
        assertEquals(null, sv.decodeBooleanOrNull(message { sv.encodeBoolean(null) }))
        assertEquals(booleanVal, sv.decodeBooleanOrNull(message { sv.encodeBoolean(booleanVal) }))

        assertEquals(booleanListVal, sv.decodeBooleanList(message { sv.encodeBooleanList(booleanListVal) }))
        assertEquals(null, sv.decodeBooleanListOrNull(message { sv.encodeBooleanList(null) }))
        assertEquals(booleanListVal, sv.decodeBooleanListOrNull(message { sv.encodeBooleanList(booleanListVal) }))
    }

    @Test
    fun testInt() {
        assertEquals(intVal, sv.decodeInt(message { sv.encodeInt(intVal) }))
        assertEquals(null, sv.decodeIntOrNull(message { sv.encodeInt(null) }))
        assertEquals(intVal, sv.decodeIntOrNull(message { sv.encodeInt(intVal) }))

        assertEquals(intListVal, sv.decodeIntList(message { sv.encodeIntList(intListVal) }))
        assertEquals(null, sv.decodeIntListOrNull(message { sv.encodeIntList(null) }))
        assertEquals(intListVal, sv.decodeIntListOrNull(message { sv.encodeIntList(intListVal) }))
    }

    @Test
    fun testShort() {
        assertEquals(shortVal, sv.decodeShort(message { sv.encodeShort(shortVal) }))
        assertEquals(null, sv.decodeShortOrNull(message { sv.encodeShort(null) }))
        assertEquals(shortVal, sv.decodeShortOrNull(message { sv.encodeShort(shortVal) }))

        assertEquals(shortListVal, sv.decodeShortList(message { sv.encodeShortList(shortListVal) }))
        assertEquals(null, sv.decodeShortListOrNull(message { sv.encodeShortList(null) }))
        assertEquals(shortListVal, sv.decodeShortListOrNull(message { sv.encodeShortList(shortListVal) }))
    }

    @Test
    fun testByte() {
        assertEquals(byteVal, sv.decodeByte(message { sv.encodeByte(byteVal) }))
        assertEquals(null, sv.decodeByteOrNull(message { sv.encodeByte(null) }))
        assertEquals(byteVal, sv.decodeByteOrNull(message { sv.encodeByte(byteVal) }))

        assertEquals(byteListVal, sv.decodeByteList(message { sv.encodeByteList(byteListVal) }))
        assertEquals(null, sv.decodeByteListOrNull(message { sv.encodeByteList(null) }))
        assertEquals(byteListVal, sv.decodeByteListOrNull(message { sv.encodeByteList(byteListVal) }))
    }

    @Test
    fun testLong() {
        assertEquals(longVal, sv.decodeLong(message { sv.encodeLong(longVal) }))
        assertEquals(null, sv.decodeLongOrNull(message { sv.encodeLong(null) }))
        assertEquals(longVal, sv.decodeLongOrNull(message { sv.encodeLong(longVal) }))

        assertEquals(longListVal, sv.decodeLongList(message { sv.encodeLongList(longListVal) }))
        assertEquals(null, sv.decodeLongListOrNull(message { sv.encodeLongList(null) }))
        assertEquals(longListVal, sv.decodeLongListOrNull(message { sv.encodeLongList(longListVal) }))
    }

    @Test
    fun testFloat() {
        assertEquals(floatVal, sv.decodeFloat(message { sv.encodeFloat(floatVal) }))
        assertEquals(null, sv.decodeFloatOrNull(message { sv.encodeFloat(null) }))
        assertEquals(floatVal, sv.decodeFloatOrNull(message { sv.encodeFloat(floatVal) }))

        assertEquals(floatListVal, sv.decodeFloatList(message { sv.encodeFloatList(floatListVal) }))
        assertEquals(null, sv.decodeFloatListOrNull(message { sv.encodeFloatList(null) }))
        assertEquals(floatListVal, sv.decodeFloatListOrNull(message { sv.encodeFloatList(floatListVal) }))
    }

    @Test
    fun testDouble() {
        assertEquals(doubleVal, sv.decodeDouble(message { sv.encodeDouble(doubleVal) }))
        assertEquals(null, sv.decodeDoubleOrNull(message { sv.encodeDouble(null) }))
        assertEquals(doubleVal, sv.decodeDoubleOrNull(message { sv.encodeDouble(doubleVal) }))

        assertEquals(doubleListVal, sv.decodeDoubleList(message { sv.encodeDoubleList(doubleListVal) }))
        assertEquals(null, sv.decodeDoubleListOrNull(message { sv.encodeDoubleList(null) }))
        assertEquals(doubleListVal, sv.decodeDoubleListOrNull(message { sv.encodeDoubleList(doubleListVal) }))
    }

    @Test
    fun testChar() {
        assertEquals(charVal, sv.decodeChar(message { sv.encodeChar(charVal) }))
        assertEquals(null, sv.decodeCharOrNull(message { sv.encodeChar(null) }))
        assertEquals(charVal, sv.decodeCharOrNull(message { sv.encodeChar(charVal) }))

        assertEquals(charListVal, sv.decodeCharList(message { sv.encodeCharList(charListVal) }))
        assertEquals(null, sv.decodeCharListOrNull(message { sv.encodeCharList(null) }))
        assertEquals(charListVal, sv.decodeCharListOrNull(message { sv.encodeCharList(charListVal) }))
    }

    @Test
    fun testString() {
        assertEquals(stringVal, sv.decodeString(message { sv.encodeString(stringVal) }))
        assertEquals(null, sv.decodeStringOrNull(message { sv.encodeString(null) }))
        assertEquals(stringVal, sv.decodeStringOrNull(message { sv.encodeString(stringVal) }))

        assertEquals(stringListVal, sv.decodeStringList(message { sv.encodeStringList(stringListVal) }))
        assertEquals(null, sv.decodeStringListOrNull(message { sv.encodeStringList(null) }))
        assertEquals(stringListVal, sv.decodeStringListOrNull(message { sv.encodeStringList(stringListVal) }))
    }

    @Test
    fun testByteArray() {
        assertContentEquals(byteArrayVal, sv.decodeByteArray(message { sv.encodeByteArray(byteArrayVal) }))
        assertEquals(null, sv.decodeByteArrayOrNull(message { sv.encodeByteArray(null) }))
        assertContentEquals(byteArrayVal, sv.decodeByteArrayOrNull(message { sv.encodeByteArray(byteArrayVal) }))

        sv.decodeByteArrayList(message { sv.encodeByteArrayList(byteArrayListVal) }).forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }
        assertEquals(null, sv.decodeByteArrayListOrNull(message { sv.encodeByteArrayList(null) }))
        sv.decodeByteArrayListOrNull(message { sv.encodeByteArrayList(byteArrayListVal) }) !!.forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }
    }

    @Test
    fun testUuid() {
        assertEquals(uuidVal, sv.decodeUuid(message { sv.encodeUuid(uuidVal) }))
        assertEquals(null, sv.decodeUuidOrNull(message { sv.encodeUuid(null as UUID<Any>?) }))
        assertEquals(uuidVal, sv.decodeUuidOrNull(message { sv.encodeUuid(uuidVal) }))

        assertEquals(uuidListVal, sv.decodeUuidList(message { sv.encodeUuidList(uuidListVal) }))
        assertEquals(null, sv.decodeUuidListOrNull(message { sv.encodeUuidList(null) }))
        assertEquals(uuidListVal, sv.decodeUuidListOrNull(message { sv.encodeUuidList(uuidListVal) }))
    }

    @Test
    fun testInstance() {
        assertEquals(instanceVal, sv.decodeInstance(message { sv.encodeInstance(instanceVal, A) }, A))
        assertEquals(null, sv.decodeInstanceOrNull(message { sv.encodeInstance(null, A) }, A))
        assertEquals(instanceVal, sv.decodeInstanceOrNull(message { sv.encodeInstance(instanceVal, A) }, A))

        assertEquals(instanceListVal, sv.decodeInstanceList(message { sv.encodeInstanceList(instanceListVal, B) }, B))
        assertEquals(null, sv.decodeInstanceListOrNull(message { sv.encodeInstanceList(null, B) }, B))
        assertEquals(instanceListVal, sv.decodeInstanceListOrNull(message { sv.encodeInstanceList(instanceListVal, B) }, B))
    }

    @Test
    fun testEnum() {
        assertEquals(enumVal, sv.decodeEnum(message { sv.encodeEnum(enumVal, E.entries) }, E.entries))
        assertEquals(null, sv.decodeEnumOrNull(message { sv.encodeEnum(null, E.entries) }, E.entries))
        assertEquals(enumVal, sv.decodeEnumOrNull(message { sv.encodeEnum(enumVal, E.entries) }, E.entries))

        assertEquals(enumListVal, sv.decodeEnumList(message { sv.encodeEnumList(enumListVal, E.entries) }, E.entries))
        assertEquals(null, sv.decodeEnumListOrNull(message { sv.encodeEnumList(null, E.entries) }, E.entries))
        assertEquals(enumListVal, sv.decodeEnumListOrNull(message { sv.encodeEnumList(enumListVal, E.entries) }, E.entries))
    }

    @Test
    fun testUInt() {
        assertEquals(uIntVal, sv.decodeUInt(message { sv.encodeUInt(uIntVal) }))
        assertEquals(null, sv.decodeUIntOrNull(message { sv.encodeUInt(null) }))
        assertEquals(uIntVal, sv.decodeUIntOrNull(message { sv.encodeUInt(uIntVal) }))

        assertEquals(uIntListVal, sv.decodeUIntList(message { sv.encodeUIntList(uIntListVal) }))
        assertEquals(null, sv.decodeUIntListOrNull(message { sv.encodeUIntList(null) }))
        assertEquals(uIntListVal, sv.decodeUIntListOrNull(message { sv.encodeUIntList(uIntListVal) }))
    }

    @Test
    fun testUShort() {
        assertEquals(uShortVal, sv.decodeUShort(message { sv.encodeUShort(uShortVal) }))
        assertEquals(null, sv.decodeUShortOrNull(message { sv.encodeUShort(null) }))
        assertEquals(uShortVal, sv.decodeUShortOrNull(message { sv.encodeUShort(uShortVal) }))

        assertEquals(uShortListVal, sv.decodeUShortList(message { sv.encodeUShortList(uShortListVal) }))
        assertEquals(null, sv.decodeUShortListOrNull(message { sv.encodeUShortList(null) }))
        assertEquals(uShortListVal, sv.decodeUShortListOrNull(message { sv.encodeUShortList(uShortListVal) }))
    }

    @Test
    fun testUByte() {
        assertEquals(uByteVal, sv.decodeUByte(message { sv.encodeUByte(uByteVal) }))
        assertEquals(null, sv.decodeUByteOrNull(message { sv.encodeUByte(null) }))
        assertEquals(uByteVal, sv.decodeUByteOrNull(message { sv.encodeUByte(uByteVal) }))

        assertEquals(uByteListVal, sv.decodeUByteList(message { sv.encodeUByteList(uByteListVal) }))
        assertEquals(null, sv.decodeUByteListOrNull(message { sv.encodeUByteList(null) }))
        assertEquals(uByteListVal, sv.decodeUByteListOrNull(message { sv.encodeUByteList(uByteListVal) }))
    }

    @Test
    fun testULong() {
        assertEquals(uLongVal, sv.decodeULong(message { sv.encodeULong(uLongVal) }))
        assertEquals(null, sv.decodeULongOrNull(message { sv.encodeULong(null) }))
        assertEquals(uLongVal, sv.decodeULongOrNull(message { sv.encodeULong(uLongVal) }))

        assertEquals(uLongListVal, sv.decodeULongList(message { sv.encodeULongList(uLongListVal) }))
        assertEquals(null, sv.decodeULongListOrNull(message { sv.encodeULongList(null) }))
        assertEquals(uLongListVal, sv.decodeULongListOrNull(message { sv.encodeULongList(uLongListVal) }))
    }

}