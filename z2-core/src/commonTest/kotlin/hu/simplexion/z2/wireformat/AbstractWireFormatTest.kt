package hu.simplexion.z2.wireformat

import hu.simplexion.z2.utility.UUID

abstract class AbstractWireFormatTest<ST>(
    private val wireFormatProvider: WireFormatProvider
) : TestHelpers<ST> {

    // ------------------------------------------------------------
    // Helper functions for tests
    // ------------------------------------------------------------

    val fieldName = "val"

    fun e(f: WireFormatEncoder.() -> Unit): ByteArray =
        packForTest(r(f))

    fun r(f: WireFormatEncoder.() -> Unit): ByteArray =
        wireFormatProvider.encoder()
            .apply { f() }
            .pack()

    @Suppress("UNCHECKED_CAST")
    infix fun <VT> ByteArray.d(f: WireFormatDecoder<ST>.(source: ST) -> VT): VT =
        (wireFormatProvider.decoder(this) as WireFormatDecoder<ST>).let { f.invoke(it, single(it)) }

    infix fun ByteArray.p(doPrint: Boolean): ByteArray =
        apply { if (doPrint) println(dump(this)) }

    fun <T> actual(value: T, wireFormat: WireFormat<T>) =
        e { instance(1, fieldName, value, wireFormat) } d { instance(1, fieldName, wireFormat) }

    // ------------------------------------------------------------
    // Test values
    // ------------------------------------------------------------

    val unitVal = Unit
    val unitListVal = listOf(Unit, Unit)

    val booleanVal = true
    val booleanArrayVal = booleanArrayOf(true, false, true)
    val booleanListVal = listOf(true, false, true)

    val intVal = 123
    val intListVal = listOf(Int.MIN_VALUE, Int.MIN_VALUE + 1, - 2, - 1, 0, 1, 2, Int.MAX_VALUE - 1, Int.MAX_VALUE)

    val shortVal: Short = 123
    val shortListVal = listOf(Short.MIN_VALUE, - 2, - 1, 0, 1, 2, Short.MAX_VALUE)

    val byteVal: Byte = 123
    val byteListVal = listOf(Byte.MIN_VALUE, - 2, - 1, 0, 1, 2, Byte.MAX_VALUE)

    val longVal = 1234L
    val longListVal = listOf(Long.MIN_VALUE, Long.MIN_VALUE + 1, - 2, - 1, 0, 1, 2, Long.MAX_VALUE - 1, Long.MAX_VALUE)

    val floatVal = 12.34f
    val floatListVal = listOf(Float.NaN, Float.NEGATIVE_INFINITY, Float.MIN_VALUE, 1.0f, 2.0f, 3.0f, Float.MAX_VALUE, Float.POSITIVE_INFINITY)

    val doubleVal = 12.34
    val doubleListVal = listOf(Double.NaN, Double.NEGATIVE_INFINITY, Double.MIN_VALUE, 1.0, 2.0, 3.0, Double.MIN_VALUE, Double.POSITIVE_INFINITY)

    val charVal = 'a'
    val charListVal = listOf('a', 'b', 'c')

    val stringVal = "abc"
    val stringListVal = listOf("a", "b", "c")

    val byteArrayVal = byteArrayOf(9, 8, 7)
    val byteArrayListVal = listOf(byteArrayOf(1), byteArrayOf(2), byteArrayOf(3))

    val uuidVal = UUID<Any>()
    val uuidListVal = listOf(UUID<Any>(), UUID(), UUID())

    val instanceVal = A(true, 12, "hello")
    val instanceListVal = listOf(
        B(A(true, 123, "a", mutableListOf(1, 2, 3)), "AA"),
        B(A(false, 456, "b", mutableListOf(4, 5, 6)), "BB"),
        B(A(true, 789, "c", mutableListOf(7, 8, 9)), "CC")
    )

    val enumVal = E.V1
    val enumListVal = listOf(E.V2, E.V1)

    val uIntVal = 123.toUInt()
    val uIntListVal = listOf(UInt.MIN_VALUE, (- 1).toUInt(), 0.toUInt(), 1.toUInt(), UInt.MAX_VALUE)

    val uShortVal = 123.toUShort()
    val uShortListVal = listOf(UShort.MIN_VALUE, (- 1).toUShort(), 0.toUShort(), 1.toUShort(), UShort.MAX_VALUE)

    val uByteVal = 123.toUByte()
    val uByteListVal = listOf(UByte.MIN_VALUE, (- 1).toUByte(), 0.toUByte(), 1.toUByte(), UByte.MAX_VALUE)

    val uLongVal = 123.toULong()
    val uLongListVal = listOf(ULong.MIN_VALUE, (- 1).toULong(), 0.toULong(), 1.toULong(), ULong.MAX_VALUE)
}