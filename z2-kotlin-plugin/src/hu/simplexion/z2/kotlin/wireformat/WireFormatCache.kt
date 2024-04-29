package hu.simplexion.z2.kotlin.wireformat

import hu.simplexion.z2.kotlin.common.AbstractIrBuilder
import hu.simplexion.z2.kotlin.common.AbstractPluginContext
import hu.simplexion.z2.kotlin.common.companionClassId
import org.jetbrains.kotlin.ir.backend.js.utils.asString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.name.FqName

class WireFormatCache(
    parentPluginContext: AbstractPluginContext
) : AbstractIrBuilder {

    override val pluginContext = WireFormatPluginContext(parentPluginContext.irContext, parentPluginContext.options)

    val pack
        get() = pluginContext.pack

    val wireFormatEncoder
        get() = pluginContext.wireFormatEncoder

    val types = mutableMapOf(
//        "kotlin.Boolean" to "Boolean".toWireFormatType(pluginContext, "Z"),
        "kotlin.Int" to "Int".toWireFormatType(pluginContext, "I"),
//        "kotlin.Short" to "Short".toWireFormatType(pluginContext, "S"),
//        "kotlin.Byte" to "Byte".toWireFormatType(pluginContext, "B"),
//        "kotlin.Long" to "Long".toWireFormatType(pluginContext, "J"),
//        "kotlin.Float" to "Float".toWireFormatType(pluginContext, "F"),
//        "kotlin.Double" to "Double".toWireFormatType(pluginContext, "D"),
//        "kotlin.Char" to "Int".toWireFormatType(pluginContext, "C"),
//
//        "kotlin.ByteArray" to "ByteArray".toWireFormatType(pluginContext, "[Z"),
//        "kotlin.IntArray" to "IntArray".toWireFormatType(pluginContext, "[I"),
//        "kotlin.ShortArray" to "ShortArray".toWireFormatType(pluginContext, "[S"),
//        "kotlin.ByteArray" to "ByteArray".toWireFormatType(pluginContext, "[B"),
//        "kotlin.LongArray" to "LongArray".toWireFormatType(pluginContext, "[J"),
//        "kotlin.FloatArray" to "FloatArray".toWireFormatType(pluginContext, "[F"),
//        "kotlin.DoubleArray" to "DoubleArray".toWireFormatType(pluginContext, "[D"),
//        "kotlin.CharArray" to "CharArray".toWireFormatType(pluginContext, "[C"),

        "kotlin.String" to "String".toWireFormatType(pluginContext, "S"),
//        "hu.simplexion.z2.utility.UUID" to "Uuid".toWireFormatType(pluginContext, "U"),
//
//        "kotlin.UInt" to "Int".toWireFormatType(pluginContext, "+I"),
//        "kotlin.UShort" to "Int".toWireFormatType(pluginContext, "+S"),
//        "kotlin.UByte" to "Int".toWireFormatType(pluginContext, "+B"),
//        "kotlin.ULong" to "Long".toWireFormatType(pluginContext, "+J"),
//
//        "kotlin.UIntArray" to "UIntArray".toWireFormatType(pluginContext, "[+I"),
//        "kotlin.UShortArray" to "UShortArray".toWireFormatType(pluginContext, "[+S"),
//        "kotlin.UByteArray" to "UByteArray".toWireFormatType(pluginContext, "[+B"),
//        "kotlin.ULongArray" to "ULongArray".toWireFormatType(pluginContext, "[+J"),

        "kotlin.time.Duration" to Strings.DURATION_WIREFORMAT.toInstanceWireFormatType(pluginContext),
        "kotlinx.datetime.Instant" to Strings.INSTANT_WIREFORMAT.toInstanceWireFormatType(pluginContext),
        "kotlinx.datetime.LocalDateTime" to Strings.LOCALDATETIME_WIREFORMAT.toInstanceWireFormatType(pluginContext),
        "kotlinx.datetime.LocalDate" to Strings.LOCALDATE_WIREFORMAT.toInstanceWireFormatType(pluginContext),
        "kotlinx.datetime.LocalTime" to Strings.LOCALTIME_WIREFORMAT.toInstanceWireFormatType(pluginContext)
    )

    //    fun primitive(type: IrType): WireFormatType? {
//        val fqName = (type.classifierOrNull as? IrClassSymbol)?.owner?.fqNameWhenAvailable ?: return null
//        return protoPrimitives[fqName.asString()]
//    }
//
//    fun list(type: IrType): WireFormatType? {
//        if (! type.isSubtypeOfClass(pluginContext.listClass)) return null
//
//        // FIXME hackish list item type retrieval
//        val itemType = (type as IrSimpleTypeImpl).arguments.first() as IrType
//        check(! itemType.isNullable()) { "nullable items in lists are not supported" }
//
//        return primitive(itemType)
//    }
//
//    operator fun get(type: IrType) =
//        protoCoders.getOrPut(type) { add(type) }
//
//    fun add(type: IrType): IrClass? {
//
//        val companion = type.getClass()?.companionObject() ?: tryLoadCompanion(type) ?: return null
//
//        if (! companion.isSubclassOf(protoEncoderClass)) return null
//        if (! companion.isSubclassOf(protoDecoderClass)) return null
//
//        pluginContext.debug("service") { "protoCache add $type $companion" }
//        return companion
//    }
//
//    fun add(type: IrType, companion: IrClass) {
//        pluginContext.debug("service") { "protoCache add $type $companion" }
//        protoCoders[type] = companion
//    }
//

    fun signature(function: IrFunction): String {
        val parts = mutableListOf<String>()

        parts += function.name.identifier + ";"

        function.valueParameters.mapTo(parts) {
            val typeFqName = checkNotNull(it.type.classFqName) { "unsupported type: ${it.type.asString()}" }
            val wireFormatType = types[typeFqName.asString()] ?: loadType(typeFqName)
            wireFormatType.signature
        }

        return parts.joinToString("")
    }

    private fun loadType(typeFqName: FqName): WireFormatType {
        val type = typeFqName.companionClassId.toInstanceWireFormatType(pluginContext)
        types[typeFqName.asString()] = type
        return type
    }


    fun encodeReturnValue(targetType: IrType, encoder: IrExpression, value: IrExpression): IrExpression {
        val typeFqName = checkNotNull(targetType.classFqName) { "unsupported type: ${targetType.asString()}" }
        val wireFormatType = types[typeFqName.asString()] ?: loadType(typeFqName)

        return irCall(pluginContext.rawInstance, encoder, value, irGetObject(wireFormatType.wireFormat))
    }

    fun decodeReturnValue(targetType: IrType, decoder: IrExpression): IrExpression {
        val typeFqName = checkNotNull(targetType.classFqName) { "unsupported type: ${targetType.asString()}" }
        val wireFormatType = types[typeFqName.asString()] ?: loadType(typeFqName)

        return irCall(pluginContext.asInstance, decoder, irGetObject(wireFormatType.wireFormat))
    }

    fun encode(encoder: IrCall, fieldNumber: Int, fieldName: String, value: IrExpression): IrCallImpl {
        val targetType = value.type

        val typeFqName = checkNotNull(targetType.classFqName) { "unsupported type: ${targetType.asString()}" }
        val wireFormatType = types[typeFqName.asString()] ?: loadType(typeFqName)


        if (wireFormatType.primitive) {
            return irCall(wireFormatType.encode, encoder, irConst(fieldNumber), irConst(fieldName), value)
        } else {
            val wireFormat = checkNotNull(wireFormatType.wireFormat)
            return irCall(wireFormatType.encode, encoder, irConst(fieldNumber), irConst(fieldName), value, irGetObject(wireFormat))
        }
    }

    fun decode(fieldNumber: Int, valueParameter: IrValueParameter, decoder: () -> IrExpression): IrCallImpl {
        val targetType = valueParameter.type
        val fieldName = valueParameter.name.identifier

        val typeFqName = checkNotNull(targetType.classFqName) { "unsupported type: ${targetType.asString()}" }
        val wireFormatType = types[typeFqName.asString()] ?: loadType(typeFqName)

        if (wireFormatType.primitive) {
            return irCall(wireFormatType.decode, decoder(), irConst(fieldNumber), irConst(fieldName))
        } else {
            return irCall(wireFormatType.decode, decoder(), irConst(fieldNumber), irConst(fieldName), irGetObject(wireFormatType.wireFormat))
        }
    }
}