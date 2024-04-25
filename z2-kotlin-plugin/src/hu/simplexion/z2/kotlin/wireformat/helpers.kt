package hu.simplexion.z2.kotlin.wireformat

import hu.simplexion.z2.kotlin.common.asClassId
import hu.simplexion.z2.kotlin.common.functionByName
import org.jetbrains.kotlin.ir.util.isSubclassOf
import org.jetbrains.kotlin.name.ClassId

fun String.toWireFormatType(pluginContext: WireFormatPluginContext, signature: String): WireFormatType {
    return with(pluginContext) {
        val type = this@toWireFormatType
        val lcType = type.take(1).lowercase() + type.drop(1)
        val ucType = type.take(1).uppercase() + type.drop(1)

        WireFormatType(
            encode = wireFormatEncoder.functionByName { lcType }, // MessageBuilder.boolean
            decode = wireFormatDecoder.functionByName { lcType }, // Message.boolean

            encodeOrNull = wireFormatEncoder.functionByName { "${lcType}OrNull" }, // MessageBuilder.booleanOrNull
            decodeOrNull = wireFormatDecoder.functionByName { "${lcType}OrNull" }, // Message.booleanOrNull

            encodeList = wireFormatEncoder.functionByName { "${lcType}List" },  // MessageBuilder.booleanList
            decodeList = wireFormatDecoder.functionByName { "${lcType}List" }, // Message.booleanList

            encodeListOrNull = wireFormatEncoder.functionByName { "${lcType}ListOrNull" }, // MessageBuilder.booleanListOrNull
            decodeListOrNull = wireFormatDecoder.functionByName { "${lcType}ListOrNull" }, // Message.booleanListOrNull

            standaloneEncode = standalone.functionByName { "encode${ucType}" }, // Standalone.encodeBoolean
            standaloneDecode = standalone.functionByName { "decode${ucType}" }, // Standalone.decodeBoolean
            standaloneDecodeOrNull = standalone.functionByName { "decode${ucType}OrNull" }, // Standalone.encodeBooleanOrNull

            standaloneEncodeList = standalone.functionByName { "encode${ucType}List" }, // Standalone.encodeBooleanList
            standaloneDecodeList = standalone.functionByName { "decode${ucType}List" }, // Standalone.decodeBooleanList
            standaloneDecodeListOrNull = standalone.functionByName { "decode${ucType}ListOrNull" }, // Standalone.encodeBoolean

            signature = signature
        )
    }
}

fun String.toInstanceWireFormatType(pluginContext: WireFormatPluginContext): WireFormatType =
    this.asClassId.toInstanceWireFormatType(pluginContext)

fun ClassId.toInstanceWireFormatType(pluginContext: WireFormatPluginContext): WireFormatType {
    val classId = this

    val symbol = checkNotNull(pluginContext.irContext.referenceClass(classId)) { "missing class: ${classId.asFqNameString()}" }
    check(symbol.owner.isSubclassOf(pluginContext.wireFormatClass.owner)) { "class ${classId.asFqNameString()} does not implement ${ClassIds.WIREFORMAT}" }

    return pluginContext.wireFormatTypeTemplate.copy(wireFormat = symbol, signature = "L${classId.asFqNameString()};")
}
