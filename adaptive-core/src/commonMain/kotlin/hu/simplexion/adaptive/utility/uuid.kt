/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.utility

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
 * If you want to use it "freely" choose one of the following options:
 *
 * 1. use `External` type for UUIDs that represent external entities
 * 2. use `Interim` type for UUDSs that exists for a short time
 * 3. create an object that represents the entity type you want to use
 */
class UUID<T> : Comparable<UUID<T>> {

    companion object {
        val mask = 0xffffffff.toULong()
        val NIL = UUID<Any>(IntArray(4) { 0 }, 0)

        @Suppress("UNCHECKED_CAST")
        fun <T> nil() = NIL as UUID<T>

        fun <T> String.toUuidOrNull(): UUID<T>? {
            return try {
                UUID(this)
            } catch (ex: NumberFormatException) {
                null
            }
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
        val array = fourRandomInt()

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

    fun toShort(): String {
        val chars = CharArray(6)
        digits(msbm shr 8, chars, 0, 6)
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