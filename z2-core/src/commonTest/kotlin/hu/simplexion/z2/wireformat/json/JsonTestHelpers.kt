package hu.simplexion.z2.wireformat.json

import hu.simplexion.z2.wireformat.TestHelpers
import hu.simplexion.z2.wireformat.WireFormatDecoder
import hu.simplexion.z2.wireformat.json.elements.JsonElement

interface JsonTestHelpers : TestHelpers<JsonElement> {

    val op
        get() = "{".encodeToByteArray()
    val cp
        get() = "}".encodeToByteArray()

    override fun packForTest(byteArray: ByteArray): ByteArray =
        op + byteArray + cp

    override fun dump(byteArray: ByteArray): String =
        byteArray.decodeToString()

    override fun single(decoder: WireFormatDecoder<JsonElement>): JsonElement =
        (decoder as JsonWireFormatDecoder).root
}