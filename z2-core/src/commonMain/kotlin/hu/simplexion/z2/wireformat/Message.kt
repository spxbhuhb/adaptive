package hu.simplexion.z2.wireformat

import hu.simplexion.z2.utility.UUID

interface Message {

    // -----------------------------------------------------------------------------------------
    // Boolean
    // -----------------------------------------------------------------------------------------

    fun boolean(fieldNumber: Int, fieldName: String): Boolean

    fun booleanOrNull(fieldNumber: Int, fieldName: String): Boolean?

    fun booleanList(fieldNumber: Int, fieldName: String): List<Boolean>

    fun booleanListOrNull(fieldNumber: Int, fieldName: String): List<Boolean>?

    // -----------------------------------------------------------------------------------------
    // Int
    // -----------------------------------------------------------------------------------------

    fun int(fieldNumber: Int, fieldName: String): Int

    fun intOrNull(fieldNumber: Int, fieldName: String): Int?

    fun intList(fieldNumber: Int, fieldName: String): List<Int>

    fun intListOrNull(fieldNumber: Int, fieldName: String): List<Int>?

    // -----------------------------------------------------------------------------------------
    // Long
    // -----------------------------------------------------------------------------------------

    fun long(fieldNumber: Int, fieldName: String): Long

    fun longOrNull(fieldNumber: Int, fieldName: String): Long?

    fun longList(fieldNumber: Int, fieldName: String): List<Long>

    fun longListOrNull(fieldNumber: Int, fieldName: String): List<Long>?

    // -----------------------------------------------------------------------------------------
    // Float
    // -----------------------------------------------------------------------------------------

    fun float(fieldNumber: Int, fieldName: String): Float

    fun floatOrNull(fieldNumber: Int, fieldName: String): Float?

    fun floatList(fieldNumber: Int, fieldName: String): List<Float>

    fun floatListOrNull(fieldNumber: Int, fieldName: String): List<Float>?

    // -----------------------------------------------------------------------------------------
    // Double
    // -----------------------------------------------------------------------------------------

    fun double(fieldNumber: Int, fieldName: String): Double

    fun doubleOrNull(fieldNumber: Int, fieldName: String): Double?

    fun doubleList(fieldNumber: Int, fieldName: String): List<Double>

    fun doubleListOrNull(fieldNumber: Int, fieldName: String): List<Double>?
    
    // -----------------------------------------------------------------------------------------
    // String
    // -----------------------------------------------------------------------------------------

    fun string(fieldNumber: Int, fieldName: String): String

    fun stringOrNull(fieldNumber: Int, fieldName: String): String?

    fun stringList(fieldNumber: Int, fieldName: String): List<String>

    fun stringListOrNull(fieldNumber: Int, fieldName: String): List<String>?

    // -----------------------------------------------------------------------------------------
    // ByteArray
    // -----------------------------------------------------------------------------------------

    fun byteArray(fieldNumber: Int, fieldName: String): ByteArray

    fun byteArrayOrNull(fieldNumber: Int, fieldName: String): ByteArray?

    fun byteArrayList(fieldNumber: Int, fieldName: String): List<ByteArray>

    fun byteArrayListOrNull(fieldNumber: Int, fieldName: String): List<ByteArray>?

    // -----------------------------------------------------------------------------------------
    // UUID
    // -----------------------------------------------------------------------------------------

    fun <T> uuid(fieldNumber: Int, fieldName: String): UUID<T>

    fun <T> uuidOrNull(fieldNumber: Int, fieldName: String): UUID<T>?

    fun <T> uuidList(fieldNumber: Int, fieldName: String): List<UUID<T>>

    fun <T> uuidListOrNull(fieldNumber: Int, fieldName: String): List<UUID<T>>?

    // -----------------------------------------------------------------------------------------
    // Instance
    // -----------------------------------------------------------------------------------------

    fun <T> instance(fieldNumber: Int, fieldName: String, decoder: WireFormat<T>): T

    fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, decoder: WireFormat<T>): T?

    fun <T> instanceList(fieldNumber: Int, fieldName: String, decoder: WireFormat<T>): MutableList<T>

    fun <T> instanceListOrNull(fieldNumber: Int, fieldName: String, decoder: WireFormat<T>): List<T>?

}