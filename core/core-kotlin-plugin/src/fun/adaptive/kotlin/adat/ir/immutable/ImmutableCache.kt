package `fun`.adaptive.kotlin.adat.ir.immutable

class ImmutableCache(
    short: Boolean
) {
    val entries = if (short) short() else qualified()

    fun short() = mutableMapOf(
        "0" to true,
        "Z" to true,
        "I" to true,
        "S" to true,
        "B" to true,
        "J" to true,
        "F" to true,
        "D" to true,
        "C" to true,
        "T" to true,
        "U" to true,

        "[Z" to false,
        "[I" to false,
        "[S" to false,
        "[B" to false,
        "[J" to false,
        "[F" to false,
        "[D" to false,
        "[C" to false,

        "+I" to true,
        "+S" to true,
        "+B" to true,
        "+J" to true,

        "[+I" to false,
        "[+S" to false,
        "[+B" to false,
        "[+J" to false,

        "*" to false
    )

    fun qualified() = mutableMapOf(
        "kotlin.Unit" to true,

        "kotlin.Boolean" to true,
        "kotlin.Int" to true,
        "kotlin.Short" to true,
        "kotlin.Byte" to true,
        "kotlin.Long" to true,
        "kotlin.Float" to true,

        "kotlin.Double" to true,

        "kotlin.Char" to true,

        "kotlin.BooleanArray" to false,
        "kotlin.IntArray" to false,
        "kotlin.ByteArray" to false,
        "kotlin.ShortArray" to false,
        "kotlin.LongArray" to false,
        "kotlin.FloatArray" to false,
        "kotlin.DoubleArray" to false,
        "kotlin.CharArray" to false,

        "kotlin.String" to true,

        "kotlin.UInt" to true,
        "kotlin.UShort" to true,
        "kotlin.UByte" to true,
        "kotlin.ULong" to true,

        "kotlin.UIntArray" to false,
        "kotlin.UShortArray" to false,
        "kotlin.UByteArray" to false,
        "kotlin.ULongArray" to false,

        "fun.adaptive.utility.UUID" to true,

        "kotlin.Array" to false,

        "kotlin.collections.MutableList" to false,
        "kotlin.collections.MutableMap" to false,
        "kotlin.collections.MutableSet" to false,

        "kotlinx.datetime.LocalDateTime" to true,
        "kotlinx.datetime.LocalDate" to true,
        "kotlinx.datetime.LocalTime" to true,
        "kotlin.time.Instant" to true,
        "kotlin.time.Duration" to true
    )

    operator fun get(key: String): Boolean? = entries[key]

    operator fun set(key: String, value: Boolean) = entries.put(key, value)
}