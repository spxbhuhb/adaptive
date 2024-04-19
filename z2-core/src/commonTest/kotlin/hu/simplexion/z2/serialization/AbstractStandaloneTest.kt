package hu.simplexion.z2.serialization

import hu.simplexion.z2.util.UUID
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

abstract class AbstractStandaloneTest(
    private val serializationConfig: SerializationConfig
) : TestValues() {

    @Test
    fun standaloneTest() {

        val sv = serializationConfig.standaloneValue()

        fun message(wireFormat: () -> ByteArray): Message =
            serializationConfig.toMessage(wireFormat())

//        assertEquals(Unit, sv.decodeUnit(message { }))

        assertEquals(booleanVal, sv.decodeBoolean(message { sv.encodeBoolean(booleanVal) }))
        assertEquals(null, sv.decodeBooleanOrNull(message { sv.encodeBoolean(null) }))
        assertEquals(booleanVal, sv.decodeBooleanOrNull(message { sv.encodeBoolean(booleanVal) }))

        assertEquals(intVal, sv.decodeInt(message { sv.encodeInt(intVal) }))
        assertEquals(null, sv.decodeIntOrNull(message { sv.encodeInt(null) }))
        assertEquals(intVal, sv.decodeIntOrNull(message { sv.encodeInt(intVal) }))

        assertEquals(longVal, sv.decodeLong(message { sv.encodeLong(longVal) }))
        assertEquals(null, sv.decodeLongOrNull(message { sv.encodeLong(null) }))
        assertEquals(longVal, sv.decodeLongOrNull(message { sv.encodeLong(longVal) }))

        assertEquals(stringVal, sv.decodeString(message { sv.encodeString(stringVal) }))
        assertEquals(null, sv.decodeStringOrNull(message { sv.encodeString(null) }))
        assertEquals(stringVal, sv.decodeStringOrNull(message { sv.encodeString(stringVal) }))

        assertContentEquals(byteArrayVal, sv.decodeByteArray(message { sv.encodeByteArray(byteArrayVal) }))
        assertEquals(null, sv.decodeByteArrayOrNull(message { sv.encodeByteArray(null) }))
        assertContentEquals(byteArrayVal, sv.decodeByteArrayOrNull(message { sv.encodeByteArray(byteArrayVal) }))

        assertEquals(uuidVal, sv.decodeUuid(message { sv.encodeUuid(uuidVal) }))
        assertEquals(null, sv.decodeUuidOrNull(message { sv.encodeUuid(null as UUID<Any>?) }))
        assertEquals(uuidVal, sv.decodeUuidOrNull(message { sv.encodeUuid(uuidVal) }))

        assertEquals(instanceVal, sv.decodeInstance(message { sv.encodeInstance(instanceVal, A) }, A))
        assertEquals(null, sv.decodeInstanceOrNull(message { sv.encodeInstance(null, A) }, A))
        assertEquals(instanceVal, sv.decodeInstanceOrNull(message { sv.encodeInstance(instanceVal, A) }, A))

        assertEquals(enumVal, sv.decodeEnum(message { sv.encodeEnum(enumVal, E.entries) }, E.entries))
        assertEquals(null, sv.decodeEnumOrNull(message { sv.encodeEnum(null, E.entries) }, E.entries))
        assertEquals(enumVal, sv.decodeEnumOrNull(message { sv.encodeEnum(enumVal, E.entries) }, E.entries))

        assertEquals(booleanListVal, sv.decodeBooleanList(message { sv.encodeBooleanList(booleanListVal) }))
        assertEquals(null, sv.decodeBooleanListOrNull(message { sv.encodeBooleanList(null) }))
        assertEquals(booleanListVal, sv.decodeBooleanListOrNull(message { sv.encodeBooleanList(booleanListVal) }))

        assertEquals(intListVal, sv.decodeIntList(message { sv.encodeIntList(intListVal) }))
        assertEquals(null, sv.decodeIntListOrNull(message { sv.encodeIntList(null) }))
        assertEquals(intListVal, sv.decodeIntListOrNull(message { sv.encodeIntList(intListVal) }))

        assertEquals(longListVal, sv.decodeLongList(message { sv.encodeLongList(longListVal) }))
        assertEquals(null, sv.decodeLongListOrNull(message { sv.encodeLongList(null) }))
        assertEquals(longListVal, sv.decodeLongListOrNull(message { sv.encodeLongList(longListVal) }))

        assertEquals(stringListVal, sv.decodeStringList(message { sv.encodeStringList(stringListVal) }))
        assertEquals(null, sv.decodeStringListOrNull(message { sv.encodeStringList(null) }))
        assertEquals(stringListVal, sv.decodeStringListOrNull(message { sv.encodeStringList(stringListVal) }))

        sv.decodeByteArrayList(message { sv.encodeByteArrayList(byteArrayListVal) }).forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }
        assertEquals(null, sv.decodeByteArrayListOrNull(message { sv.encodeByteArrayList(null) }))
        sv.decodeByteArrayListOrNull(message { sv.encodeByteArrayList(byteArrayListVal) }) !!.forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }

        assertEquals(uuidListVal, sv.decodeUuidList(message { sv.encodeUuidList(uuidListVal) }))
        assertEquals(null, sv.decodeUuidListOrNull(message { sv.encodeUuidList(null) }))
        assertEquals(uuidListVal, sv.decodeUuidListOrNull(message { sv.encodeUuidList(uuidListVal) }))

        assertEquals(instanceListVal, sv.decodeInstanceList(message { sv.encodeInstanceList(instanceListVal, B) }, B))
        assertEquals(null, sv.decodeInstanceListOrNull(message { sv.encodeInstanceList(null, B) }, B))
        assertEquals(instanceListVal, sv.decodeInstanceListOrNull(message { sv.encodeInstanceList(instanceListVal, B) }, B))

        assertEquals(enumListVal, sv.decodeEnumList(message { sv.encodeEnumList(enumListVal, E.entries) }, E.entries))
        assertEquals(null, sv.decodeEnumListOrNull(message { sv.encodeEnumList(null, E.entries) }, E.entries))
        assertEquals(enumListVal, sv.decodeEnumListOrNull(message { sv.encodeEnumList(enumListVal, E.entries) }, E.entries))

    }
}