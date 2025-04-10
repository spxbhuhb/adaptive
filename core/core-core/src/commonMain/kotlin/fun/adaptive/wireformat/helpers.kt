/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat

import `fun`.adaptive.wireformat.builtin.PolymorphicWireFormat
import `fun`.adaptive.wireformat.json.JsonBufferReader
import `fun`.adaptive.wireformat.json.JsonBufferWriter
import `fun`.adaptive.wireformat.json.JsonWireFormatDecoder
import `fun`.adaptive.wireformat.json.JsonWireFormatEncoder
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatDecoder
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatEncoder

fun <T> T.toJson(wireFormat: WireFormat<T>) =
    JsonWireFormatEncoder().rawInstance(this, wireFormat).pack()

fun <T> String.fromJson(wireFormat: WireFormat<T>) =
    JsonWireFormatDecoder(this.encodeToByteArray()).asInstance(wireFormat)

fun <T> ByteArray.fromJson(wireFormat: WireFormat<T>) =
    JsonWireFormatDecoder(this).asInstance(wireFormat)

fun <T> T.toProto(wireFormat: WireFormat<T>) =
    ProtoWireFormatEncoder().rawInstance(this, wireFormat).pack()

fun <T> ByteArray.fromProto(wireFormat: WireFormat<T>) =
    ProtoWireFormatDecoder(this).asInstance(wireFormat)

/**
 * `this` JSON string as a pretty JSON string (parses the original strings and then formats it).
 */
val String.asPrettyJson
    get() = JsonBufferReader(this.encodeToByteArray()).read().asPrettyString

val Array<Any?>.asJson : String
    get() {
        val writer = JsonBufferWriter()
        val encoder = JsonWireFormatEncoder(writer)

        writer.openArray()
        for (item in this) {
            val wireFormat = PolymorphicWireFormat.wireFormatOrNullFor(item)
            if (wireFormat != null) {
                encoder.rawInstance(item, wireFormat)
            } else {
                encoder.rawString(item.toString())
            }
            writer.separator()
        }
        writer.closeArray()
        return writer.pack().decodeToString()
    }

val Array<Any?>.asPrettyJson : String
    get() = asJson.asPrettyJson