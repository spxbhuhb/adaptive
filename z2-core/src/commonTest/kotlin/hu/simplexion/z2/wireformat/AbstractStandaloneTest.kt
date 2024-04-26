package hu.simplexion.z2.wireformat

import hu.simplexion.z2.utility.UUID
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

abstract class AbstractStandaloneTest(
    private val wireFormatProvider: WireFormatProvider
) : TestValues() {

    val sv = wireFormatProvider.standalone()

    fun decoder(wireFormat: () -> ByteArray): WireFormatDecoder<*> =
        wireFormatProvider.decoder(wireFormat())

    @Test
    fun testUnit() {
        assertEquals(unitVal, sv.decodeUnit(sv.encodeUnit(unitVal)))
        assertEquals(null, sv.decodeUnitOrNull(sv.encodeUnit(null)))
        assertEquals(unitVal, sv.decodeUnitOrNull(sv.encodeUnit(unitVal)))

        assertEquals(unitListVal, sv.decodeUnitList(sv.encodeUnitList(unitListVal)))
        assertEquals(null, sv.decodeUnitListOrNull(sv.encodeUnitList(null)))
        assertEquals(unitListVal, sv.decodeUnitListOrNull(sv.encodeUnitList(unitListVal)))
    }

    @Test
    fun testBoolean() {
        assertEquals(booleanVal, sv.decodeBoolean(sv.encodeBoolean(booleanVal)))
        assertEquals(null, sv.decodeBooleanOrNull(sv.encodeBoolean(null)))
        assertEquals(booleanVal, sv.decodeBooleanOrNull(sv.encodeBoolean(booleanVal)))

        assertEquals(booleanListVal, sv.decodeBooleanList(sv.encodeBooleanList(booleanListVal)))
        assertEquals(null, sv.decodeBooleanListOrNull(sv.encodeBooleanList(null)))
        assertEquals(booleanListVal, sv.decodeBooleanListOrNull(sv.encodeBooleanList(booleanListVal)))
    }

    @Test
    fun testInt() {
        assertEquals(intVal, sv.decodeInt(sv.encodeInt(intVal)))
        assertEquals(null, sv.decodeIntOrNull(sv.encodeInt(null)))
        assertEquals(intVal, sv.decodeIntOrNull(sv.encodeInt(intVal)))

        assertEquals(intListVal, sv.decodeIntList(sv.encodeIntList(intListVal)))
        assertEquals(null, sv.decodeIntListOrNull(sv.encodeIntList(null)))
        assertEquals(intListVal, sv.decodeIntListOrNull(sv.encodeIntList(intListVal)))
    }

    @Test
    fun testShort() {
        assertEquals(shortVal, sv.decodeShort(sv.encodeShort(shortVal)))
        assertEquals(null, sv.decodeShortOrNull(sv.encodeShort(null)))
        assertEquals(shortVal, sv.decodeShortOrNull(sv.encodeShort(shortVal)))

        assertEquals(shortListVal, sv.decodeShortList(sv.encodeShortList(shortListVal)))
        assertEquals(null, sv.decodeShortListOrNull(sv.encodeShortList(null)))
        assertEquals(shortListVal, sv.decodeShortListOrNull(sv.encodeShortList(shortListVal)))
    }

    @Test
    fun testByte() {
        assertEquals(byteVal, sv.decodeByte(sv.encodeByte(byteVal)))
        assertEquals(null, sv.decodeByteOrNull(sv.encodeByte(null)))
        assertEquals(byteVal, sv.decodeByteOrNull(sv.encodeByte(byteVal)))

        assertEquals(byteListVal, sv.decodeByteList(sv.encodeByteList(byteListVal)))
        assertEquals(null, sv.decodeByteListOrNull(sv.encodeByteList(null)))
        assertEquals(byteListVal, sv.decodeByteListOrNull(sv.encodeByteList(byteListVal)))
    }

    @Test
    fun testLong() {
        assertEquals(longVal, sv.decodeLong(sv.encodeLong(longVal)))
        assertEquals(null, sv.decodeLongOrNull(sv.encodeLong(null)))
        assertEquals(longVal, sv.decodeLongOrNull(sv.encodeLong(longVal)))

        assertEquals(longListVal, sv.decodeLongList(sv.encodeLongList(longListVal)))
        assertEquals(null, sv.decodeLongListOrNull(sv.encodeLongList(null)))
        assertEquals(longListVal, sv.decodeLongListOrNull(sv.encodeLongList(longListVal)))
    }

    @Test
    fun testFloat() {
        assertEquals(floatVal, sv.decodeFloat(sv.encodeFloat(floatVal)))
        assertEquals(null, sv.decodeFloatOrNull(sv.encodeFloat(null)))
        assertEquals(floatVal, sv.decodeFloatOrNull(sv.encodeFloat(floatVal)))

        assertEquals(floatListVal, sv.decodeFloatList(sv.encodeFloatList(floatListVal)))
        assertEquals(null, sv.decodeFloatListOrNull(sv.encodeFloatList(null)))
        assertEquals(floatListVal, sv.decodeFloatListOrNull(sv.encodeFloatList(floatListVal)))
    }

    @Test
    fun testDouble() {
        assertEquals(doubleVal, sv.decodeDouble(sv.encodeDouble(doubleVal)))
        assertEquals(null, sv.decodeDoubleOrNull(sv.encodeDouble(null)))
        assertEquals(doubleVal, sv.decodeDoubleOrNull(sv.encodeDouble(doubleVal)))

        assertEquals(doubleListVal, sv.decodeDoubleList(sv.encodeDoubleList(doubleListVal)))
        assertEquals(null, sv.decodeDoubleListOrNull(sv.encodeDoubleList(null)))
        assertEquals(doubleListVal, sv.decodeDoubleListOrNull(sv.encodeDoubleList(doubleListVal)))
    }

    @Test
    fun testChar() {
        assertEquals(charVal, sv.decodeChar(sv.encodeChar(charVal)))
        assertEquals(null, sv.decodeCharOrNull(sv.encodeChar(null)))
        assertEquals(charVal, sv.decodeCharOrNull(sv.encodeChar(charVal)))

        assertEquals(charListVal, sv.decodeCharList(sv.encodeCharList(charListVal)))
        assertEquals(null, sv.decodeCharListOrNull(sv.encodeCharList(null)))
        assertEquals(charListVal, sv.decodeCharListOrNull(sv.encodeCharList(charListVal)))
    }

    @Test
    fun testString() {
        assertEquals(stringVal, sv.decodeString(sv.encodeString(stringVal)))
        assertEquals(null, sv.decodeStringOrNull(sv.encodeString(null)))
        assertEquals(stringVal, sv.decodeStringOrNull(sv.encodeString(stringVal)))

        assertEquals(stringListVal, sv.decodeStringList(sv.encodeStringList(stringListVal)))
        assertEquals(null, sv.decodeStringListOrNull(sv.encodeStringList(null)))
        assertEquals(stringListVal, sv.decodeStringListOrNull(sv.encodeStringList(stringListVal)))
    }

    @Test
    fun testByteArray() {
        assertContentEquals(byteArrayVal, sv.decodeByteArray(sv.encodeByteArray(byteArrayVal)))
        assertEquals(null, sv.decodeByteArrayOrNull(sv.encodeByteArray(null)))
        assertContentEquals(byteArrayVal, sv.decodeByteArrayOrNull(sv.encodeByteArray(byteArrayVal)))

        sv.decodeByteArrayList(sv.encodeByteArrayList(byteArrayListVal)).forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }
        assertEquals(null, sv.decodeByteArrayListOrNull(sv.encodeByteArrayList(null)))
        sv.decodeByteArrayListOrNull(sv.encodeByteArrayList(byteArrayListVal)) !!.forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }
    }

    @Test
    fun testUuid() {
        assertEquals(uuidVal, sv.decodeUuid(sv.encodeUuid(uuidVal)))
        assertEquals(null, sv.decodeUuidOrNull(sv.encodeUuid(null as UUID<Any>?)))
        assertEquals(uuidVal, sv.decodeUuidOrNull(sv.encodeUuid(uuidVal)))

        assertEquals(uuidListVal, sv.decodeUuidList(sv.encodeUuidList(uuidListVal)))
        assertEquals(null, sv.decodeUuidListOrNull(sv.encodeUuidList(null)))
        assertEquals(uuidListVal, sv.decodeUuidListOrNull(sv.encodeUuidList(uuidListVal)))
    }

    @Test
    fun testInstance() {
        assertEquals(instanceVal, sv.decodeInstance(sv.encodeInstance(instanceVal, A), A))
        assertEquals(null, sv.decodeInstanceOrNull(sv.encodeInstance(null, A), A))
        assertEquals(instanceVal, sv.decodeInstanceOrNull(sv.encodeInstance(instanceVal, A), A))

        assertEquals(instanceListVal, sv.decodeInstanceList(sv.encodeInstanceList(instanceListVal, B), B))
        assertEquals(null, sv.decodeInstanceListOrNull(sv.encodeInstanceList(null, B), B))
        assertEquals(instanceListVal, sv.decodeInstanceListOrNull(sv.encodeInstanceList(instanceListVal, B), B))
    }

    @Test
    fun testEnum() {
        assertEquals(enumVal, sv.decodeEnum(sv.encodeEnum(enumVal, E.entries), E.entries))
        assertEquals(null, sv.decodeEnumOrNull(sv.encodeEnum(null, E.entries), E.entries))
        assertEquals(enumVal, sv.decodeEnumOrNull(sv.encodeEnum(enumVal, E.entries), E.entries))

        assertEquals(enumListVal, sv.decodeEnumList(sv.encodeEnumList(enumListVal, E.entries), E.entries))
        assertEquals(null, sv.decodeEnumListOrNull(sv.encodeEnumList(null, E.entries), E.entries))
        assertEquals(enumListVal, sv.decodeEnumListOrNull(sv.encodeEnumList(enumListVal, E.entries), E.entries))
    }

    @Test
    fun testUInt() {
        assertEquals(uIntVal, sv.decodeUInt(sv.encodeUInt(uIntVal)))
        assertEquals(null, sv.decodeUIntOrNull(sv.encodeUInt(null)))
        assertEquals(uIntVal, sv.decodeUIntOrNull(sv.encodeUInt(uIntVal)))

        assertEquals(uIntListVal, sv.decodeUIntList(sv.encodeUIntList(uIntListVal)))
        assertEquals(null, sv.decodeUIntListOrNull(sv.encodeUIntList(null)))
        assertEquals(uIntListVal, sv.decodeUIntListOrNull(sv.encodeUIntList(uIntListVal)))
    }

    @Test
    fun testUShort() {
        assertEquals(uShortVal, sv.decodeUShort(sv.encodeUShort(uShortVal)))
        assertEquals(null, sv.decodeUShortOrNull(sv.encodeUShort(null)))
        assertEquals(uShortVal, sv.decodeUShortOrNull(sv.encodeUShort(uShortVal)))

        assertEquals(uShortListVal, sv.decodeUShortList(sv.encodeUShortList(uShortListVal)))
        assertEquals(null, sv.decodeUShortListOrNull(sv.encodeUShortList(null)))
        assertEquals(uShortListVal, sv.decodeUShortListOrNull(sv.encodeUShortList(uShortListVal)))
    }

    @Test
    fun testUByte() {
        assertEquals(uByteVal, sv.decodeUByte(sv.encodeUByte(uByteVal)))
        assertEquals(null, sv.decodeUByteOrNull(sv.encodeUByte(null)))
        assertEquals(uByteVal, sv.decodeUByteOrNull(sv.encodeUByte(uByteVal)))

        assertEquals(uByteListVal, sv.decodeUByteList(sv.encodeUByteList(uByteListVal)))
        assertEquals(null, sv.decodeUByteListOrNull(sv.encodeUByteList(null)))
        assertEquals(uByteListVal, sv.decodeUByteListOrNull(sv.encodeUByteList(uByteListVal)))
    }

    @Test
    fun testULong() {
        assertEquals(uLongVal, sv.decodeULong(sv.encodeULong(uLongVal)))
        assertEquals(null, sv.decodeULongOrNull(sv.encodeULong(null)))
        assertEquals(uLongVal, sv.decodeULongOrNull(sv.encodeULong(uLongVal)))

        assertEquals(uLongListVal, sv.decodeULongList(sv.encodeULongList(uLongListVal)))
        assertEquals(null, sv.decodeULongListOrNull(sv.encodeULongList(null)))
        assertEquals(uLongListVal, sv.decodeULongListOrNull(sv.encodeULongList(uLongListVal)))
    }

}