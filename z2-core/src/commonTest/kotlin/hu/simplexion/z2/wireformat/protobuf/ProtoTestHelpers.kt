package hu.simplexion.z2.wireformat.protobuf

import hu.simplexion.z2.wireformat.TestHelpers
import hu.simplexion.z2.wireformat.WireFormatDecoder

interface ProtoTestHelpers : TestHelpers<ProtoRecord> {

    override fun packForTest(byteArray: ByteArray): ByteArray =
        byteArray

    override fun dump(byteArray: ByteArray): String =
        byteArray.dumpProto()

    override fun single(decoder: WireFormatDecoder<ProtoRecord>): ProtoRecord =
        (decoder as ProtoWireFormatDecoder).records.single()

}