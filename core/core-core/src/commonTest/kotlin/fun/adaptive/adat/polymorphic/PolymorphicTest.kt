package `fun`.adaptive.adat.polymorphic

import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.toJson
import `fun`.adaptive.wireformat.toProto
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.time.Clock.System.now
import kotlin.time.Duration.Companion.seconds

class PolymorphicTest {

    @Test
    fun nullValue() = test { null }

    @Test
    fun byte() = test { 12.toByte() }

    @Test
    fun short() = test { 12.toShort() }

    @Test
    fun int() = test { 12 }

    @Test
    fun long() = test { 12L }

    @Test
    fun float() = test { 12.0f }

    @Test
    fun double() = test { 12.0 }

    @Test
    fun boolean() = test { true }

    @Test
    fun char() = test { 'a' }

    @Test
    fun string() = test { "abc" }

    @Test
    fun uByte() = test { 12.toUByte() }

    @Test
    fun uShort() = test { 12.toUShort() }

    @Test
    fun uInt() = test { 12.toUInt() }

    @Test
    fun uLong() = test { 12.toULong() }

    @Test
    fun list() = test { listOf(1, 2, 3) }

    @Test
    fun mutableList() = test { mutableListOf(1, 2, 3) }

    @Test
    fun instance() = test { PolymorphicTestClass(12) }

    @Test
    fun instanceList() = test { listOf(PolymorphicTestClass(12), PolymorphicTestClass(23)) }

    @Test
    fun polymorphicList() = test { listOf(PolymorphicTestClass(12), 23) }

    @Test
    fun instanceSet() = test { setOf(PolymorphicTestClass(12), PolymorphicTestClass(23)) }

    @Test
    fun polymorphicSet() = test { setOf(PolymorphicTestClass(12), 23) }

    @Test
    fun duration() = test { 1.seconds }

    @Test
    fun instant() = test { now() }

    @Test
    fun localDateTime() = test { LocalDateTime(2023, 7, 27, 15, 35, 5, 11) }

    @Test
    fun localDate() = test { LocalDate(2023, 7, 27) }

    @Test
    fun localTime() = test { LocalTime(15, 35) }

    @Test
    fun uuid() = test { UUID<Any>() }

    @Test
    fun byteArray() {
        // FIXME AdatClass equals should work for arrays as well
        val value = byteArrayOf(1, 2, 3)
        val t1 = PolymorphicTestClass(value)
        assertContentEquals(value, PolymorphicTestClass.fromJson(t1.toJson(PolymorphicTestClass)).something as ByteArray)
        assertContentEquals(value, PolymorphicTestClass.fromProto(t1.toProto(PolymorphicTestClass)).something as ByteArray)
    }

    fun test(value: () -> Any?) {
        val t1 = PolymorphicTestClass(value())
        assertEquals(t1, PolymorphicTestClass.fromJson(t1.toJson(PolymorphicTestClass)))
        assertEquals(t1, PolymorphicTestClass.fromProto(t1.toProto(PolymorphicTestClass)))
    }


}