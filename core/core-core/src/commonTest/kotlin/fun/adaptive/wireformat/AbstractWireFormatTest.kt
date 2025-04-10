/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat

import `fun`.adaptive.utility.UUID

@OptIn(ExperimentalUnsignedTypes::class)
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

    fun <T> polymorphicActual(value: T, wireFormat: WireFormat<T>) =
        e { polymorphic(1, fieldName, value, wireFormat) } d { polymorphic<T>(1, fieldName) }

    // ------------------------------------------------------------
    // Test values
    // ------------------------------------------------------------

    val unitVal = Unit
    val unitListVal = listOf(Unit, Unit)

    val booleanVal = true
    val booleanArrayVal = booleanArrayOf(true, false, true)
    val booleanListVal = booleanArrayVal.toList()

    val intVal = 123
    val intArrayVal = intArrayOf(Int.MIN_VALUE, Int.MIN_VALUE + 1, - 2, - 1, 0, 1, 2, Int.MAX_VALUE - 1, Int.MAX_VALUE)
    val intListVal = intArrayVal.toList()

    val shortVal: Short = 123
    val shortArrayVal = shortArrayOf(Short.MIN_VALUE, - 2, - 1, 0, 1, 2, Short.MAX_VALUE)
    val shortListVal = shortArrayVal.toList()

    val byteVal: Byte = 123
    val byteArrayVal = byteArrayOf(Byte.MIN_VALUE, - 2, - 1, 0, 1, 2, Byte.MAX_VALUE)
    val byteListVal = byteArrayVal.toList()

    val longVal = 1234L
    val longArrayVal = longArrayOf(Long.MIN_VALUE, Long.MIN_VALUE + 1, - 2, - 1, 0, 1, 2, Long.MAX_VALUE - 1, Long.MAX_VALUE)
    val longListVal = longArrayVal.toList()

    val floatVal = 12.34f
    val floatArrayVal = floatArrayOf(Float.NaN, Float.NEGATIVE_INFINITY, Float.MIN_VALUE, 1.0f, 2.0f, 3.0f, Float.MAX_VALUE, Float.POSITIVE_INFINITY)
    val floatListVal = floatArrayVal.toList()

    val doubleVal = 12.34
    val doubleArrayVal = doubleArrayOf(Double.NaN, Double.NEGATIVE_INFINITY, Double.MIN_VALUE, 1.0, 2.0, 3.0, Double.MIN_VALUE, Double.POSITIVE_INFINITY)
    val doubleListVal = doubleArrayVal.toList()

    val charVal = 'a'
    val charArrayVal = charArrayOf('a', 'b', 'c')
    val charListVal = charArrayVal.toList()

    val stringVal = "abc"
    val stringListVal = listOf("a", "b", "c")

    val uuidVal = UUID<Any>()
    val uuidListVal = listOf(UUID<Any>(), UUID(), UUID())

    val instanceVal = A(true, 12, "hello")
    val instanceListVal = listOf(
        B(A(true, 123, "a", mutableListOf(1, 2, 3)), "AA"),
        B(A(false, 456, "b", mutableListOf(4, 5, 6)), "BB"),
        B(A(true, 789, "c", mutableListOf(7, 8, 9)), "CC")
    )

    val instanceListValWithNull = listOf(
        null,
        B(A(true, 123, "a", mutableListOf(1, 2, 3)), "AA"),
        null,
        B(A(false, 456, "b", mutableListOf(4, 5, 6)), "BB"),
        null,
        B(A(true, 789, "c", mutableListOf(7, 8, 9)), "CC"),
        null
    )

    val enumVal = E.V2
    val enumListVal = listOf(E.V2, E.V1)

    val uIntVal = 123.toUInt()
    val uIntArrayVal = uintArrayOf(UInt.MIN_VALUE, (- 1).toUInt(), 0.toUInt(), 1.toUInt(), UInt.MAX_VALUE)
    val uIntListVal = uIntArrayVal.toList()

    val uShortVal = 123.toUShort()
    val uShortArrayVal = ushortArrayOf(UShort.MIN_VALUE, (- 1).toUShort(), 0.toUShort(), 1.toUShort(), UShort.MAX_VALUE)
    val uShortListVal = uShortArrayVal.toList()

    val uByteVal = 123.toUByte()
    val uByteArrayVal = ubyteArrayOf(UByte.MIN_VALUE, (- 1).toUByte(), 0.toUByte(), 1.toUByte(), UByte.MAX_VALUE)
    val uByteListVal = uByteArrayVal.toList()

    val uLongVal = 123.toULong()
    val uLongArrayVal = ulongArrayOf(ULong.MIN_VALUE, (- 1).toULong(), 0.toULong(), 1.toULong(), ULong.MAX_VALUE)
    val uLongListVal = uLongArrayVal.toList()

    val pairVal = Pair(1, "a")
    val pairListVal = listOf(Pair(1, "a"), Pair(2, "b"))
}