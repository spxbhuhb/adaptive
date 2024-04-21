package hu.simplexion.z2.wireformat.json

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.wireformat.Message
import hu.simplexion.z2.wireformat.WireFormat
import hu.simplexion.z2.wireformat.json.elements.*

class JsonMessage : Message {

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
    // Boolean
    // -----------------------------------------------------------------------------------------

    override fun boolean(fieldNumber: Int, fieldName: String): Boolean =
        (get(fieldName) as JsonBoolean).value

    override fun booleanOrNull(fieldNumber: Int, fieldName: String): Boolean? =
        (getOrNull(fieldName) as? JsonBoolean)?.value

    override fun booleanList(fieldNumber: Int, fieldName: String) =
        requireNotNull(booleanListOrNull(fieldNumber, fieldName)) { "missing or null array" }

    override fun booleanListOrNull(fieldNumber: Int, fieldName: String): List<Boolean>? =
        array(fieldName) { (it as JsonBoolean).value }

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

    // -----------------------------------------------------------------------------------------
    // Instance
    // -----------------------------------------------------------------------------------------

    override fun <T> instance(fieldNumber: Int, fieldName: String, decoder: WireFormat<T>): T {
        val element = get(fieldName)
        check(element is JsonObject)
        return decoder.decodeInstance(JsonMessage(element))
    }

    override fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, decoder: WireFormat<T>): T? =
        if (getOrNull(fieldName) == null) null else instance(fieldNumber, fieldName, decoder)

    override fun <T> instanceList(fieldNumber: Int, fieldName: String, decoder: WireFormat<T>): MutableList<T> =
        requireNotNull(instanceListOrNull(fieldNumber, fieldName, decoder)) { "missing or null instance" }

    override fun <T> instanceListOrNull(fieldNumber: Int, fieldName: String, decoder: WireFormat<T>): MutableList<T>? =
        array(fieldName) { decoder.decodeInstance(JsonMessage(it as JsonObject)) }

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