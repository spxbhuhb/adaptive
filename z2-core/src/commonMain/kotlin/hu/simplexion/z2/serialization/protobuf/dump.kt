package hu.simplexion.z2.serialization.protobuf

import hu.simplexion.z2.util.toDotString

fun ProtoMessage.dumpProto(lines: MutableList<String>, indent: String = "") {
    for (record in records) {
        when (record) {
            is VarintProtoRecord -> record.dumpProto(lines, indent)
            is I32ProtoRecord -> record.dumpProto(lines, indent)
            is I64ProtoRecord -> record.dumpProto(lines, indent)
            is LenProtoRecord -> record.dumpProto(lines, indent)
        }
    }
}

fun ByteArray.dumpProto(): String {
    val lines = mutableListOf<String>()
    dumpProto(lines)
    return lines.joinToString("\n")
}

fun ByteArray.dumpProto(lines: MutableList<String>, indent: String = "") {
    ProtoMessage(this).dumpProto(lines, indent)
}

fun VarintProtoRecord.dumpProto(lines: MutableList<String>, indent: String = "") {
    lines += "${indent}VARINT  $fieldNumber  ${value.sint64()}"
}


fun I32ProtoRecord.dumpProto(lines: MutableList<String>, indent: String = "") {
    lines += "${indent}I32     $fieldNumber  ${value.int32()}"
}


fun I64ProtoRecord.dumpProto(lines: MutableList<String>, indent: String = "") {
    lines += "${indent}I64     $fieldNumber  ${value.int64()}"
}


@OptIn(ExperimentalStdlibApi::class)
fun LenProtoRecord.dumpProto(lines: MutableList<String>, indent: String = "") {
    val recordBytes = byteArray.copyOfRange(offset, offset + length)

    lines += "${indent}LEN     $fieldNumber  $length  ${recordBytes.toDotString(limit = 100)}  ${recordBytes.toHexString()}"

    try {
        val localLines = mutableListOf<String>()
        recordBytes.dumpProto(localLines, "$indent  ")
        lines += localLines
    } catch (ex: Exception) {
        // nothing to do here, probably not a valid record
    }
}