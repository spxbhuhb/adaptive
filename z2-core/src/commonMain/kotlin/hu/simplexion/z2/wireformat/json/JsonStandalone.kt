package hu.simplexion.z2.wireformat.json

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.wireformat.Standalone
import hu.simplexion.z2.wireformat.WireFormat
import hu.simplexion.z2.wireformat.json.elements.JsonArray
import hu.simplexion.z2.wireformat.json.elements.JsonElement
import hu.simplexion.z2.wireformat.json.elements.JsonNull
import hu.simplexion.z2.wireformat.json.elements.JsonObject
import kotlin.enums.EnumEntries

object JsonStandalone : Standalone<JsonWireFormatDecoder> {

    // ---------------------------------------------------------------------------
    // Any
    // ---------------------------------------------------------------------------

    override fun encodeAny(value: Any?): ByteArray =
        TODO()

    override fun encodeAnyList(value: List<Any>?): ByteArray =
        TODO()

    override fun decodeAny(decoder: JsonWireFormatDecoder?): Any =
        TODO()

    override fun decodeAnyOrNull(decoder: JsonWireFormatDecoder?): Any =
        TODO()

    override fun decodeAnyList(decoder: JsonWireFormatDecoder?): List<Any> =
        TODO()

    override fun decodeAnyListOrNull(decoder: JsonWireFormatDecoder?): List<Any> =
        TODO()

    // ---------------------------------------------------------------------------
    // Unit
    // ---------------------------------------------------------------------------

    override fun encodeUnit(value: Unit?): ByteArray =
        valueOrNull(value) { bool(true) }

    override fun encodeUnitList(value: List<Unit>?): ByteArray =
        arrayOrNull(value) { bool(true) }

    override fun decodeUnit(decoder: JsonWireFormatDecoder?): Unit =
        requireNotNull(decodeUnitOrNull(decoder))

    override fun decodeUnitOrNull(decoder: JsonWireFormatDecoder?): Unit? =
        fromValueOrNull(decoder) { asUnit }

    override fun decodeUnitList(decoder: JsonWireFormatDecoder?): List<Unit> =
        requireNotNull(decodeUnitListOrNull(decoder))

    override fun decodeUnitListOrNull(decoder: JsonWireFormatDecoder?): List<Unit>? =
        fromArray(decoder) { asUnit }

    // ---------------------------------------------------------------------------
    // Boolean
    // ---------------------------------------------------------------------------

    override fun encodeBoolean(value: Boolean?): ByteArray =
        valueOrNull(value) { bool(it) }

    override fun encodeBooleanList(value: List<Boolean>?): ByteArray =
        arrayOrNull(value) { bool(it) }

    override fun decodeBoolean(decoder: JsonWireFormatDecoder?): Boolean =
        requireNotNull(decodeBooleanOrNull(decoder))

    override fun decodeBooleanOrNull(decoder: JsonWireFormatDecoder?): Boolean? =
        fromValueOrNull(decoder) { asBoolean }

    override fun decodeBooleanList(decoder: JsonWireFormatDecoder?): List<Boolean> =
        requireNotNull(decodeBooleanListOrNull(decoder))

    override fun decodeBooleanListOrNull(decoder: JsonWireFormatDecoder?): List<Boolean>? =
        fromArray(decoder) { asBoolean }

    // ---------------------------------------------------------------------------
    // Int
    // ---------------------------------------------------------------------------

    override fun encodeInt(value: Int?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeIntList(value: List<Int>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeInt(decoder: JsonWireFormatDecoder?): Int =
        requireNotNull(decodeIntOrNull(decoder))

    override fun decodeIntOrNull(decoder: JsonWireFormatDecoder?): Int? =
        fromValueOrNull(decoder) { asInt }

    override fun decodeIntList(decoder: JsonWireFormatDecoder?): List<Int> =
        requireNotNull(decodeIntListOrNull(decoder))

    override fun decodeIntListOrNull(decoder: JsonWireFormatDecoder?): List<Int>? =
        fromArray(decoder) { asInt }

    // ---------------------------------------------------------------------------
    // Short
    // ---------------------------------------------------------------------------

    override fun encodeShort(value: Short?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeShortList(value: List<Short>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeShort(decoder: JsonWireFormatDecoder?): Short =
        requireNotNull(decodeShortOrNull(decoder))

    override fun decodeShortOrNull(decoder: JsonWireFormatDecoder?): Short? =
        fromValueOrNull(decoder) { asShort }

    override fun decodeShortList(decoder: JsonWireFormatDecoder?): List<Short> =
        requireNotNull(decodeShortListOrNull(decoder))

    override fun decodeShortListOrNull(decoder: JsonWireFormatDecoder?): List<Short>? =
        fromArray(decoder) { asShort }

    // ---------------------------------------------------------------------------
    // Byte
    // ---------------------------------------------------------------------------

    override fun encodeByte(value: Byte?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeByteList(value: List<Byte>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeByte(decoder: JsonWireFormatDecoder?): Byte =
        requireNotNull(decodeByteOrNull(decoder))

    override fun decodeByteOrNull(decoder: JsonWireFormatDecoder?): Byte? =
        fromValueOrNull(decoder) { asByte }

    override fun decodeByteList(decoder: JsonWireFormatDecoder?): List<Byte> =
        requireNotNull(decodeByteListOrNull(decoder))

    override fun decodeByteListOrNull(decoder: JsonWireFormatDecoder?): List<Byte>? =
        fromArray(decoder) { asByte }
    
    // ---------------------------------------------------------------------------
    // Long
    // ---------------------------------------------------------------------------

    override fun encodeLong(value: Long?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeLongList(value: List<Long>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeLong(decoder: JsonWireFormatDecoder?): Long =
        requireNotNull(decodeLongOrNull(decoder))

    override fun decodeLongOrNull(decoder: JsonWireFormatDecoder?): Long? =
        fromValueOrNull(decoder) { asLong }

    override fun decodeLongList(decoder: JsonWireFormatDecoder?): List<Long> =
        requireNotNull(decodeLongListOrNull(decoder))

    override fun decodeLongListOrNull(decoder: JsonWireFormatDecoder?): List<Long>? =
        fromArray(decoder) { asLong }

    // ---------------------------------------------------------------------------
    // Float
    // ---------------------------------------------------------------------------

    override fun encodeFloat(value: Float?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeFloatList(value: List<Float>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeFloat(decoder: JsonWireFormatDecoder?): Float =
        requireNotNull(decodeFloatOrNull(decoder))

    override fun decodeFloatOrNull(decoder: JsonWireFormatDecoder?): Float? =
        fromValueOrNull(decoder) { asFloat }

    override fun decodeFloatList(decoder: JsonWireFormatDecoder?): List<Float> =
        requireNotNull(decodeFloatListOrNull(decoder))

    override fun decodeFloatListOrNull(decoder: JsonWireFormatDecoder?): List<Float>? =
        fromArray(decoder) { asFloat }

    // ---------------------------------------------------------------------------
    // Double
    // ---------------------------------------------------------------------------

    override fun encodeDouble(value: Double?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeDoubleList(value: List<Double>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeDouble(decoder: JsonWireFormatDecoder?): Double =
        requireNotNull(decodeDoubleOrNull(decoder))

    override fun decodeDoubleOrNull(decoder: JsonWireFormatDecoder?): Double? =
        fromValueOrNull(decoder) { asDouble }

    override fun decodeDoubleList(decoder: JsonWireFormatDecoder?): List<Double> =
        requireNotNull(decodeDoubleListOrNull(decoder))

    override fun decodeDoubleListOrNull(decoder: JsonWireFormatDecoder?): List<Double>? =
        fromArray(decoder) { asDouble }

    // ---------------------------------------------------------------------------
    // Char
    // ---------------------------------------------------------------------------

    override fun encodeChar(value: Char?): ByteArray =
        valueOrNull(value) { quotedString(it.toString()) }

    override fun encodeCharList(value: List<Char>?): ByteArray =
        arrayOrNull(value) { quotedString(it.toString()) }

    override fun decodeChar(decoder: JsonWireFormatDecoder?): Char =
        requireNotNull(decodeCharOrNull(decoder))

    override fun decodeCharOrNull(decoder: JsonWireFormatDecoder?): Char? =
        fromValueOrNull(decoder) { asChar }

    override fun decodeCharList(decoder: JsonWireFormatDecoder?): List<Char> =
        requireNotNull(decodeCharListOrNull(decoder))

    override fun decodeCharListOrNull(decoder: JsonWireFormatDecoder?): List<Char>? =
        fromArray(decoder) { asChar }
    
    // ---------------------------------------------------------------------------
    // String
    // ---------------------------------------------------------------------------

    override fun encodeString(value: String?): ByteArray =
        valueOrNull(value) { quotedString(it) }

    override fun encodeStringList(value: List<String>?): ByteArray =
        arrayOrNull(value) { quotedString(it) }

    override fun decodeString(decoder: JsonWireFormatDecoder?): String =
        requireNotNull(decodeStringOrNull(decoder))

    override fun decodeStringOrNull(decoder: JsonWireFormatDecoder?): String? =
        fromValueOrNull(decoder) { asString }

    override fun decodeStringList(decoder: JsonWireFormatDecoder?): List<String> =
        requireNotNull(decodeStringListOrNull(decoder))

    override fun decodeStringListOrNull(decoder: JsonWireFormatDecoder?): List<String>? =
        fromArray(decoder) { asString }

    // ---------------------------------------------------------------------------
    // ByteArray
    // ---------------------------------------------------------------------------

    override fun encodeByteArray(value: ByteArray?): ByteArray =
        valueOrNull(value) { bytes(it) }

    override fun encodeByteArrayList(value: List<ByteArray>?): ByteArray =
        arrayOrNull(value) { bytes(it) }

    override fun decodeByteArray(decoder: JsonWireFormatDecoder?): ByteArray =
        requireNotNull(decodeByteArrayOrNull(decoder))

    override fun decodeByteArrayOrNull(decoder: JsonWireFormatDecoder?): ByteArray? =
        fromValueOrNull(decoder) { asByteArray }

    override fun decodeByteArrayList(decoder: JsonWireFormatDecoder?): List<ByteArray> =
        requireNotNull(decodeByteArrayListOrNull(decoder))

    override fun decodeByteArrayListOrNull(decoder: JsonWireFormatDecoder?): List<ByteArray>? =
        fromArray(decoder) { asByteArray }

    // ---------------------------------------------------------------------------
    // UUID
    // ---------------------------------------------------------------------------

    override fun encodeUuid(value: UUID<*>?): ByteArray =
        valueOrNull(value) { uuid(it) }

    override fun encodeUuidList(value: List<UUID<*>>?): ByteArray =
        arrayOrNull(value) { uuid(it) }

    override fun decodeUuid(decoder: JsonWireFormatDecoder?): UUID<Any> =
        requireNotNull(decodeUuidOrNull(decoder))

    override fun decodeUuidOrNull(decoder: JsonWireFormatDecoder?): UUID<Any>? =
        fromValueOrNull(decoder) { asUuid() }

    override fun decodeUuidList(decoder: JsonWireFormatDecoder?): List<UUID<Any>> =
        requireNotNull(decodeUuidListOrNull(decoder))

    override fun decodeUuidListOrNull(decoder: JsonWireFormatDecoder?): List<UUID<Any>>? =
        fromArray(decoder) { asUuid() }

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

    override fun <T> decodeInstance(decoder: JsonWireFormatDecoder?, wireFormat: WireFormat<T>): T =
        requireNotNull(decodeInstanceOrNull(decoder, wireFormat))

    override fun <T> decodeInstanceOrNull(decoder: JsonWireFormatDecoder?, wireFormat: WireFormat<T>): T? =
        fromValueOrNull(decoder) { wireFormat.wireFormatDecode(JsonWireFormatDecoder(decoder?.root as JsonObject)) }

    override fun <T> decodeInstanceList(decoder: JsonWireFormatDecoder?, wireFormat: WireFormat<T>): List<T> =
        requireNotNull(decodeInstanceListOrNull(decoder, wireFormat))

    override fun <T> decodeInstanceListOrNull(decoder: JsonWireFormatDecoder?, wireFormat: WireFormat<T>): List<T>? =
        fromArray(decoder) { wireFormat.wireFormatDecode(JsonWireFormatDecoder(this as JsonObject)) }

    // ---------------------------------------------------------------------------
    // Enum
    // ---------------------------------------------------------------------------

    override fun <E : Enum<E>> encodeEnum(value: E?, entries: EnumEntries<E>): ByteArray =
        valueOrNull(value) { quotedString(it.name) }

    override fun <E : Enum<E>> encodeEnumList(value: List<E>?, entries: EnumEntries<E>): ByteArray =
        arrayOrNull(value) { quotedString(it.name) }

    override fun <E : Enum<E>> decodeEnum(decoder: JsonWireFormatDecoder?, entries: EnumEntries<E>): E =
        requireNotNull(decodeEnumOrNull(decoder, entries))

    override fun <E : Enum<E>> decodeEnumOrNull(decoder: JsonWireFormatDecoder?, entries: EnumEntries<E>): E? {
        if (decoder == null) return null
        if (decoder.root is JsonNull) return null
        val name = decoder.root.asString
        return entries.first { it.name == name }
    }

    override fun <E : Enum<E>> decodeEnumList(decoder: JsonWireFormatDecoder?, entries: EnumEntries<E>): List<E> =
        requireNotNull(decodeEnumListOrNull(decoder, entries))

    override fun <E : Enum<E>> decodeEnumListOrNull(decoder: JsonWireFormatDecoder?, entries: EnumEntries<E>): List<E>? =
        fromArray(decoder) {
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

    override fun decodeUInt(decoder: JsonWireFormatDecoder?): UInt =
        requireNotNull(decodeUIntOrNull(decoder))

    override fun decodeUIntOrNull(decoder: JsonWireFormatDecoder?): UInt? =
        fromValueOrNull(decoder) { asUInt }

    override fun decodeUIntList(decoder: JsonWireFormatDecoder?): List<UInt> =
        requireNotNull(decodeUIntListOrNull(decoder))

    override fun decodeUIntListOrNull(decoder: JsonWireFormatDecoder?): List<UInt>? =
        fromArray(decoder) { asUInt }

    // ---------------------------------------------------------------------------
    // UShort
    // ---------------------------------------------------------------------------

    override fun encodeUShort(value: UShort?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeUShortList(value: List<UShort>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeUShort(decoder: JsonWireFormatDecoder?): UShort =
        requireNotNull(decodeUShortOrNull(decoder))

    override fun decodeUShortOrNull(decoder: JsonWireFormatDecoder?): UShort? =
        fromValueOrNull(decoder) { asUShort }

    override fun decodeUShortList(decoder: JsonWireFormatDecoder?): List<UShort> =
        requireNotNull(decodeUShortListOrNull(decoder))

    override fun decodeUShortListOrNull(decoder: JsonWireFormatDecoder?): List<UShort>? =
        fromArray(decoder) { asUShort }

    // ---------------------------------------------------------------------------
    // UByte
    // ---------------------------------------------------------------------------

    override fun encodeUByte(value: UByte?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeUByteList(value: List<UByte>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeUByte(decoder: JsonWireFormatDecoder?): UByte =
        requireNotNull(decodeUByteOrNull(decoder))

    override fun decodeUByteOrNull(decoder: JsonWireFormatDecoder?): UByte? =
        fromValueOrNull(decoder) { asUByte }

    override fun decodeUByteList(decoder: JsonWireFormatDecoder?): List<UByte> =
        requireNotNull(decodeUByteListOrNull(decoder))

    override fun decodeUByteListOrNull(decoder: JsonWireFormatDecoder?): List<UByte>? =
        fromArray(decoder) { asUByte }

    // ---------------------------------------------------------------------------
    // ULong
    // ---------------------------------------------------------------------------

    override fun encodeULong(value: ULong?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeULongList(value: List<ULong>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeULong(decoder: JsonWireFormatDecoder?): ULong =
        requireNotNull(decodeULongOrNull(decoder))

    override fun decodeULongOrNull(decoder: JsonWireFormatDecoder?): ULong? =
        fromValueOrNull(decoder) { asULong }

    override fun decodeULongList(decoder: JsonWireFormatDecoder?): List<ULong> =
        requireNotNull(decodeULongListOrNull(decoder))

    override fun decodeULongListOrNull(decoder: JsonWireFormatDecoder?): List<ULong>? =
        fromArray(decoder) { asULong }

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

    private fun <T> fromValueOrNull(decoder: JsonWireFormatDecoder?, valueFun: JsonElement.() -> T): T? {
        if (decoder == null) return null
        if (decoder.root is JsonNull) return null
        return decoder.root.valueFun()
    }

    private fun <T> fromArray(decoder: JsonWireFormatDecoder?, valueFun: JsonElement.() -> T): List<T>? {
        val element = decoder?.root

        if (element == null) return null
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
