package `fun`.adaptive.resource.avs

import `fun`.adaptive.wireformat.protobuf.ProtoBufferReader

/**
 * Access values in an AVS formatted byte array.
 *
 * ```kotlin
 * val values = AvsReader(binary)
 * println(values[0])
 * values.forEach { println(it) }
 * ```
 */
class AvsReader(binary: ByteArray) : Collection<ByteArray>, Iterable<ByteArray> {

    val reader = ProtoBufferReader(binary)

    val version = reader.i32().toInt()
        .also { check(it == AvsVersion.V1)  }

    override val size = reader.i32().toInt()

    operator fun get(index: Int) : ByteArray {
        require(index < size) { "Index out of bounds: $index >= $size" }
        reader.seek(8 + index * 4)
        val valueOffset = reader.i32().toInt()
        reader.seek(valueOffset)
        return reader.bytes()
    }

    override fun isEmpty(): Boolean =
        size == 0

    override fun iterator(): Iterator<ByteArray> =
        AvsIterator()

    override fun contains(element: ByteArray): Boolean  {
        throw UnsupportedOperationException()
    }

    override fun containsAll(elements: Collection<ByteArray>): Boolean {
        throw UnsupportedOperationException()
    }

    inner class AvsIterator : Iterator<ByteArray> {
        var index = 0

        override fun hasNext(): Boolean =
           index < size

        override fun next(): ByteArray =
            get(index++)
    }

    companion object {
        val EMPTY = AvsReader(AvsWriter().pack())
    }

}