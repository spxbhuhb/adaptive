package `fun`.adaptive.resource.avs

import `fun`.adaptive.wireformat.protobuf.ProtoBufferWriter

class AvsWriter {

    val values = ProtoBufferWriter()
    val offsets = mutableListOf<Int>()

    operator fun plusAssign(value: ByteArray) {
        offsets += values.size
        values.bytes(value)
    }

    fun pack(): ByteArray {
        val headerSize = 8
        val offsetTableSize = offsets.size * 4
        val shift = headerSize + offsetTableSize

        val metadata = ProtoBufferWriter(headerSize + offsetTableSize)
        metadata.fixed32(AvsVersion.V1.toUInt())
        metadata.fixed32(offsets.size.toUInt())

        offsets
            .map { it + shift }
            .forEach { metadata.fixed32(it.toUInt()) }

        return metadata.pack() + values.pack()
    }

}