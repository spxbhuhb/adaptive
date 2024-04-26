package hu.simplexion.z2.wireformat.protobuf

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.utility.toUuid

interface ProtoRecord {

    val fieldNumber: Int
    val type: Int
    val value: ULong

    fun string(): String {
        check(this is LenProtoRecord) { "not a LEN record, fieldNumber=$fieldNumber" }
        return byteArray.decodeToString(offset, offset + length)
    }

    fun <T> uuid(): UUID<T> {
        check(this is LenProtoRecord)
        return byteArray.toUuid(offset)
    }

    fun bytes(): ByteArray {
        check(this is LenProtoRecord)
        return byteArray.copyOfRange(offset, offset + length)
    }

}

class VarintProtoRecord(
    override val fieldNumber: Int,
    override val value: ULong
) : ProtoRecord {

    override val type: Int
        get() = VARINT

}

class I64ProtoRecord(
    override val fieldNumber: Int,
    override val value: ULong
) : ProtoRecord {
    override val type: Int
        get() = I64
}

class LenProtoRecord(
    override val fieldNumber: Int,
    val byteArray: ByteArray,
    val offset: Int,
    val length: Int
) : ProtoRecord {
    override val type: Int
        get() = LEN

    override val value: ULong
        get() = throw IllegalStateException("long value is not available for LEN record")

    fun decoder() = ProtoWireFormatDecoder(byteArray, offset, length)
}

class I32ProtoRecord(
    override val fieldNumber: Int,
    override val value: ULong
) : ProtoRecord {
    override val type: Int
        get() = I32
}