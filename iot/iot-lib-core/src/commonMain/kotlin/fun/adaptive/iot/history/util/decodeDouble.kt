package `fun`.adaptive.iot.history.util

import `fun`.adaptive.iot.history.model.AioDoubleHistoryRecord
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatDecoder
import kotlinx.datetime.Instant


fun List<ByteArray>.decodeDoubleChunks(
    out: MutableList<AioDoubleHistoryRecord>,
    start: Instant,
    end: Instant
) {
    forEach {
        it.decodeDoubleChunk(out, start, end)
    }
}

fun ByteArray.decodeDoubleChunk(
    out: MutableList<AioDoubleHistoryRecord>,
    start: Instant,
    end: Instant
) {
    val decoder = ProtoWireFormatDecoder(this)



    for (record in decoder.records) {
        val record = record.decoder().rawInstance(record, AioDoubleHistoryRecord)
        if (record.timestamp >= start && record.timestamp < end) {
            out += record
        }
    }
}