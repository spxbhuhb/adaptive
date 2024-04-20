package hu.simplexion.z2.wireformat

import hu.simplexion.z2.wireformat.protobuf.ProtoWireFormatProvider

abstract class WireFormatProvider {

    abstract fun messageBuilder(): MessageBuilder
    abstract fun standalone(): Standalone<Message>
    abstract fun toMessage(payload: ByteArray): Message

    companion object {
        var defaultWireFormatProvider: WireFormatProvider = ProtoWireFormatProvider()
    }
}
