package hu.simplexion.z2.wireformat

import hu.simplexion.z2.utility.UUID
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

abstract class AbstractStandaloneTest(
    private val wireFormatProvider: WireFormatProvider
) : TestValues() {

    val sv = wireFormatProvider.standalone()

    fun decoder(wireFormat: () -> ByteArray): WireFormatDecoder =
        wireFormatProvider.decoder(wireFormat())

    @Test
    fun testUnit() {
        assertEquals(unitVal, sv.decodeUnit(decoder { sv.encodeUnit(unitVal) }))
        assertEquals(null, sv.decodeUnitOrNull(decoder { sv.encodeUnit(null) }))
        assertEquals(unitVal, sv.decodeUnitOrNull(decoder { sv.encodeUnit(unitVal) }))

        assertEquals(unitListVal, sv.decodeUnitList(decoder { sv.encodeUnitList(unitListVal) }))
        assertEquals(null, sv.decodeUnitListOrNull(decoder { sv.encodeUnitList(null) }))
        assertEquals(unitListVal, sv.decodeUnitListOrNull(decoder { sv.encodeUnitList(unitListVal) }))
    }

    @Test
    fun testBoolean() {
        assertEquals(booleanVal, sv.decodeBoolean(decoder { sv.encodeBoolean(booleanVal) }))
        assertEquals(null, sv.decodeBooleanOrNull(decoder { sv.encodeBoolean(null) }))
        assertEquals(booleanVal, sv.decodeBooleanOrNull(decoder { sv.encodeBoolean(booleanVal) }))

        assertEquals(booleanListVal, sv.decodeBooleanList(decoder { sv.encodeBooleanList(booleanListVal) }))
        assertEquals(null, sv.decodeBooleanListOrNull(decoder { sv.encodeBooleanList(null) }))
        assertEquals(booleanListVal, sv.decodeBooleanListOrNull(decoder { sv.encodeBooleanList(booleanListVal) }))
    }

    @Test
    fun testInt() {
        assertEquals(intVal, sv.decodeInt(decoder { sv.encodeInt(intVal) }))
        assertEquals(null, sv.decodeIntOrNull(decoder { sv.encodeInt(null) }))
        assertEquals(intVal, sv.decodeIntOrNull(decoder { sv.encodeInt(intVal) }))

        assertEquals(intListVal, sv.decodeIntList(decoder { sv.encodeIntList(intListVal) }))
        assertEquals(null, sv.decodeIntListOrNull(decoder { sv.encodeIntList(null) }))
        assertEquals(intListVal, sv.decodeIntListOrNull(decoder { sv.encodeIntList(intListVal) }))
    }

    @Test
    fun testShort() {
        assertEquals(shortVal, sv.decodeShort(decoder { sv.encodeShort(shortVal) }))
        assertEquals(null, sv.decodeShortOrNull(decoder { sv.encodeShort(null) }))
        assertEquals(shortVal, sv.decodeShortOrNull(decoder { sv.encodeShort(shortVal) }))

        assertEquals(shortListVal, sv.decodeShortList(decoder { sv.encodeShortList(shortListVal) }))
        assertEquals(null, sv.decodeShortListOrNull(decoder { sv.encodeShortList(null) }))
        assertEquals(shortListVal, sv.decodeShortListOrNull(decoder { sv.encodeShortList(shortListVal) }))
    }

    @Test
    fun testByte() {
        assertEquals(byteVal, sv.decodeByte(decoder { sv.encodeByte(byteVal) }))
        assertEquals(null, sv.decodeByteOrNull(decoder { sv.encodeByte(null) }))
        assertEquals(byteVal, sv.decodeByteOrNull(decoder { sv.encodeByte(byteVal) }))

        assertEquals(byteListVal, sv.decodeByteList(decoder { sv.encodeByteList(byteListVal) }))
        assertEquals(null, sv.decodeByteListOrNull(decoder { sv.encodeByteList(null) }))
        assertEquals(byteListVal, sv.decodeByteListOrNull(decoder { sv.encodeByteList(byteListVal) }))
    }

    @Test
    fun testLong() {
        assertEquals(longVal, sv.decodeLong(decoder { sv.encodeLong(longVal) }))
        assertEquals(null, sv.decodeLongOrNull(decoder { sv.encodeLong(null) }))
        assertEquals(longVal, sv.decodeLongOrNull(decoder { sv.encodeLong(longVal) }))

        assertEquals(longListVal, sv.decodeLongList(decoder { sv.encodeLongList(longListVal) }))
        assertEquals(null, sv.decodeLongListOrNull(decoder { sv.encodeLongList(null) }))
        assertEquals(longListVal, sv.decodeLongListOrNull(decoder { sv.encodeLongList(longListVal) }))
    }

    @Test
    fun testFloat() {
        assertEquals(floatVal, sv.decodeFloat(decoder { sv.encodeFloat(floatVal) }))
        assertEquals(null, sv.decodeFloatOrNull(decoder { sv.encodeFloat(null) }))
        assertEquals(floatVal, sv.decodeFloatOrNull(decoder { sv.encodeFloat(floatVal) }))

        assertEquals(floatListVal, sv.decodeFloatList(decoder { sv.encodeFloatList(floatListVal) }))
        assertEquals(null, sv.decodeFloatListOrNull(decoder { sv.encodeFloatList(null) }))
        assertEquals(floatListVal, sv.decodeFloatListOrNull(decoder { sv.encodeFloatList(floatListVal) }))
    }

    @Test
    fun testDouble() {
        assertEquals(doubleVal, sv.decodeDouble(decoder { sv.encodeDouble(doubleVal) }))
        assertEquals(null, sv.decodeDoubleOrNull(decoder { sv.encodeDouble(null) }))
        assertEquals(doubleVal, sv.decodeDoubleOrNull(decoder { sv.encodeDouble(doubleVal) }))

        assertEquals(doubleListVal, sv.decodeDoubleList(decoder { sv.encodeDoubleList(doubleListVal) }))
        assertEquals(null, sv.decodeDoubleListOrNull(decoder { sv.encodeDoubleList(null) }))
        assertEquals(doubleListVal, sv.decodeDoubleListOrNull(decoder { sv.encodeDoubleList(doubleListVal) }))
    }

    @Test
    fun testChar() {
        assertEquals(charVal, sv.decodeChar(decoder { sv.encodeChar(charVal) }))
        assertEquals(null, sv.decodeCharOrNull(decoder { sv.encodeChar(null) }))
        assertEquals(charVal, sv.decodeCharOrNull(decoder { sv.encodeChar(charVal) }))

        assertEquals(charListVal, sv.decodeCharList(decoder { sv.encodeCharList(charListVal) }))
        assertEquals(null, sv.decodeCharListOrNull(decoder { sv.encodeCharList(null) }))
        assertEquals(charListVal, sv.decodeCharListOrNull(decoder { sv.encodeCharList(charListVal) }))
    }

    @Test
    fun testString() {
        assertEquals(stringVal, sv.decodeString(decoder { sv.encodeString(stringVal) }))
        assertEquals(null, sv.decodeStringOrNull(decoder { sv.encodeString(null) }))
        assertEquals(stringVal, sv.decodeStringOrNull(decoder { sv.encodeString(stringVal) }))

        assertEquals(stringListVal, sv.decodeStringList(decoder { sv.encodeStringList(stringListVal) }))
        assertEquals(null, sv.decodeStringListOrNull(decoder { sv.encodeStringList(null) }))
        assertEquals(stringListVal, sv.decodeStringListOrNull(decoder { sv.encodeStringList(stringListVal) }))
    }

    @Test
    fun testByteArray() {
        assertContentEquals(byteArrayVal, sv.decodeByteArray(decoder { sv.encodeByteArray(byteArrayVal) }))
        assertEquals(null, sv.decodeByteArrayOrNull(decoder { sv.encodeByteArray(null) }))
        assertContentEquals(byteArrayVal, sv.decodeByteArrayOrNull(decoder { sv.encodeByteArray(byteArrayVal) }))

        sv.decodeByteArrayList(decoder { sv.encodeByteArrayList(byteArrayListVal) }).forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }
        assertEquals(null, sv.decodeByteArrayListOrNull(decoder { sv.encodeByteArrayList(null) }))
        sv.decodeByteArrayListOrNull(decoder { sv.encodeByteArrayList(byteArrayListVal) }) !!.forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }
    }

    @Test
    fun testUuid() {
        assertEquals(uuidVal, sv.decodeUuid(decoder { sv.encodeUuid(uuidVal) }))
        assertEquals(null, sv.decodeUuidOrNull(decoder { sv.encodeUuid(null as UUID<Any>?) }))
        assertEquals(uuidVal, sv.decodeUuidOrNull(decoder { sv.encodeUuid(uuidVal) }))

        assertEquals(uuidListVal, sv.decodeUuidList(decoder { sv.encodeUuidList(uuidListVal) }))
        assertEquals(null, sv.decodeUuidListOrNull(decoder { sv.encodeUuidList(null) }))
        assertEquals(uuidListVal, sv.decodeUuidListOrNull(decoder { sv.encodeUuidList(uuidListVal) }))
    }

    @Test
    fun testInstance() {
        assertEquals(instanceVal, sv.decodeInstance(decoder { sv.encodeInstance(instanceVal, A) }, A))
        assertEquals(null, sv.decodeInstanceOrNull(decoder { sv.encodeInstance(null, A) }, A))
        assertEquals(instanceVal, sv.decodeInstanceOrNull(decoder { sv.encodeInstance(instanceVal, A) }, A))

        assertEquals(instanceListVal, sv.decodeInstanceList(decoder { sv.encodeInstanceList(instanceListVal, B) }, B))
        assertEquals(null, sv.decodeInstanceListOrNull(decoder { sv.encodeInstanceList(null, B) }, B))
        assertEquals(instanceListVal, sv.decodeInstanceListOrNull(decoder { sv.encodeInstanceList(instanceListVal, B) }, B))
    }

    @Test
    fun testEnum() {
        assertEquals(enumVal, sv.decodeEnum(decoder { sv.encodeEnum(enumVal, E.entries) }, E.entries))
        assertEquals(null, sv.decodeEnumOrNull(decoder { sv.encodeEnum(null, E.entries) }, E.entries))
        assertEquals(enumVal, sv.decodeEnumOrNull(decoder { sv.encodeEnum(enumVal, E.entries) }, E.entries))

        assertEquals(enumListVal, sv.decodeEnumList(decoder { sv.encodeEnumList(enumListVal, E.entries) }, E.entries))
        assertEquals(null, sv.decodeEnumListOrNull(decoder { sv.encodeEnumList(null, E.entries) }, E.entries))
        assertEquals(enumListVal, sv.decodeEnumListOrNull(decoder { sv.encodeEnumList(enumListVal, E.entries) }, E.entries))
    }

    @Test
    fun testUInt() {
        assertEquals(uIntVal, sv.decodeUInt(decoder { sv.encodeUInt(uIntVal) }))
        assertEquals(null, sv.decodeUIntOrNull(decoder { sv.encodeUInt(null) }))
        assertEquals(uIntVal, sv.decodeUIntOrNull(decoder { sv.encodeUInt(uIntVal) }))

        assertEquals(uIntListVal, sv.decodeUIntList(decoder { sv.encodeUIntList(uIntListVal) }))
        assertEquals(null, sv.decodeUIntListOrNull(decoder { sv.encodeUIntList(null) }))
        assertEquals(uIntListVal, sv.decodeUIntListOrNull(decoder { sv.encodeUIntList(uIntListVal) }))
    }

    @Test
    fun testUShort() {
        assertEquals(uShortVal, sv.decodeUShort(decoder { sv.encodeUShort(uShortVal) }))
        assertEquals(null, sv.decodeUShortOrNull(decoder { sv.encodeUShort(null) }))
        assertEquals(uShortVal, sv.decodeUShortOrNull(decoder { sv.encodeUShort(uShortVal) }))

        assertEquals(uShortListVal, sv.decodeUShortList(decoder { sv.encodeUShortList(uShortListVal) }))
        assertEquals(null, sv.decodeUShortListOrNull(decoder { sv.encodeUShortList(null) }))
        assertEquals(uShortListVal, sv.decodeUShortListOrNull(decoder { sv.encodeUShortList(uShortListVal) }))
    }

    @Test
    fun testUByte() {
        assertEquals(uByteVal, sv.decodeUByte(decoder { sv.encodeUByte(uByteVal) }))
        assertEquals(null, sv.decodeUByteOrNull(decoder { sv.encodeUByte(null) }))
        assertEquals(uByteVal, sv.decodeUByteOrNull(decoder { sv.encodeUByte(uByteVal) }))

        assertEquals(uByteListVal, sv.decodeUByteList(decoder { sv.encodeUByteList(uByteListVal) }))
        assertEquals(null, sv.decodeUByteListOrNull(decoder { sv.encodeUByteList(null) }))
        assertEquals(uByteListVal, sv.decodeUByteListOrNull(decoder { sv.encodeUByteList(uByteListVal) }))
    }

    @Test
    fun testULong() {
        assertEquals(uLongVal, sv.decodeULong(decoder { sv.encodeULong(uLongVal) }))
        assertEquals(null, sv.decodeULongOrNull(decoder { sv.encodeULong(null) }))
        assertEquals(uLongVal, sv.decodeULongOrNull(decoder { sv.encodeULong(uLongVal) }))

        assertEquals(uLongListVal, sv.decodeULongList(decoder { sv.encodeULongList(uLongListVal) }))
        assertEquals(null, sv.decodeULongListOrNull(decoder { sv.encodeULongList(null) }))
        assertEquals(uLongListVal, sv.decodeULongListOrNull(decoder { sv.encodeULongList(uLongListVal) }))
    }

}