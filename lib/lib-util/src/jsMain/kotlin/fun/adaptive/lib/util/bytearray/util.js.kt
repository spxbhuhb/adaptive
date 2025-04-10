package `fun`.adaptive.lib.util.bytearray

import org.khronos.webgl.Int8Array
import org.khronos.webgl.Uint8Array

fun Uint8Array.toKotlinArray(): ByteArray =
    Int8Array(buffer, byteOffset, byteLength).unsafeCast<ByteArray>()

@Suppress("NOTHING_TO_INLINE")
inline fun ByteArray.toPlatformArray(): Uint8Array {
    val i8a = unsafeCast<Int8Array>()
    return Uint8Array(i8a.buffer, i8a.byteOffset, i8a.byteLength)
}