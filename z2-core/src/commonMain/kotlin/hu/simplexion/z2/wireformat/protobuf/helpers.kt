package hu.simplexion.z2.wireformat.protobuf

import kotlin.enums.EnumEntries

fun ULong.bool() = (this != 0UL)

fun ULong.int64(): Long = this.toLong()

fun ULong.int32(): Int = this.toInt()

fun ULong.sint32(): Int = (this shr 1).toInt() xor - (this and 1UL).toInt()

fun ULong.sint64(): Long = (this shr 1).toLong() xor - (this and 1UL).toLong()

fun ULong.float(): Float = Float.fromBits(this.toUInt().toInt())

fun ULong.double(): Double = Double.fromBits(this.toLong())

fun <E : Enum<E>> enumOrNullToOrdinal(value: E?): Int? = value?.ordinal

fun <E : Enum<E>> ordinalToEnum(entries: EnumEntries<E>, ordinal: Int) = entries[ordinal]

fun <E : Enum<E>> ordinalOrNullToEnum(entries: EnumEntries<E>, ordinal: Int?) = ordinal?.let { entries[it] }

fun <E : Enum<E>> ordinalListToEnum(entries: EnumEntries<E>, ordinals: List<Int>): List<Enum<E>> {
    return ordinals.map { entries[it] }
}

fun <E : Enum<E>> ordinalListOrNullToEnum(entries: EnumEntries<E>, ordinals: List<Int>?): List<Enum<E>>? {
    if (ordinals == null) return null
    return ordinals.map { entries[it] }
}

fun enumListToOrdinals(values: List<Enum<*>>): List<Int> {
    return values.map { it.ordinal }
}

fun enumListOrNullToOrdinals(values: List<Enum<*>>?): List<Int>? {
    return values?.map { it.ordinal }
}