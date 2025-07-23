/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.utility

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName
import kotlin.time.Clock.System.now
import kotlin.time.Instant

const val versionMask = 0xffff0fff.toInt()
const val version = 0x00004000
const val variantMask = 0x3fffffff
const val variant = 0x80000000.toInt()

val hexChars = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

fun <T> ByteArray.toUuid(offset: Int = 0): UUID<T> =
    UUID(toLong(offset), toLong(offset + 8))

/**
 * Strongly typed UUID class. Strong typing of this class was a decision to improve
 * code quality. While it might be annoying to specify the types in all scenarios,
 * enforcing the proper types leads to well-defined and deterministic code.
 *
 *
 * ```kotlin
 * val uuid1 = uuid4<SomeType>()
 * val uuid2 = uuid7<SomeType>()
 * val uuid3 = monotonicUuid7(uuid2)
 * ```
 *
 * `monotonicUuid7` guarantees that the timestamp in the created UUID is larger than the timestamp in the passed uuid.
 *
 *  `cast` can be used to easily cast a uuid to another type:
 *
 * ```kotlin
 * val uuid1 = uuid4<SomeType>()
 * val uuid2 : UUID<Any> = uuid1.cast()
 * ```
 */
class UUID<T> : Comparable<UUID<T>> {

    companion object {
        val mask = 0xffffffff.toULong()
        val NIL = UUID<Any>(IntArray(4) { 0 }, 0)

        @OptIn(ExperimentalObjCName::class)
        @Suppress("UNCHECKED_CAST")
        @ObjCName("getNil")
        fun <T> nil() = NIL as UUID<T>

        fun <T> String.toUuidOrNull(): UUID<T>? {
            return try {
                UUID(this)
            } catch (ex: NumberFormatException) {
                null
            }
        }

        fun <T> uuid4() = UUID<T>()

        /**
         * Create a Version 7 UUID with the current time and random bytes from a
         * secure random number generator. See [secureRandom].
         */
        fun <T> uuid7(): UUID<T> =
            uuid7(now(), secureRandom(3))

        /**
         * Create a Version 7 UUID with the given timestamp and random part.
         */
        fun <T> uuid7(timestamp: Instant, random: IntArray): UUID<T> {
            val unixTimestamp = timestamp.epochSeconds * 1_000 + timestamp.nanosecondsOfSecond / 1_000_000

            val ms = (random[0].toLong() shl 32) or random[1].toLong()
            val ls = random[2].toLong()

            // High 64 bits: Timestamp (48 bits) + Version (4 bits) + Random (12 bits)
            val msb = (unixTimestamp and 0xFFFFFFFFFFFFL shl 16) or
                (0x7L shl 12) or // Version 7
                (ms and 0xFFF)

            // Low 64 bits: Variant (2 bits) + 62-bit Random
            val lsb = (0x2L shl 62) or (ls and 0x3FFFFFFFFFFFFFFFL)

            return UUID(msb, lsb)
        }

        /**
         * Create a new Version 7 UUID that is guaranteed to be larger than [lastId].
         * If the timestamp in [lastId] is larger than the current time, the returning
         * UUID will contain a timestamp one millisecond larger than in [lastId].
         *
         * The random part is fully random in all cases.
         */
        fun <T> monotonicUuid7(lastId: UUID<T>?): UUID<T> {

            val newUuid = uuid7<T>(now(), secureRandom(3))

            if (lastId == null || newUuid > lastId) {
                return newUuid
            }

            val lastTimestamp = lastId.msb ushr 16

            val adjustedMsb = ((lastTimestamp + 1) shl 16) or (newUuid.msb and 0xFFFF)

            return UUID<T>(adjustedMsb, newUuid.lsb)
        }
    }

    val msbm: Int
    val msbl: Int
    val lsbm: Int
    val lsbl: Int

    val isNil
        get() = (msbm == 0 && msbl == 0 && lsbm == 0 && lsbl == 0)

    val msb: Long
        get() = (((msbm.toULong()) shl 32) or (msbl.toULong() and mask)).toLong()

    val lsb: Long
        get() = (((lsbm.toULong()) shl 32) or (lsbl.toULong() and mask)).toLong()

    constructor() {
        val array = secureRandom(4)

        msbm = array[0]
        msbl = (array[1] and versionMask) or version
        lsbm = (array[2] and variantMask) or variant
        lsbl = array[3]
    }

    constructor(msbm: Int, msbl: Int, lsbm: Int, lsbl: Int) {
        this.msbm = msbm
        this.msbl = msbl
        this.lsbm = lsbm
        this.lsbl = lsbl
    }

    constructor(array: IntArray, position: Int) {
        var idx = position

        msbm = array[idx ++]
        msbl = array[idx ++]
        lsbm = array[idx ++]
        lsbl = array[idx]
    }

    constructor(msb: Long, lsb: Long) {
        msbm = (msb.toULong() shr 32).toInt()
        msbl = (msb.toULong() and mask).toInt()
        lsbm = (lsb.toULong() shr 32).toInt()
        lsbl = (lsb.toULong() and mask).toInt()
    }

    constructor(uuid: String) {
        val s = uuid.replace("-", "")

        msbm = s.substring(0, 8).toLong(16).toInt()
        msbl = s.substring(8, 16).toLong(16).toInt()
        lsbm = s.substring(16, 24).toLong(16).toInt()
        lsbl = s.substring(24, 32).toLong(16).toInt()
    }

    override fun equals(other: Any?): Boolean {
        if (other is UUID<*>) {
            if (other.msbm != this.msbm) return false
            if (other.msbl != this.msbl) return false
            if (other.lsbm != this.lsbm) return false
            return other.lsbl == this.lsbl
        } else {
            return false
        }
    }

    override fun hashCode() = msbm

    override fun toString(): String {
        val chars = CharArray(36)

        digits(msbm, chars, 0, 8)
        chars[8] = '-'
        digits(msbl shr 16, chars, 9, 4)
        chars[13] = '-'
        digits(msbl, chars, 14, 4)
        chars[18] = '-'
        digits(lsbm shr 16, chars, 19, 4)
        chars[23] = '-'
        digits(lsbm, chars, 24, 4)
        digits(lsbl, chars, 28, 8)

        return chars.concatToString()
    }

    /**
     * The first 6 characters of the UUID as a string.
     */
    fun toShort(): String {
        val chars = CharArray(6)
        digits(msbm shr 8, chars, 0, 6)
        return chars.concatToString()
    }

    /**
     * The last 6 characters of the UUID as a string.
     */
    fun toShortEnd(): String {
        val chars = CharArray(6)
        digits(lsbl and 0xffffff, chars, 0, 6)
        return chars.concatToString()
    }

    fun toByteArray(): ByteArray =
        ByteArray(16).also {
            msb.encodeInto(it, 0)
            lsb.encodeInto(it, 8)
        }

    private fun digits(value: Int, chars: CharArray, position: Int, digits: Int) {
        var i = position + digits
        var v = value

        while (i > position) {
            chars[-- i] = hexChars[v and 0xf]
            v = v shr 4
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> cast() = this as UUID<T>

    override fun compareTo(other: UUID<T>): Int {
        var r = msbm.compareTo(other.msbm)
        if (r != 0) return r

        r = msbl.compareTo(other.msbl)
        if (r != 0) return r

        r = lsbm.compareTo(other.lsbm)
        if (r != 0) return r

        return lsbl.compareTo(other.lsbl)
    }
}