@file:OptIn(ExperimentalUnsignedTypes::class)

package `fun`.adaptive.wireformat.signature

import `fun`.adaptive.utility.UUID

val Any.signature: String
    get() = "*"

val Unit.signature: String
    get() = "0"

val Boolean.signature: String
    get() = "Z"

val Int.signature: String
    get() = "I"

val Byte.signature: String
    get() = "B"

val Short.signature: String
    get() = "S"

val Long.signature: String
    get() = "J"

val Float.signature: String
    get() = "F"

val Double.signature: String
    get() = "D"

val Char.signature: String
    get() = "C"

val String.signature: String
    get() = "T"

val UUID<*>.signature: String
    get() = "U"

val BooleanArray.signature: String
    get() = "[Z"

val IntArray.signature: String
    get() = "[I"

val ByteArray.signature: String
    get() = "[B"

val ShortArray.signature: String
    get() = "[S"

val LongArray.signature: String
    get() = "[J"

val FloatArray.signature: String
    get() = "[F"

val DoubleArray.signature: String
    get() = "[D"

val CharArray.signature: String
    get() = "[C"

val UInt.signature: String
    get() = "+I"

val UByte.signature: String
    get() = "+B"

val UShort.signature: String
    get() = "+S"

val ULong.signature: String
    get() = "+J"

val UIntArray.signature: String
    get() = "[+I"

val UByteArray.signature: String
    get() = "[+B"

val UShortArray.signature: String
    get() = "[+S"

val ULongArray.signature: String
    get() = "[+J"