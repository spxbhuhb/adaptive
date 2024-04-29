/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.protobuf

import hu.simplexion.adaptive.wireformat.TestHelpers
import hu.simplexion.adaptive.wireformat.WireFormatDecoder

interface ProtoTestHelpers : TestHelpers<ProtoRecord> {

    override fun packForTest(byteArray: ByteArray): ByteArray =
        byteArray

    override fun dump(byteArray: ByteArray): String =
        byteArray.dumpProto()

    override fun single(decoder: WireFormatDecoder<ProtoRecord>): ProtoRecord =
        (decoder as ProtoWireFormatDecoder).records.single()

}