/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.json

import `fun`.adaptive.wireformat.TestHelpers
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.json.elements.JsonElement

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