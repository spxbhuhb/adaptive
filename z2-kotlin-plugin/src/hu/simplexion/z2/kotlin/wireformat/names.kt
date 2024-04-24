package hu.simplexion.z2.kotlin.wireformat


import hu.simplexion.z2.kotlin.common.NamesBase
import org.jetbrains.kotlin.name.FqName

object Strings {
    const val WIREFORMAT_PACKAGE = "hu.simplexion.z2.wireformat"
    const val BUILTIN_PACKAGE = "hu.simplexion.z2.wireformat.builtin"

    const val PACK = "pack"

    const val WIREFORMAT = "WireFormat"

    const val STANDALONE = "Standalone"
    const val WIREFORMAT_ENCODER = "WireFormatEncoder"
    const val WIREFORMAT_DECODER = "WireFormatDecoder"

    const val WIREFORMAT_PROVIDER = "WireFormatProvider"
    const val DEFAULT_WIREFORMAT_PROVIDER = "defaultWireFormatProvider"

    const val ENCODE_INSTANCE = "encodeInstance"
    const val DECODE_INSTANCE = "decodeInstance"

    const val ENCODE_INSTANCE_OR_NULL = "encodeInstanceOrNull"
    const val DECODE_INSTANCE_OR_NULL = "decodeInstanceOrNull"

    const val ENCODE_INSTANCE_LIST = "encodeInstanceList"
    const val DECODE_INSTANCE_LIST = "encodeInstanceList"

    const val ENCODE_INSTANCE_LIST_OR_NULL = "encodeInstanceListOrNull"
    const val DECODE_INSTANCE_LIST_OR_NULL = "encodeInstanceListOrNull"
}

object FqNames {
    val WIREFORMAT_PACKAGE = FqName(Strings.WIREFORMAT_PACKAGE)
}

object ClassIds : NamesBase(Strings.WIREFORMAT_PACKAGE) {
    val WIREFORMAT = Strings.WIREFORMAT.classId()

    val STANDALONE = Strings.STANDALONE.classId()
    val WIREFORMAT_ENCODER = Strings.WIREFORMAT_ENCODER.classId()
    val WIREFORMAT_DECODER = Strings.WIREFORMAT_DECODER.classId()

    val WIREFORMAT_PROVIDER = Strings.WIREFORMAT_PROVIDER.classId()
}

object CallableIds : NamesBase(Strings.WIREFORMAT_PACKAGE) {
    val DEFAULT_WIREFORMAT_PROVIDER = Strings.DEFAULT_WIREFORMAT_PROVIDER.callableId {
        (Strings.WIREFORMAT_PACKAGE + "." + Strings.WIREFORMAT_PROVIDER + ".Companion").fqName()
    }
}
