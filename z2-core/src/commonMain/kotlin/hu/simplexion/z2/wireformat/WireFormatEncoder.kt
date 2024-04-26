package hu.simplexion.z2.wireformat

import hu.simplexion.z2.utility.UUID
import kotlin.enums.EnumEntries

/**
 * Interface for building serialized messages. Protobuf needs field number
 * JSON needs field name, hence passing both.
 */
@OptIn(ExperimentalUnsignedTypes::class)
interface WireFormatEncoder {

    fun pack(): ByteArray

    // ----------------------------------------------------------------------------
    // Special Types
    // ----------------------------------------------------------------------------

    fun any(fieldNumber: Int, fieldName: String, value: Any): WireFormatEncoder

    fun anyOrNull(fieldNumber: Int, fieldName: String, value: Any?): WireFormatEncoder

    fun rawAny(value: Any): WireFormatEncoder

    // ----

    fun unit(fieldNumber: Int, fieldName: String, value: Unit): WireFormatEncoder

    fun unitOrNull(fieldNumber: Int, fieldName: String, value: Unit?): WireFormatEncoder

    fun rawUnit(value: Unit): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // Primitives
    // ----------------------------------------------------------------------------

    fun boolean(fieldNumber: Int, fieldName: String, value: Boolean): WireFormatEncoder

    fun booleanOrNull(fieldNumber: Int, fieldName: String, value: Boolean?): WireFormatEncoder

    fun rawBoolean(value: Boolean): WireFormatEncoder

    // ----

    fun int(fieldNumber: Int, fieldName: String, value: Int): WireFormatEncoder

    fun intOrNull(fieldNumber: Int, fieldName: String, value: Int?): WireFormatEncoder

    fun rawInt(value: Int): WireFormatEncoder

    // ----

    fun short(fieldNumber: Int, fieldName: String, value: Short): WireFormatEncoder

    fun shortOrNull(fieldNumber: Int, fieldName: String, value: Short?): WireFormatEncoder

    fun rawShort(value: Short): WireFormatEncoder

    // ----

    fun byte(fieldNumber: Int, fieldName: String, value: Byte): WireFormatEncoder

    fun byteOrNull(fieldNumber: Int, fieldName: String, value: Byte?): WireFormatEncoder

    fun rawByte(value: Byte): WireFormatEncoder

    // ----

    fun long(fieldNumber: Int, fieldName: String, value: Long): WireFormatEncoder

    fun longOrNull(fieldNumber: Int, fieldName: String, value: Long?): WireFormatEncoder

    fun rawLong(value: Long): WireFormatEncoder

    // ----

    fun float(fieldNumber: Int, fieldName: String, value: Float): WireFormatEncoder

    fun floatOrNull(fieldNumber: Int, fieldName: String, value: Float?): WireFormatEncoder

    fun rawFloat(value: Float): WireFormatEncoder

    // ----

    fun double(fieldNumber: Int, fieldName: String, value: Double): WireFormatEncoder

    fun doubleOrNull(fieldNumber: Int, fieldName: String, value: Double?): WireFormatEncoder

    fun rawDouble(value: Double): WireFormatEncoder

    // ----

    fun char(fieldNumber: Int, fieldName: String, value: Char): WireFormatEncoder

    fun charOrNull(fieldNumber: Int, fieldName: String, value: Char?): WireFormatEncoder

    fun rawChar(value: Char): WireFormatEncoder

    // -----------------------------------------------------------------------------------------
    // Arrays
    // -----------------------------------------------------------------------------------------

    fun booleanArray(fieldNumber: Int, fieldName: String, values: BooleanArray): WireFormatEncoder

    fun booleanArrayOrNull(fieldNumber: Int, fieldName: String, values: BooleanArray?): WireFormatEncoder

    fun rawBooleanArray(values: BooleanArray): WireFormatEncoder

    // ----

    fun intArray(fieldNumber: Int, fieldName: String, values: IntArray): WireFormatEncoder

    fun intArrayOrNull(fieldNumber: Int, fieldName: String, values: IntArray?): WireFormatEncoder

    fun rawIntArray(values: IntArray): WireFormatEncoder

    // ----

    fun shortArray(fieldNumber: Int, fieldName: String, values: ShortArray): WireFormatEncoder

    fun shortArrayOrNull(fieldNumber: Int, fieldName: String, values: ShortArray?): WireFormatEncoder

    fun rawShortArray(values: ShortArray): WireFormatEncoder
    // ----

    fun byteArray(fieldNumber: Int, fieldName: String, value: ByteArray): WireFormatEncoder

    fun byteArrayOrNull(fieldNumber: Int, fieldName: String, value: ByteArray?): WireFormatEncoder

    fun rawByteArray(values: ByteArray): WireFormatEncoder

    // ----

    fun longArray(fieldNumber: Int, fieldName: String, values: LongArray): WireFormatEncoder

    fun longArrayOrNull(fieldNumber: Int, fieldName: String, values: LongArray?): WireFormatEncoder

    fun rawLongArray(values: LongArray): WireFormatEncoder

    // ----

    fun floatArray(fieldNumber: Int, fieldName: String, values: FloatArray): WireFormatEncoder

    fun floatArrayOrNull(fieldNumber: Int, fieldName: String, values: FloatArray?): WireFormatEncoder

    fun rawFloatArray(values: FloatArray): WireFormatEncoder

    // ----

    fun doubleArray(fieldNumber: Int, fieldName: String, values: DoubleArray): WireFormatEncoder

    fun doubleArrayOrNull(fieldNumber: Int, fieldName: String, values: DoubleArray?): WireFormatEncoder

    fun rawDoubleArray(values: DoubleArray): WireFormatEncoder

    // ----

    fun charArray(fieldNumber: Int, fieldName: String, values: CharArray): WireFormatEncoder

    fun charArrayOrNull(fieldNumber: Int, fieldName: String, values: CharArray?): WireFormatEncoder

    fun rawCharArray(values: CharArray): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // Built-in Types
    // ----------------------------------------------------------------------------

    fun string(fieldNumber: Int, fieldName: String, value: String): WireFormatEncoder

    fun stringOrNull(fieldNumber: Int, fieldName: String, value: String?): WireFormatEncoder

    fun rawString(value: String): WireFormatEncoder

    // ----

    fun <E : Enum<E>> enum(fieldNumber: Int, fieldName: String, value: E, entries: EnumEntries<E>): WireFormatEncoder

    fun <E : Enum<E>> enumOrNull(fieldNumber: Int, fieldName: String, value: E?, entries: EnumEntries<E>): WireFormatEncoder

    fun <E : Enum<E>> rawEnum(value: E, entries: EnumEntries<E>): WireFormatEncoder

    // ----

    fun uuid(fieldNumber: Int, fieldName: String, value: UUID<*>): WireFormatEncoder

    fun uuidOrNull(fieldNumber: Int, fieldName: String, value: UUID<*>?): WireFormatEncoder

    fun rawUuid(value: UUID<*>): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // Instance
    // ----------------------------------------------------------------------------

    fun <T> instance(fieldNumber: Int, fieldName: String, value: T, encoder: WireFormat<T>): WireFormatEncoder

    fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, value: T?, encoder: WireFormat<T>): WireFormatEncoder

    fun <T> rawInstance(value: T, wireFormat: WireFormat<T>): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // Collection
    // ----------------------------------------------------------------------------

    fun <T> collection(fieldNumber: Int, fieldName: String, values: Collection<T>, wireFormat: WireFormat<T>): WireFormatEncoder

    fun <T> collectionOrNull(fieldNumber: Int, fieldName: String, values: Collection<T>?, wireFormat: WireFormat<T>): WireFormatEncoder

    // -----------------------------------------------------------------------------------------
    // Unsigned Primitives
    // -----------------------------------------------------------------------------------------

    fun uInt(fieldNumber: Int, fieldName: String, value: UInt): WireFormatEncoder

    fun uIntOrNull(fieldNumber: Int, fieldName: String, value: UInt?): WireFormatEncoder

    fun rawUInt(value: UInt): WireFormatEncoder

    // ----

    fun uShort(fieldNumber: Int, fieldName: String, value: UShort): WireFormatEncoder

    fun uShortOrNull(fieldNumber: Int, fieldName: String, value: UShort?): WireFormatEncoder

    fun rawUShort(value: UShort): WireFormatEncoder

    // ----

    fun uByte(fieldNumber: Int, fieldName: String, value: UByte): WireFormatEncoder

    fun uByteOrNull(fieldNumber: Int, fieldName: String, value: UByte?): WireFormatEncoder

    fun rawUByte(value: UByte): WireFormatEncoder

    // ----

    fun uLong(fieldNumber: Int, fieldName: String, value: ULong): WireFormatEncoder

    fun uLongOrNull(fieldNumber: Int, fieldName: String, value: ULong?): WireFormatEncoder

    fun rawULong(value: ULong): WireFormatEncoder

    // -----------------------------------------------------------------------------------------
    // Unsigned Arrays
    // -----------------------------------------------------------------------------------------


    fun uIntArray(fieldNumber: Int, fieldName: String, values: UIntArray): WireFormatEncoder

    fun uIntArrayOrNull(fieldNumber: Int, fieldName: String, values: UIntArray?): WireFormatEncoder

    fun rawUIntArray(values: UIntArray): WireFormatEncoder

    // ----

    fun uShortArray(fieldNumber: Int, fieldName: String, values: UShortArray): WireFormatEncoder

    fun uShortArrayOrNull(fieldNumber: Int, fieldName: String, values: UShortArray?): WireFormatEncoder

    fun rawUShortArray(values: UShortArray): WireFormatEncoder

    // ----

    fun uByteArray(fieldNumber: Int, fieldName: String, values: UByteArray): WireFormatEncoder

    fun uByteArrayOrNull(fieldNumber: Int, fieldName: String, values: UByteArray?): WireFormatEncoder

    fun rawUByteArray(value: UByteArray): WireFormatEncoder

    // ----

    fun uLongArray(fieldNumber: Int, fieldName: String, values: ULongArray): WireFormatEncoder

    fun uLongArrayOrNull(fieldNumber: Int, fieldName: String, values: ULongArray?): WireFormatEncoder

    fun rawULongArray(values: ULongArray): WireFormatEncoder

}