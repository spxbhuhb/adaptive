/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.json

import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.json.elements.JsonArray
import hu.simplexion.adaptive.wireformat.json.elements.JsonElement
import hu.simplexion.adaptive.wireformat.json.elements.JsonNull
import hu.simplexion.adaptive.wireformat.json.elements.JsonObject
import kotlin.enums.EnumEntries

@OptIn(ExperimentalUnsignedTypes::class)
class JsonWireFormatDecoder : WireFormatDecoder<JsonElement> {

    val root: JsonElement
    val map: MutableMap<String, JsonElement>?

    constructor(wireFormat: ByteArray, offset: Int = 0, length: Int = wireFormat.size) {
        root = JsonBufferReader(wireFormat, offset, length).read()
        map = (root as? JsonObject)?.entries
    }

    constructor(root: JsonElement) {
        this.root = root
        map = (root as? JsonObject)?.entries
    }

    fun get(fieldName: String): JsonElement =
        requireNotNull(map?.get(fieldName)) { "missing field: $fieldName" }

    fun getOrNull(fieldName: String): JsonElement? =
        map?.get(fieldName)?.let { if (it is JsonNull) null else it }

    // -----------------------------------------------------------------------------------------
    // Any
    // -----------------------------------------------------------------------------------------

    override fun any(fieldNumber: Int, fieldName: String): Any =
        TODO()

    override fun anyOrNull(fieldNumber: Int, fieldName: String): Any =
        TODO()

    override fun rawAny(source: JsonElement): Any {
        TODO("Not yet implemented")
    }

    // -----------------------------------------------------------------------------------------
    // Unit
    // -----------------------------------------------------------------------------------------

    override fun unit(fieldNumber: Int, fieldName: String) {
        get(fieldName).asUnit
    }

    override fun unitOrNull(fieldNumber: Int, fieldName: String): Unit? =
        getOrNull(fieldName)?.asUnit

    override fun rawUnit(source: JsonElement) {
        return source.asUnit
    }

    // -----------------------------------------------------------------------------------------
    // Boolean
    // -----------------------------------------------------------------------------------------

    override fun boolean(fieldNumber: Int, fieldName: String): Boolean =
        get(fieldName).asBoolean

    override fun booleanOrNull(fieldNumber: Int, fieldName: String): Boolean? =
        getOrNull(fieldName)?.asBoolean

    override fun rawBoolean(source: JsonElement): Boolean {
        return source.asBoolean
    }

    // -----------------------------------------------------------------------------------------
    // Int
    // -----------------------------------------------------------------------------------------

    override fun int(fieldNumber: Int, fieldName: String): Int =
        get(fieldName).asInt

    override fun intOrNull(fieldNumber: Int, fieldName: String): Int? =
        getOrNull(fieldName)?.asInt

    override fun rawInt(source: JsonElement): Int {
        return source.asInt
    }

    // -----------------------------------------------------------------------------------------
    // Short
    // -----------------------------------------------------------------------------------------

    override fun short(fieldNumber: Int, fieldName: String): Short =
        get(fieldName).asShort

    override fun shortOrNull(fieldNumber: Int, fieldName: String): Short? =
        getOrNull(fieldName)?.asShort

    override fun rawShort(source: JsonElement): Short {
        return source.asShort
    }

    // -----------------------------------------------------------------------------------------
    // Byte
    // -----------------------------------------------------------------------------------------

    override fun byte(fieldNumber: Int, fieldName: String): Byte =
        get(fieldName).asByte

    override fun byteOrNull(fieldNumber: Int, fieldName: String): Byte? =
        getOrNull(fieldName)?.asByte

    override fun rawByte(source: JsonElement): Byte {
        return source.asByte
    }

    // -----------------------------------------------------------------------------------------
    // Long
    // -----------------------------------------------------------------------------------------

    override fun long(fieldNumber: Int, fieldName: String): Long =
        get(fieldName).asLong

    override fun longOrNull(fieldNumber: Int, fieldName: String): Long? =
        getOrNull(fieldName)?.asLong

    override fun rawLong(source: JsonElement): Long {
        return source.asLong
    }

    // -----------------------------------------------------------------------------------------
    // Float
    // -----------------------------------------------------------------------------------------

    override fun float(fieldNumber: Int, fieldName: String): Float =
        get(fieldName).asFloat

    override fun floatOrNull(fieldNumber: Int, fieldName: String): Float? =
        getOrNull(fieldName)?.asFloat

    override fun rawFloat(source: JsonElement): Float {
        return source.asFloat
    }

    // -----------------------------------------------------------------------------------------
    // Double
    // -----------------------------------------------------------------------------------------

    override fun double(fieldNumber: Int, fieldName: String): Double =
        get(fieldName).asDouble

    override fun doubleOrNull(fieldNumber: Int, fieldName: String): Double? =
        getOrNull(fieldName)?.asDouble

    override fun rawDouble(source: JsonElement): Double {
        return source.asDouble
    }

    // -----------------------------------------------------------------------------------------
    // Char
    // -----------------------------------------------------------------------------------------

    override fun char(fieldNumber: Int, fieldName: String): Char =
        get(fieldName).asChar

    override fun charOrNull(fieldNumber: Int, fieldName: String): Char? =
        getOrNull(fieldName)?.asChar

    override fun rawChar(source: JsonElement): Char {
        return source.asChar
    }

    // -----------------------------------------------------------------------------------------
    // BooleanArray
    // -----------------------------------------------------------------------------------------

    override fun booleanArray(fieldNumber: Int, fieldName: String): BooleanArray =
        requireNotNull(booleanArrayOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun booleanArrayOrNull(fieldNumber: Int, fieldName: String): BooleanArray? =
        array(fieldName) { it.asBoolean }?.toBooleanArray()

    override fun rawBooleanArray(source: JsonElement): BooleanArray {
        source as JsonArray
        return source.items.map { it.asBoolean }.toBooleanArray()
    }

    // -----------------------------------------------------------------------------------------
    // IntArray
    // -----------------------------------------------------------------------------------------

    override fun intArray(fieldNumber: Int, fieldName: String): IntArray =
        requireNotNull(intArrayOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun intArrayOrNull(fieldNumber: Int, fieldName: String): IntArray? =
        array(fieldName) { it.asInt }?.toIntArray()

    override fun rawIntArray(source: JsonElement): IntArray {
        source as JsonArray
        return source.items.map { it.asInt }.toIntArray()
    }

    // -----------------------------------------------------------------------------------------
    // ShortArray
    // -----------------------------------------------------------------------------------------

    override fun shortArray(fieldNumber: Int, fieldName: String): ShortArray =
        requireNotNull(shortArrayOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun shortArrayOrNull(fieldNumber: Int, fieldName: String): ShortArray? =
        array(fieldName) { it.asShort }?.toShortArray()

    override fun rawShortArray(source: JsonElement): ShortArray {
        source as JsonArray
        return source.items.map { it.asShort }.toShortArray()
    }

    // -----------------------------------------------------------------------------------------
    // ByteArray
    // -----------------------------------------------------------------------------------------

    override fun byteArray(fieldNumber: Int, fieldName: String): ByteArray =
        get(fieldName).asByteArray

    override fun byteArrayOrNull(fieldNumber: Int, fieldName: String): ByteArray? =
        getOrNull(fieldName)?.asByteArray

    override fun rawByteArray(source: JsonElement): ByteArray {
        return source.asByteArray
    }

    // -----------------------------------------------------------------------------------------
    // LongArray
    // -----------------------------------------------------------------------------------------

    override fun longArray(fieldNumber: Int, fieldName: String): LongArray =
        requireNotNull(longArrayOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun longArrayOrNull(fieldNumber: Int, fieldName: String): LongArray? =
        array(fieldName) { it.asLong }?.toLongArray()

    override fun rawLongArray(source: JsonElement): LongArray {
        source as JsonArray
        return source.items.map { it.asLong }.toLongArray()
    }

    // -----------------------------------------------------------------------------------------
    // FloatArray
    // -----------------------------------------------------------------------------------------

    override fun floatArray(fieldNumber: Int, fieldName: String): FloatArray =
        requireNotNull(floatArrayOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun floatArrayOrNull(fieldNumber: Int, fieldName: String): FloatArray? =
        array(fieldName) { it.asFloat }?.toFloatArray()

    override fun rawFloatArray(source: JsonElement): FloatArray {
        source as JsonArray
        return source.items.map { it.asFloat }.toFloatArray()
    }

    // -----------------------------------------------------------------------------------------
    // DoubleArray
    // -----------------------------------------------------------------------------------------

    override fun doubleArray(fieldNumber: Int, fieldName: String): DoubleArray =
        requireNotNull(doubleArrayOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun doubleArrayOrNull(fieldNumber: Int, fieldName: String): DoubleArray? =
        array(fieldName) { it.asDouble }?.toDoubleArray()

    override fun rawDoubleArray(source: JsonElement): DoubleArray {
        source as JsonArray
        return source.items.map { it.asDouble }.toDoubleArray()
    }

    // -----------------------------------------------------------------------------------------
    // CharArray
    // -----------------------------------------------------------------------------------------

    override fun charArray(fieldNumber: Int, fieldName: String): CharArray =
        requireNotNull(charArrayOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun charArrayOrNull(fieldNumber: Int, fieldName: String): CharArray? =
        array(fieldName) { it.asChar }?.toCharArray()

    override fun rawCharArray(source: JsonElement): CharArray {
        source as JsonArray
        return source.items.map { it.asChar }.toCharArray()
    }

    // -----------------------------------------------------------------------------------------
    // String
    // -----------------------------------------------------------------------------------------

    override fun string(fieldNumber: Int, fieldName: String): String =
        get(fieldName).asString

    override fun stringOrNull(fieldNumber: Int, fieldName: String): String? =
        getOrNull(fieldName)?.asString

    override fun rawString(source: JsonElement): String {
        return source.asString
    }

    // -----------------------------------------------------------------------------------------
    // Enum
    // -----------------------------------------------------------------------------------------

    override fun <E : Enum<E>> enum(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>): E =
        entries.first { it.name == string(fieldNumber, fieldName) }

    override fun <E : Enum<E>> enumOrNull(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>): E? =
        stringOrNull(fieldNumber, fieldName)?.let { s -> entries.first { it.name == s } }

    override fun <E : Enum<E>> rawEnum(source: JsonElement, entries: EnumEntries<E>): E =
        entries.first { it.name == rawString(source) }

    // -----------------------------------------------------------------------------------------
    // UUID
    // -----------------------------------------------------------------------------------------

    override fun <T> uuid(fieldNumber: Int, fieldName: String): UUID<T> =
        get(fieldName).asUuid()

    override fun <T> uuidOrNull(fieldNumber: Int, fieldName: String): UUID<T>? =
        getOrNull(fieldName)?.asUuid()

    override fun <T> rawUuid(source: JsonElement): UUID<T> {
        return source.asUuid()
    }

    // -----------------------------------------------------------------------------------------
    // UInt
    // -----------------------------------------------------------------------------------------

    override fun uInt(fieldNumber: Int, fieldName: String): UInt =
        get(fieldName).asUInt

    override fun uIntOrNull(fieldNumber: Int, fieldName: String): UInt? =
        getOrNull(fieldName)?.asUInt

    override fun rawUInt(source: JsonElement): UInt {
        return source.asUInt
    }

    // -----------------------------------------------------------------------------------------
    // UShort
    // -----------------------------------------------------------------------------------------

    override fun uShort(fieldNumber: Int, fieldName: String): UShort =
        get(fieldName).asUShort

    override fun uShortOrNull(fieldNumber: Int, fieldName: String): UShort? =
        getOrNull(fieldName)?.asUShort

    override fun rawUShort(source: JsonElement): UShort {
        return source.asUShort
    }

    // -----------------------------------------------------------------------------------------
    // UByte
    // -----------------------------------------------------------------------------------------

    override fun uByte(fieldNumber: Int, fieldName: String): UByte =
        get(fieldName).asUByte

    override fun uByteOrNull(fieldNumber: Int, fieldName: String): UByte? =
        getOrNull(fieldName)?.asUByte

    override fun rawUByte(source: JsonElement): UByte {
        return source.asUByte
    }

    // -----------------------------------------------------------------------------------------
    // ULong
    // -----------------------------------------------------------------------------------------

    override fun uLong(fieldNumber: Int, fieldName: String): ULong =
        get(fieldName).asULong

    override fun uLongOrNull(fieldNumber: Int, fieldName: String): ULong? =
        getOrNull(fieldName)?.asULong

    override fun rawULong(source: JsonElement): ULong {
        return source.asULong
    }

    // -----------------------------------------------------------------------------------------
    // UIntArray
    // -----------------------------------------------------------------------------------------

    override fun uIntArray(fieldNumber: Int, fieldName: String): UIntArray =
        requireNotNull(uIntArrayOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun uIntArrayOrNull(fieldNumber: Int, fieldName: String): UIntArray? =
        array(fieldName) { it.asUInt }?.toUIntArray()

    override fun rawUIntArray(source: JsonElement): UIntArray {
        source as JsonArray
        return source.items.map { it.asUInt }.toUIntArray()
    }

    // -----------------------------------------------------------------------------------------
    // UShortArray
    // -----------------------------------------------------------------------------------------

    override fun uShortArray(fieldNumber: Int, fieldName: String): UShortArray =
        requireNotNull(uShortArrayOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun uShortArrayOrNull(fieldNumber: Int, fieldName: String): UShortArray? =
        array(fieldName) { it.asUShort }?.toUShortArray()

    override fun rawUShortArray(source: JsonElement): UShortArray {
        source as JsonArray
        return source.items.map { it.asUShort }.toUShortArray()
    }

    // -----------------------------------------------------------------------------------------
    // UByteArray
    // -----------------------------------------------------------------------------------------

    override fun uByteArray(fieldNumber: Int, fieldName: String): UByteArray =
        requireNotNull(uByteArrayOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun uByteArrayOrNull(fieldNumber: Int, fieldName: String): UByteArray? =
        array(fieldName) { it.asUByte }?.toUByteArray()

    override fun rawUByteArray(source: JsonElement): UByteArray {
        source as JsonArray
        return source.items.map { it.asUByte }.toUByteArray()
    }

    // -----------------------------------------------------------------------------------------
    // ULongArray
    // -----------------------------------------------------------------------------------------

    override fun uLongArray(fieldNumber: Int, fieldName: String): ULongArray =
        requireNotNull(uLongArrayOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun uLongArrayOrNull(fieldNumber: Int, fieldName: String): ULongArray? =
        array(fieldName) { it.asULong }?.toULongArray()

    override fun rawULongArray(source: JsonElement): ULongArray {
        source as JsonArray
        return source.items.map { it.asULong }.toULongArray()
    }

    // -----------------------------------------------------------------------------------------
    // Instance
    // -----------------------------------------------------------------------------------------

    override fun <T> instance(fieldNumber: Int, fieldName: String, wireFormat: WireFormat<T>): T {
        val element = get(fieldName)
        return wireFormat.wireFormatDecode(element, JsonWireFormatDecoder(element))
    }

    override fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, wireFormat: WireFormat<T>): T? =
        if (getOrNull(fieldName) == null) null else instance(fieldNumber, fieldName, wireFormat)

    override fun <T> rawInstance(source: JsonElement, wireFormat: WireFormat<T>): T {
        return wireFormat.wireFormatDecode(source, JsonWireFormatDecoder(source))
    }

    override fun <T> asInstance(wireFormat: WireFormat<T>): T =
        rawInstance(root, wireFormat)

    override fun <T> asInstanceOrNull(wireFormat: WireFormat<T>): T? =
        if (root is JsonNull) null else rawInstance(root, wireFormat)

    // -----------------------------------------------------------------------------------------
    // Utilities for classes that implement `WireFormat`
    // -----------------------------------------------------------------------------------------

    override fun <T> items(source: JsonElement, itemWireFormat: WireFormat<T>): MutableList<T> {
        val result = mutableListOf<T>()

        require(source is JsonArray) { "source is not an array" }

        for (element in source.items) {
            result += itemWireFormat.wireFormatDecode(element, JsonWireFormatDecoder(element))
        }

        return result
    }

    // --------------------------------------------------------------------------------------
    // Helpers
    // --------------------------------------------------------------------------------------

    fun <T> array(
        fieldName: String, item: (element: JsonElement) -> T
    ): MutableList<T>? {
        val result = mutableListOf<T>()

        val a = getOrNull(fieldName) ?: return null
        require(a is JsonArray) { "field $fieldName is not an array" }

        for (element in a.items) {
            result += item(element)
        }

        return result
    }

}