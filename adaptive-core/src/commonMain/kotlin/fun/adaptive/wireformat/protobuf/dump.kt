/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.protobuf

import `fun`.adaptive.utility.toDotString

fun ProtoWireFormatDecoder.dumpProto(lines: MutableList<String> = mutableListOf(), indent: String = "") : MutableList<String> {
    for (record in records) {
        when (record) {
            is VarintProtoRecord -> record.dumpProto(lines, indent)
            is I32ProtoRecord -> record.dumpProto(lines, indent)
            is I64ProtoRecord -> record.dumpProto(lines, indent)
            is LenProtoRecord -> record.dumpProto(lines, indent)
        }
    }
    return lines
}

fun ByteArray.dumpProto(): String {
    val lines = mutableListOf<String>()
    dumpProto(lines)
    return lines.joinToString("\n")
}

fun ByteArray.dumpProto(lines: MutableList<String>, indent: String = "") {
    ProtoWireFormatDecoder(this).dumpProto(lines, indent)
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