package hu.simplexion.z2.wireformat

interface TestHelpers<ST> {

    fun packForTest(byteArray: ByteArray): ByteArray

    fun single(decoder: WireFormatDecoder<ST>): ST

    fun dump(byteArray: ByteArray): String

}