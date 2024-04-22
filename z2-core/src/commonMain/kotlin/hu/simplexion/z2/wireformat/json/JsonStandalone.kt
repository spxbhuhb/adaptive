package hu.simplexion.z2.wireformat.json

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.wireformat.Standalone
import hu.simplexion.z2.wireformat.WireFormat
import hu.simplexion.z2.wireformat.json.elements.JsonArray
import hu.simplexion.z2.wireformat.json.elements.JsonElement
import hu.simplexion.z2.wireformat.json.elements.JsonNull
import hu.simplexion.z2.wireformat.json.elements.JsonObject
import kotlin.enums.EnumEntries

object JsonStandalone : Standalone<JsonMessage> {

    // ---------------------------------------------------------------------------
    // Any
    // ---------------------------------------------------------------------------

    override fun encodeAny(value: Any?): ByteArray =
        TODO()

    override fun encodeAnyList(value: List<Any>?): ByteArray =
        TODO()

    override fun decodeAny(message: JsonMessage?): Any =
        TODO()

    override fun decodeAnyOrNull(message: JsonMessage?): Any? =
        TODO()

    override fun decodeAnyList(message: JsonMessage?): List<Any> =
        TODO()

    override fun decodeAnyListOrNull(message: JsonMessage?): List<Any>? =
        TODO()

    // ---------------------------------------------------------------------------
    // Unit
    // ---------------------------------------------------------------------------

    override fun encodeUnit(value: Unit?): ByteArray =
        valueOrNull(value) { bool(true) }

    override fun encodeUnitList(value: List<Unit>?): ByteArray =
        arrayOrNull(value) { bool(true) }

    override fun decodeUnit(message: JsonMessage?): Unit =
        requireNotNull(decodeUnitOrNull(message))

    override fun decodeUnitOrNull(message: JsonMessage?): Unit? =
        fromValueOrNull(message) { asUnit }

    override fun decodeUnitList(message: JsonMessage?): List<Unit> =
        requireNotNull(decodeUnitListOrNull(message))

    override fun decodeUnitListOrNull(message: JsonMessage?): List<Unit>? =
        fromArray(message) { asUnit }

    // ---------------------------------------------------------------------------
    // Boolean
    // ---------------------------------------------------------------------------

    override fun encodeBoolean(value: Boolean?): ByteArray =
        valueOrNull(value) { bool(it) }

    override fun encodeBooleanList(value: List<Boolean>?): ByteArray =
        arrayOrNull(value) { bool(it) }

    override fun decodeBoolean(message: JsonMessage?): Boolean =
        requireNotNull(decodeBooleanOrNull(message))

    override fun decodeBooleanOrNull(message: JsonMessage?): Boolean? =
        fromValueOrNull(message) { asBoolean }

    override fun decodeBooleanList(message: JsonMessage?): List<Boolean> =
        requireNotNull(decodeBooleanListOrNull(message))

    override fun decodeBooleanListOrNull(message: JsonMessage?): List<Boolean>? =
        fromArray(message) { asBoolean }

    // ---------------------------------------------------------------------------
    // Int
    // ---------------------------------------------------------------------------

    override fun encodeInt(value: Int?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeIntList(value: List<Int>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeInt(message: JsonMessage?): Int =
        requireNotNull(decodeIntOrNull(message))

    override fun decodeIntOrNull(message: JsonMessage?): Int? =
        fromValueOrNull(message) { asInt }

    override fun decodeIntList(message: JsonMessage?): List<Int> =
        requireNotNull(decodeIntListOrNull(message))

    override fun decodeIntListOrNull(message: JsonMessage?): List<Int>? =
        fromArray(message) { asInt }

    // ---------------------------------------------------------------------------
    // Short
    // ---------------------------------------------------------------------------

    override fun encodeShort(value: Short?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeShortList(value: List<Short>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeShort(message: JsonMessage?): Short =
        requireNotNull(decodeShortOrNull(message))

    override fun decodeShortOrNull(message: JsonMessage?): Short? =
        fromValueOrNull(message) { asShort }

    override fun decodeShortList(message: JsonMessage?): List<Short> =
        requireNotNull(decodeShortListOrNull(message))

    override fun decodeShortListOrNull(message: JsonMessage?): List<Short>? =
        fromArray(message) { asShort }

    // ---------------------------------------------------------------------------
    // Byte
    // ---------------------------------------------------------------------------

    override fun encodeByte(value: Byte?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeByteList(value: List<Byte>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeByte(message: JsonMessage?): Byte =
        requireNotNull(decodeByteOrNull(message))

    override fun decodeByteOrNull(message: JsonMessage?): Byte? =
        fromValueOrNull(message) { asByte }

    override fun decodeByteList(message: JsonMessage?): List<Byte> =
        requireNotNull(decodeByteListOrNull(message))

    override fun decodeByteListOrNull(message: JsonMessage?): List<Byte>? =
        fromArray(message) { asByte }
    
    // ---------------------------------------------------------------------------
    // Long
    // ---------------------------------------------------------------------------

    override fun encodeLong(value: Long?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeLongList(value: List<Long>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeLong(message: JsonMessage?): Long =
        requireNotNull(decodeLongOrNull(message))

    override fun decodeLongOrNull(message: JsonMessage?): Long? =
        fromValueOrNull(message) { asLong }

    override fun decodeLongList(message: JsonMessage?): List<Long> =
        requireNotNull(decodeLongListOrNull(message))

    override fun decodeLongListOrNull(message: JsonMessage?): List<Long>? =
        fromArray(message) { asLong }

    // ---------------------------------------------------------------------------
    // Float
    // ---------------------------------------------------------------------------

    override fun encodeFloat(value: Float?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeFloatList(value: List<Float>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeFloat(message: JsonMessage?): Float =
        requireNotNull(decodeFloatOrNull(message))

    override fun decodeFloatOrNull(message: JsonMessage?): Float? =
        fromValueOrNull(message) { asFloat }

    override fun decodeFloatList(message: JsonMessage?): List<Float> =
        requireNotNull(decodeFloatListOrNull(message))

    override fun decodeFloatListOrNull(message: JsonMessage?): List<Float>? =
        fromArray(message) { asFloat }

    // ---------------------------------------------------------------------------
    // Double
    // ---------------------------------------------------------------------------

    override fun encodeDouble(value: Double?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeDoubleList(value: List<Double>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeDouble(message: JsonMessage?): Double =
        requireNotNull(decodeDoubleOrNull(message))

    override fun decodeDoubleOrNull(message: JsonMessage?): Double? =
        fromValueOrNull(message) { asDouble }

    override fun decodeDoubleList(message: JsonMessage?): List<Double> =
        requireNotNull(decodeDoubleListOrNull(message))

    override fun decodeDoubleListOrNull(message: JsonMessage?): List<Double>? =
        fromArray(message) { asDouble }

    // ---------------------------------------------------------------------------
    // Char
    // ---------------------------------------------------------------------------

    override fun encodeChar(value: Char?): ByteArray =
        valueOrNull(value) { quotedString(it.toString()) }

    override fun encodeCharList(value: List<Char>?): ByteArray =
        arrayOrNull(value) { quotedString(it.toString()) }

    override fun decodeChar(message: JsonMessage?): Char =
        requireNotNull(decodeCharOrNull(message))

    override fun decodeCharOrNull(message: JsonMessage?): Char? =
        fromValueOrNull(message) { asChar }

    override fun decodeCharList(message: JsonMessage?): List<Char> =
        requireNotNull(decodeCharListOrNull(message))

    override fun decodeCharListOrNull(message: JsonMessage?): List<Char>? =
        fromArray(message) { asChar }
    
    // ---------------------------------------------------------------------------
    // String
    // ---------------------------------------------------------------------------

    override fun encodeString(value: String?): ByteArray =
        valueOrNull(value) { quotedString(it) }

    override fun encodeStringList(value: List<String>?): ByteArray =
        arrayOrNull(value) { quotedString(it) }

    override fun decodeString(message: JsonMessage?): String =
        requireNotNull(decodeStringOrNull(message))

    override fun decodeStringOrNull(message: JsonMessage?): String? =
        fromValueOrNull(message) { asString }

    override fun decodeStringList(message: JsonMessage?): List<String> =
        requireNotNull(decodeStringListOrNull(message))

    override fun decodeStringListOrNull(message: JsonMessage?): List<String>? =
        fromArray(message) { asString }

    // ---------------------------------------------------------------------------
    // ByteArray
    // ---------------------------------------------------------------------------

    override fun encodeByteArray(value: ByteArray?): ByteArray =
        valueOrNull(value) { bytes(it) }

    override fun encodeByteArrayList(value: List<ByteArray>?): ByteArray =
        arrayOrNull(value) { bytes(it) }

    override fun decodeByteArray(message: JsonMessage?): ByteArray =
        requireNotNull(decodeByteArrayOrNull(message))

    override fun decodeByteArrayOrNull(message: JsonMessage?): ByteArray? =
        fromValueOrNull(message) { asByteArray }

    override fun decodeByteArrayList(message: JsonMessage?): List<ByteArray> =
        requireNotNull(decodeByteArrayListOrNull(message))

    override fun decodeByteArrayListOrNull(message: JsonMessage?): List<ByteArray>? =
        fromArray(message) { asByteArray }

    // ---------------------------------------------------------------------------
    // UUID
    // ---------------------------------------------------------------------------

    override fun encodeUuid(value: UUID<*>?): ByteArray =
        valueOrNull(value) { uuid(it) }

    override fun encodeUuidList(value: List<UUID<*>>?): ByteArray =
        arrayOrNull(value) { uuid(it) }

    override fun decodeUuid(message: JsonMessage?): UUID<Any> =
        requireNotNull(decodeUuidOrNull(message))

    override fun decodeUuidOrNull(message: JsonMessage?): UUID<Any>? =
        fromValueOrNull(message) { asUuid() }

    override fun decodeUuidList(message: JsonMessage?): List<UUID<Any>> =
        requireNotNull(decodeUuidListOrNull(message))

    override fun decodeUuidListOrNull(message: JsonMessage?): List<UUID<Any>>? =
        fromArray(message) { asUuid() }

    // ---------------------------------------------------------------------------
    // Instance
    // ---------------------------------------------------------------------------

    override fun <T> encodeInstance(value: T?, encoder: WireFormat<T>): ByteArray =
        if (value == null) {
            JsonBufferWriter().apply { nullValue() }.pack()
        } else {
            JsonMessageBuilder().apply { encoder.encodeInstance(this, value) }.pack()
        }

    override fun <T> encodeInstanceList(value: List<T>?, encoder: WireFormat<T>): ByteArray =
        arrayOrNull(value) {
            encoder.encodeInstance(JsonMessageBuilder(this), it)
            if (peekLast() == 0x2c.toByte()) rollback() // to remove the last colon
        }

    override fun <T> decodeInstance(message: JsonMessage?, decoder: WireFormat<T>): T =
        requireNotNull(decodeInstanceOrNull(message, decoder))

    override fun <T> decodeInstanceOrNull(message: JsonMessage?, decoder: WireFormat<T>): T? =
        fromValueOrNull(message) { decoder.decodeInstance(JsonMessage(message?.root as JsonObject)) }

    override fun <T> decodeInstanceList(message: JsonMessage?, decoder: WireFormat<T>): List<T> =
        requireNotNull(decodeInstanceListOrNull(message, decoder))

    override fun <T> decodeInstanceListOrNull(message: JsonMessage?, decoder: WireFormat<T>): List<T>? =
        fromArray(message) { decoder.decodeInstance(JsonMessage(this as JsonObject)) }

    // ---------------------------------------------------------------------------
    // Enum
    // ---------------------------------------------------------------------------

    override fun <E : Enum<E>> encodeEnum(value: E?, entries: EnumEntries<E>): ByteArray =
        valueOrNull(value) { quotedString(it.name) }

    override fun <E : Enum<E>> encodeEnumList(value: List<E>?, entries: EnumEntries<E>): ByteArray =
        arrayOrNull(value) { quotedString(it.name) }

    override fun <E : Enum<E>> decodeEnum(message: JsonMessage?, entries: EnumEntries<E>): E =
        requireNotNull(decodeEnumOrNull(message, entries))

    override fun <E : Enum<E>> decodeEnumOrNull(message: JsonMessage?, entries: EnumEntries<E>): E? {
        if (message == null) return null
        if (message.root is JsonNull) return null
        val name = message.root.asString
        return entries.first { it.name == name }
    }

    override fun <E : Enum<E>> decodeEnumList(message: JsonMessage?, entries: EnumEntries<E>): List<E> =
        requireNotNull(decodeEnumListOrNull(message, entries))

    override fun <E : Enum<E>> decodeEnumListOrNull(message: JsonMessage?, entries: EnumEntries<E>): List<E>? =
        fromArray(message) {
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

    override fun decodeUInt(message: JsonMessage?): UInt =
        requireNotNull(decodeUIntOrNull(message))

    override fun decodeUIntOrNull(message: JsonMessage?): UInt? =
        fromValueOrNull(message) { asUInt }

    override fun decodeUIntList(message: JsonMessage?): List<UInt> =
        requireNotNull(decodeUIntListOrNull(message))

    override fun decodeUIntListOrNull(message: JsonMessage?): List<UInt>? =
        fromArray(message) { asUInt }

    // ---------------------------------------------------------------------------
    // UShort
    // ---------------------------------------------------------------------------

    override fun encodeUShort(value: UShort?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeUShortList(value: List<UShort>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeUShort(message: JsonMessage?): UShort =
        requireNotNull(decodeUShortOrNull(message))

    override fun decodeUShortOrNull(message: JsonMessage?): UShort? =
        fromValueOrNull(message) { asUShort }

    override fun decodeUShortList(message: JsonMessage?): List<UShort> =
        requireNotNull(decodeUShortListOrNull(message))

    override fun decodeUShortListOrNull(message: JsonMessage?): List<UShort>? =
        fromArray(message) { asUShort }

    // ---------------------------------------------------------------------------
    // UByte
    // ---------------------------------------------------------------------------

    override fun encodeUByte(value: UByte?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeUByteList(value: List<UByte>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeUByte(message: JsonMessage?): UByte =
        requireNotNull(decodeUByteOrNull(message))

    override fun decodeUByteOrNull(message: JsonMessage?): UByte? =
        fromValueOrNull(message) { asUByte }

    override fun decodeUByteList(message: JsonMessage?): List<UByte> =
        requireNotNull(decodeUByteListOrNull(message))

    override fun decodeUByteListOrNull(message: JsonMessage?): List<UByte>? =
        fromArray(message) { asUByte }

    // ---------------------------------------------------------------------------
    // ULong
    // ---------------------------------------------------------------------------

    override fun encodeULong(value: ULong?): ByteArray =
        valueOrNull(value) { number(it) }

    override fun encodeULongList(value: List<ULong>?): ByteArray =
        arrayOrNull(value) { number(it) }

    override fun decodeULong(message: JsonMessage?): ULong =
        requireNotNull(decodeULongOrNull(message))

    override fun decodeULongOrNull(message: JsonMessage?): ULong? =
        fromValueOrNull(message) { asULong }

    override fun decodeULongList(message: JsonMessage?): List<ULong> =
        requireNotNull(decodeULongListOrNull(message))

    override fun decodeULongListOrNull(message: JsonMessage?): List<ULong>? =
        fromArray(message) { asULong }

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

    private fun <T> fromValueOrNull(message: JsonMessage?, valueFun: JsonElement.() -> T): T? {
        if (message == null) return null
        if (message.root is JsonNull) return null
        return message.root.valueFun()
    }

    private fun <T> fromArray(message: JsonMessage?, valueFun: JsonElement.() -> T): List<T>? {
        val element = message?.root

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
