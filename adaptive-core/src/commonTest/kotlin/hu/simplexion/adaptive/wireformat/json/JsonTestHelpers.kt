/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.json

import hu.simplexion.adaptive.wireformat.TestHelpers
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.json.elements.JsonElement

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