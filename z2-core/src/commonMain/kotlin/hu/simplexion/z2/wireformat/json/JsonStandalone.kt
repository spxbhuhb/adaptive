package hu.simplexion.z2.wireformat.json

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.wireformat.Standalone
import hu.simplexion.z2.wireformat.WireFormat
import hu.simplexion.z2.wireformat.json.elements.JsonArray
import hu.simplexion.z2.wireformat.json.elements.JsonElement
import hu.simplexion.z2.wireformat.json.elements.JsonNull
import hu.simplexion.z2.wireformat.json.elements.JsonObject
import kotlin.enums.EnumEntries

object JsonStandalone : Standalone {

    // ---------------------------------------------------------------------------
    // Any
    // ---------------------------------------------------------------------------

    override fun encodeAny(value: Any?): ByteArray =
        TODO()

    override fun encodeAnyList(value: List<Any>?): ByteArray =
        TODO()

    override fun decodeAny(source: ByteArray): Any =
        TODO()

    override fun decodeAnyOrNull(source: ByteArray): Any =
        TODO()

    override fun decodeAnyList(source: ByteArray): List<Any> =
        TODO()

    override fun decodeAnyListOrNull(source: ByteArray): List<Any> =
        TODO()

    // ---------------------------------------------------------------------------
    // Unit
    // ---------------------------------------------------------------------------

    override fun encodeUnit(value: Unit?): ByteArray =
        valueOrNull(value) { bool(true) }

    override fun encodeUnitList(value: List<Unit>?): ByteArray =
        arrayOrNull(value) { bool(true) }

    override fun decodeUnit(source: ByteArray): Unit =
        requireNotNull(decodeUnitOrNull(source))

    override fun decodeUnitOrNull(source: ByteArray): Unit? =
        fromValueOrNull(source) { asUnit }

    override fun decodeUnitList(source: ByteArray): List<Unit> =
        requireNotNull(decodeUnitListOrNull(source))

    override fun decodeUnitListOrNull(source: ByteArray): List<Unit>? =
        fromArray(source) { asUnit }

    // ---------------------------------------------------------------------------
    // Boolean
    // ---------------------------------------------------------------------------

    override fun encodeBoolean(value: Boolean?): ByteArray =
        valueOrNull(value) { bool(it) }

    override fun encodeBooleanList(value: List<Boolean>?): ByteArray =
        arrayOrNull(value) { bool(it) }

    override fun decodeBoolean(source: ByteArray): Boolean =
        requireNotNull(decodeBooleanOrNull(source))

    override fun decodeBooleanOrNull(source: ByteArray): Boolean? =
        fromValueOrNull(source) { asBoolean }

    override fun decodeBooleanList(source: ByteArray): List<Boolean> =
        requireNotNull(decodeBooleanListOrNull(source))

    override fun decodeBooleanListOrNull(source: ByteArray): List<Boolean>? =
        fromArray(source) { asBoolean }

    // ---------------------------------------------------------------------------
    // Int
    // ---------------------------------------------------------------------------

    override fun encodeInt(value: Int?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeIntList(value: List<Int>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeInt(source: ByteArray): Int =
        requireNotNull(decodeIntOrNull(source))

    override fun decodeIntOrNull(source: ByteArray): Int? =
        fromValueOrNull(source) { asInt }

    override fun decodeIntList(source: ByteArray): List<Int> =
        requireNotNull(decodeIntListOrNull(source))

    override fun decodeIntListOrNull(source: ByteArray): List<Int>? =
        fromArray(source) { asInt }

    // ---------------------------------------------------------------------------
    // Short
    // ---------------------------------------------------------------------------

    override fun encodeShort(value: Short?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeShortList(value: List<Short>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeShort(source: ByteArray): Short =
        requireNotNull(decodeShortOrNull(source))

    override fun decodeShortOrNull(source: ByteArray): Short? =
        fromValueOrNull(source) { asShort }

    override fun decodeShortList(source: ByteArray): List<Short> =
        requireNotNull(decodeShortListOrNull(source))

    override fun decodeShortListOrNull(source: ByteArray): List<Short>? =
        fromArray(source) { asShort }

    // ---------------------------------------------------------------------------
    // Byte
    // ---------------------------------------------------------------------------

    override fun encodeByte(value: Byte?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeByteList(value: List<Byte>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeByte(source: ByteArray): Byte =
        requireNotNull(decodeByteOrNull(source))

    override fun decodeByteOrNull(source: ByteArray): Byte? =
        fromValueOrNull(source) { asByte }

    override fun decodeByteList(source: ByteArray): List<Byte> =
        requireNotNull(decodeByteListOrNull(source))

    override fun decodeByteListOrNull(source: ByteArray): List<Byte>? =
        fromArray(source) { asByte }
    
    // ---------------------------------------------------------------------------
    // Long
    // ---------------------------------------------------------------------------

    override fun encodeLong(value: Long?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeLongList(value: List<Long>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeLong(source: ByteArray): Long =
        requireNotNull(decodeLongOrNull(source))

    override fun decodeLongOrNull(source: ByteArray): Long? =
        fromValueOrNull(source) { asLong }

    override fun decodeLongList(source: ByteArray): List<Long> =
        requireNotNull(decodeLongListOrNull(source))

    override fun decodeLongListOrNull(source: ByteArray): List<Long>? =
        fromArray(source) { asLong }

    // ---------------------------------------------------------------------------
    // Float
    // ---------------------------------------------------------------------------

    override fun encodeFloat(value: Float?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeFloatList(value: List<Float>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeFloat(source: ByteArray): Float =
        requireNotNull(decodeFloatOrNull(source))

    override fun decodeFloatOrNull(source: ByteArray): Float? =
        fromValueOrNull(source) { asFloat }

    override fun decodeFloatList(source: ByteArray): List<Float> =
        requireNotNull(decodeFloatListOrNull(source))

    override fun decodeFloatListOrNull(source: ByteArray): List<Float>? =
        fromArray(source) { asFloat }

    // ---------------------------------------------------------------------------
    // Double
    // ---------------------------------------------------------------------------

    override fun encodeDouble(value: Double?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeDoubleList(value: List<Double>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeDouble(source: ByteArray): Double =
        requireNotNull(decodeDoubleOrNull(source))

    override fun decodeDoubleOrNull(source: ByteArray): Double? =
        fromValueOrNull(source) { asDouble }

    override fun decodeDoubleList(source: ByteArray): List<Double> =
        requireNotNull(decodeDoubleListOrNull(source))

    override fun decodeDoubleListOrNull(source: ByteArray): List<Double>? =
        fromArray(source) { asDouble }

    // ---------------------------------------------------------------------------
    // Char
    // ---------------------------------------------------------------------------

    override fun encodeChar(value: Char?): ByteArray =
        valueOrNull(value) { quotedString(it.toString()) }

    override fun encodeCharList(value: List<Char>?): ByteArray =
        arrayOrNull(value) { quotedString(it.toString()) }

    override fun decodeChar(source: ByteArray): Char =
        requireNotNull(decodeCharOrNull(source))

    override fun decodeCharOrNull(source: ByteArray): Char? =
        fromValueOrNull(source) { asChar }

    override fun decodeCharList(source: ByteArray): List<Char> =
        requireNotNull(decodeCharListOrNull(source))

    override fun decodeCharListOrNull(source: ByteArray): List<Char>? =
        fromArray(source) { asChar }
    
    // ---------------------------------------------------------------------------
    // String
    // ---------------------------------------------------------------------------

    override fun encodeString(value: String?): ByteArray =
        valueOrNull(value) { quotedString(it) }

    override fun encodeStringList(value: List<String>?): ByteArray =
        arrayOrNull(value) { quotedString(it) }

    override fun decodeString(source: ByteArray): String =
        requireNotNull(decodeStringOrNull(source))

    override fun decodeStringOrNull(source: ByteArray): String? =
        fromValueOrNull(source) { asString }

    override fun decodeStringList(source: ByteArray): List<String> =
        requireNotNull(decodeStringListOrNull(source))

    override fun decodeStringListOrNull(source: ByteArray): List<String>? =
        fromArray(source) { asString }

    // ---------------------------------------------------------------------------
    // ByteArray
    // ---------------------------------------------------------------------------

    override fun encodeByteArray(value: ByteArray?): ByteArray =
        valueOrNull(value) { bytes(it) }

    override fun encodeByteArrayList(value: List<ByteArray>?): ByteArray =
        arrayOrNull(value) { bytes(it) }

    override fun decodeByteArray(source: ByteArray): ByteArray =
        requireNotNull(decodeByteArrayOrNull(source))

    override fun decodeByteArrayOrNull(source: ByteArray): ByteArray? =
        fromValueOrNull(source) { asByteArray }

    override fun decodeByteArrayList(source: ByteArray): List<ByteArray> =
        requireNotNull(decodeByteArrayListOrNull(source))

    override fun decodeByteArrayListOrNull(source: ByteArray): List<ByteArray>? =
        fromArray(source) { asByteArray }

    // ---------------------------------------------------------------------------
    // UUID
    // ---------------------------------------------------------------------------

    override fun encodeUuid(value: UUID<*>?): ByteArray =
        valueOrNull(value) { uuid(it) }

    override fun encodeUuidList(value: List<UUID<*>>?): ByteArray =
        arrayOrNull(value) { uuid(it) }

    override fun decodeUuid(source: ByteArray): UUID<Any> =
        requireNotNull(decodeUuidOrNull(source))

    override fun decodeUuidOrNull(source: ByteArray): UUID<Any>? =
        fromValueOrNull(source) { asUuid() }

    override fun decodeUuidList(source: ByteArray): List<UUID<Any>> =
        requireNotNull(decodeUuidListOrNull(source))

    override fun decodeUuidListOrNull(source: ByteArray): List<UUID<Any>>? =
        fromArray(source) { asUuid() }

    // ---------------------------------------------------------------------------
    // Instance
    // ---------------------------------------------------------------------------

    override fun <T> encodeInstance(value: T?, wireFormat: WireFormat<T>): ByteArray =
        if (value == null) {
            JsonBufferWriter().apply { nullValue() }.pack()
        } else {
            JsonWireFormatEncoder().apply {
                instance(value, wireFormat)
            }.pack()
        }

    override fun <T> encodeInstanceList(value: List<T>?, wireFormat: WireFormat<T>): ByteArray =
        arrayOrNull(value) {
            val encoder = JsonWireFormatEncoder(this)
            encoder.instance(it, wireFormat)
            if (peekLast() == 0x2c.toByte()) rollback() // to remove the last colon
        }

    override fun <T> decodeInstance(source: ByteArray, wireFormat: WireFormat<T>): T =
        requireNotNull(decodeInstanceOrNull(source, wireFormat))

    override fun <T> decodeInstanceOrNull(source: ByteArray, wireFormat: WireFormat<T>): T? =
        fromValueOrNull(source) { wireFormat.wireFormatDecode(JsonWireFormatDecoder(source)) }

    override fun <T> decodeInstanceList(source: ByteArray, wireFormat: WireFormat<T>): List<T> =
        requireNotNull(decodeInstanceListOrNull(source, wireFormat))

    override fun <T> decodeInstanceListOrNull(source: ByteArray, wireFormat: WireFormat<T>): List<T>? =
        fromArray(source) { wireFormat.wireFormatDecode(JsonWireFormatDecoder(this as JsonObject)) }

    // ---------------------------------------------------------------------------
    // Enum
    // ---------------------------------------------------------------------------

    override fun <E : Enum<E>> encodeEnum(value: E?, entries: EnumEntries<E>): ByteArray =
        valueOrNull(value) { quotedString(it.name) }

    override fun <E : Enum<E>> encodeEnumList(value: List<E>?, entries: EnumEntries<E>): ByteArray =
        arrayOrNull(value) { quotedString(it.name) }

    override fun <E : Enum<E>> decodeEnum(source: ByteArray, entries: EnumEntries<E>): E =
        requireNotNull(decodeEnumOrNull(source, entries))

    override fun <E : Enum<E>> decodeEnumOrNull(source: ByteArray, entries: EnumEntries<E>): E? {
        val decoder = JsonWireFormatDecoder(source)
        if (decoder.root is JsonNull) return null
        val name = decoder.root.asString
        return entries.first { it.name == name }
    }

    override fun <E : Enum<E>> decodeEnumList(source: ByteArray, entries: EnumEntries<E>): List<E> =
        requireNotNull(decodeEnumListOrNull(source, entries))

    override fun <E : Enum<E>> decodeEnumListOrNull(source: ByteArray, entries: EnumEntries<E>): List<E>? =
        fromArray(source) {
            val name = asString
            entries.first { it.name == name }
        }

    // ---------------------------------------------------------------------------
    // UInt
    // ---------------------------------------------------------------------------

    override fun encodeUInt(value: UInt?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeUIntList(value: List<UInt>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeUInt(source: ByteArray): UInt =
        requireNotNull(decodeUIntOrNull(source))

    override fun decodeUIntOrNull(source: ByteArray): UInt? =
        fromValueOrNull(source) { asUInt }

    override fun decodeUIntList(source: ByteArray): List<UInt> =
        requireNotNull(decodeUIntListOrNull(source))

    override fun decodeUIntListOrNull(source: ByteArray): List<UInt>? =
        fromArray(source) { asUInt }

    // ---------------------------------------------------------------------------
    // UShort
    // ---------------------------------------------------------------------------

    override fun encodeUShort(value: UShort?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeUShortList(value: List<UShort>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeUShort(source: ByteArray): UShort =
        requireNotNull(decodeUShortOrNull(source))

    override fun decodeUShortOrNull(source: ByteArray): UShort? =
        fromValueOrNull(source) { asUShort }

    override fun decodeUShortList(source: ByteArray): List<UShort> =
        requireNotNull(decodeUShortListOrNull(source))

    override fun decodeUShortListOrNull(source: ByteArray): List<UShort>? =
        fromArray(source) { asUShort }

    // ---------------------------------------------------------------------------
    // UByte
    // ---------------------------------------------------------------------------

    override fun encodeUByte(value: UByte?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeUByteList(value: List<UByte>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeUByte(source: ByteArray): UByte =
        requireNotNull(decodeUByteOrNull(source))

    override fun decodeUByteOrNull(source: ByteArray): UByte? =
        fromValueOrNull(source) { asUByte }

    override fun decodeUByteList(source: ByteArray): List<UByte> =
        requireNotNull(decodeUByteListOrNull(source))

    override fun decodeUByteListOrNull(source: ByteArray): List<UByte>? =
        fromArray(source) { asUByte }

    // ---------------------------------------------------------------------------
    // ULong
    // ---------------------------------------------------------------------------

    override fun encodeULong(value: ULong?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeULongList(value: List<ULong>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeULong(source: ByteArray): ULong =
        requireNotNull(decodeULongOrNull(source))

    override fun decodeULongOrNull(source: ByteArray): ULong? =
        fromValueOrNull(source) { asULong }

    override fun decodeULongList(source: ByteArray): List<ULong> =
        requireNotNull(decodeULongListOrNull(source))

    override fun decodeULongListOrNull(source: ByteArray): List<ULong>? =
        fromArray(source) { asULong }

    // ---------------------------------------------------------------------------
    // Utility
    // ---------------------------------------------------------------------------

    private fun <T> valueOrNull(value: T?, valueFun: JsonBufferWriter.(value: T) -> Unit): ByteArray {
        val writer = JsonBufferWriter()
        if (value == null) {
            writer.nullValue()
        } else {
            writer.valueFun(value)
        }
        return writer.pack()
    }

    private fun <T> arrayOrNull(values: List<T>?, valueFun: JsonBufferWriter.(value: T) -> Unit): ByteArray {
        val writer = JsonBufferWriter()

        if (values == null) {
            writer.nullValue()
            return writer.pack()
        }

        writer.openArray()
        for (value in values) {
            writer.valueFun(value)
            writer.separator()
        }
        writer.closeArray()
        return writer.pack()
    }

    private fun <T> fromValueOrNull(source: ByteArray, valueFun: JsonElement.() -> T): T? {
        val decoder = JsonWireFormatDecoder(source)
        if (decoder.root is JsonNull) return null
        return decoder.root.valueFun()
    }

    private fun <T> fromArray(source: ByteArray, valueFun: JsonElement.() -> T): List<T>? {

        val element = JsonWireFormatDecoder(source).root

        if (element is JsonNull) return null

        require(element is JsonArray)
        if (element.items.isEmpty()) return emptyList()

        val result = mutableListOf<T>()
        for (item in element.items) {
            result += item.valueFun()
        }

        return result
    }

}
