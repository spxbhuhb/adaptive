package hu.simplexion.z2.wireformat.json

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.wireformat.WireFormat
import hu.simplexion.z2.wireformat.WireFormatDecoder
import hu.simplexion.z2.wireformat.json.elements.JsonArray
import hu.simplexion.z2.wireformat.json.elements.JsonElement
import hu.simplexion.z2.wireformat.json.elements.JsonNull
import hu.simplexion.z2.wireformat.json.elements.JsonObject
import kotlin.enums.EnumEntries

class JsonWireFormatDecoder : WireFormatDecoder<JsonElement> {

    val root: JsonElement
    val map: MutableMap<String, JsonElement>?

    constructor(wireFormat: ByteArray, offset: Int = 0, length: Int = wireFormat.size) {
        root = JsonBufferReader(wireFormat, offset, length).read()
        map = (root as? JsonObject)?.entries
    }

    constructor(root: JsonObject) {
        this.root = root
        map = root.entries
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

    override fun anyList(fieldNumber: Int, fieldName: String) =
        requireNotNull(anyListOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun anyListOrNull(fieldNumber: Int, fieldName: String): List<Any>? =
        array(fieldName) { TODO() }

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

    override fun unitList(fieldNumber: Int, fieldName: String) =
        requireNotNull(unitListOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun unitListOrNull(fieldNumber: Int, fieldName: String): List<Unit>? =
        array(fieldName) { it.asUnit }

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

    override fun booleanList(fieldNumber: Int, fieldName: String) =
        requireNotNull(booleanListOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun booleanListOrNull(fieldNumber: Int, fieldName: String): List<Boolean>? =
        array(fieldName) { it.asBoolean }

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

    override fun intList(fieldNumber: Int, fieldName: String) =
        requireNotNull(intListOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun intListOrNull(fieldNumber: Int, fieldName: String): List<Int>? =
        array(fieldName) { it.asInt }

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

    override fun shortList(fieldNumber: Int, fieldName: String) =
        requireNotNull(shortListOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun shortListOrNull(fieldNumber: Int, fieldName: String): List<Short>? =
        array(fieldName) { it.asShort }

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

    override fun byteList(fieldNumber: Int, fieldName: String) =
        requireNotNull(byteListOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun byteListOrNull(fieldNumber: Int, fieldName: String): List<Byte>? =
        array(fieldName) { it.asByte }

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

    override fun longList(fieldNumber: Int, fieldName: String) =
        requireNotNull(longListOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun longListOrNull(fieldNumber: Int, fieldName: String): List<Long>? =
        array(fieldName) { it.asLong }

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

    override fun floatList(fieldNumber: Int, fieldName: String) =
        requireNotNull(floatListOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun floatListOrNull(fieldNumber: Int, fieldName: String): List<Float>? =
        array(fieldName) { it.asFloat }

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

    override fun doubleList(fieldNumber: Int, fieldName: String) =
        requireNotNull(doubleListOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun doubleListOrNull(fieldNumber: Int, fieldName: String): List<Double>? =
        array(fieldName) { it.asDouble }

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

    override fun charList(fieldNumber: Int, fieldName: String) =
        requireNotNull(charListOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun charListOrNull(fieldNumber: Int, fieldName: String): List<Char>? =
        array(fieldName) { it.asChar }

    override fun rawChar(source: JsonElement): Char {
        return source.asChar
    }

    // -----------------------------------------------------------------------------------------
    // String
    // -----------------------------------------------------------------------------------------

    override fun string(fieldNumber: Int, fieldName: String): String =
        get(fieldName).asString

    override fun stringOrNull(fieldNumber: Int, fieldName: String): String? =
        getOrNull(fieldName)?.asString

    override fun stringList(fieldNumber: Int, fieldName: String) =
        requireNotNull(stringListOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun stringListOrNull(fieldNumber: Int, fieldName: String): List<String>? =
        array(fieldName) { it.asString }

    override fun rawString(source: JsonElement): String {
        return source.asString
    }

    // -----------------------------------------------------------------------------------------
    // ByteArray
    // -----------------------------------------------------------------------------------------

    override fun byteArray(fieldNumber: Int, fieldName: String): ByteArray =
        get(fieldName).asByteArray

    override fun byteArrayOrNull(fieldNumber: Int, fieldName: String): ByteArray? =
        getOrNull(fieldName)?.asByteArray

    override fun byteArrayList(fieldNumber: Int, fieldName: String) =
        requireNotNull(byteArrayListOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun byteArrayListOrNull(fieldNumber: Int, fieldName: String): List<ByteArray>? =
        array(fieldName) { it.asByteArray }

    override fun rawByteArray(source: JsonElement): ByteArray {
        return source.asByteArray
    }

    // -----------------------------------------------------------------------------------------
    // UUID
    // -----------------------------------------------------------------------------------------

    override fun <T> uuid(fieldNumber: Int, fieldName: String): UUID<T> =
        get(fieldName).asUuid()

    override fun <T> uuidOrNull(fieldNumber: Int, fieldName: String): UUID<T>? =
        getOrNull(fieldName)?.asUuid()

    override fun <T> uuidList(fieldNumber: Int, fieldName: String) =
        requireNotNull(uuidListOrNull<T>(fieldNumber, fieldName)) { "missing or null array" }

    override fun <T> uuidListOrNull(fieldNumber: Int, fieldName: String): List<UUID<T>>? =
        array(fieldName) { it.asUuid() }

    override fun rawUuid(source: JsonElement): UUID<*> {
        return source.asUuid<Any>()
    }

    // -----------------------------------------------------------------------------------------
    // Instance
    // -----------------------------------------------------------------------------------------

    override fun <T> instance(fieldNumber: Int, fieldName: String, wireFormat: WireFormat<T>): T {
        val element = get(fieldName)
        check(element is JsonObject)
        return wireFormat.wireFormatDecode(element, JsonWireFormatDecoder(element))
    }

    override fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, wireFormat: WireFormat<T>): T? =
        if (getOrNull(fieldName) == null) null else instance(fieldNumber, fieldName, wireFormat)

    override fun <T> instanceList(fieldNumber: Int, fieldName: String, wireFormat: WireFormat<T>): MutableList<T> =
        requireNotNull(instanceListOrNull(fieldNumber, fieldName, wireFormat)) { "missing or null instance" }

    override fun <T> instanceListOrNull(fieldNumber: Int, fieldName: String, wireFormat: WireFormat<T>): MutableList<T>? =
        array(fieldName) { wireFormat.wireFormatDecode(it, JsonWireFormatDecoder(it as JsonObject)) }

    override fun <T> rawInstance(source: JsonElement, wireFormat: WireFormat<T>): T {
        check(source is JsonObject)
        return wireFormat.wireFormatDecode(source, JsonWireFormatDecoder(source))
    }

    // -----------------------------------------------------------------------------------------
    // Enum
    // -----------------------------------------------------------------------------------------

    override fun <E : Enum<E>> enum(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>): E =
        entries.first { it.name == string(fieldNumber, fieldName) }

    override fun <E : Enum<E>> enumOrNull(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>): E? =
        stringOrNull(fieldNumber, fieldName)?.let { s -> entries.first { it.name == s } }

    override fun <E : Enum<E>> enumList(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>): MutableList<E> =
        checkNotNull(enumListOrNull(fieldNumber, fieldName, entries))

    override fun <E : Enum<E>> enumListOrNull(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>): MutableList<E>? =
        stringListOrNull(fieldNumber, fieldName)?.map { s -> entries.first { it.name == s } }?.toMutableList()

    override fun <E : Enum<E>> rawEnum(source: JsonElement, entries: EnumEntries<E>): E =
        entries.first { it.name == rawString(source) }

    // -----------------------------------------------------------------------------------------
    // UInt
    // -----------------------------------------------------------------------------------------

    override fun uInt(fieldNumber: Int, fieldName: String): UInt =
        get(fieldName).asUInt

    override fun uIntOrNull(fieldNumber: Int, fieldName: String): UInt? =
        getOrNull(fieldName)?.asUInt

    override fun uIntList(fieldNumber: Int, fieldName: String) =
        requireNotNull(uIntListOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun uIntListOrNull(fieldNumber: Int, fieldName: String): List<UInt>? =
        array(fieldName) { it.asUInt }

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

    override fun uShortList(fieldNumber: Int, fieldName: String) =
        requireNotNull(uShortListOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun uShortListOrNull(fieldNumber: Int, fieldName: String): List<UShort>? =
        array(fieldName) { it.asUShort }

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

    override fun uByteList(fieldNumber: Int, fieldName: String) =
        requireNotNull(uByteListOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun uByteListOrNull(fieldNumber: Int, fieldName: String): List<UByte>? =
        array(fieldName) { it.asUByte }

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

    override fun uLongList(fieldNumber: Int, fieldName: String) =
        requireNotNull(uLongListOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun uLongListOrNull(fieldNumber: Int, fieldName: String): List<ULong>? =
        array(fieldName) { it.asULong }

    override fun rawULong(source: JsonElement): ULong {
        return source.asULong
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