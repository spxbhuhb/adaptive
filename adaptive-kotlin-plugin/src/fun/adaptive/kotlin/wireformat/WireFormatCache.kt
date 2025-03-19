/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.wireformat

import `fun`.adaptive.kotlin.common.AbstractIrBuilder
import `fun`.adaptive.kotlin.common.AbstractPluginContext
import `fun`.adaptive.kotlin.common.asClassId
import `fun`.adaptive.kotlin.common.companionClassId
import `fun`.adaptive.kotlin.wireformat.Signature.typeSignature
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.types.isNullable
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class WireFormatCache(
    parentPluginContext: AbstractPluginContext
) : AbstractIrBuilder {

    override val pluginContext = WireFormatPluginContext(parentPluginContext.irContext, parentPluginContext.options)

    val pack
        get() = pluginContext.pack

    val wireFormatEncoder
        get() = pluginContext.wireFormatEncoder

    val basicFormats = /* mutableMapOf, see below */ listOf(
        PrimitiveWireFormat(pluginContext, "Unit", "0"),

        PrimitiveWireFormat(pluginContext, "Boolean", "Z"),
        PrimitiveWireFormat(pluginContext, "Int", "I"),
        PrimitiveWireFormat(pluginContext, "Short", "S"),
        PrimitiveWireFormat(pluginContext, "Byte", "B"),
        PrimitiveWireFormat(pluginContext, "Long", "J"),
        PrimitiveWireFormat(pluginContext, "Float", "F"),
        PrimitiveWireFormat(pluginContext, "Double", "D"),
        PrimitiveWireFormat(pluginContext, "Char", "C"),

        PrimitiveWireFormat(pluginContext, "BooleanArray", "[Z"),
        PrimitiveWireFormat(pluginContext, "IntArray", "[I"),
        PrimitiveWireFormat(pluginContext, "ShortArray", "[S"),
        PrimitiveWireFormat(pluginContext, "ByteArray", "[B"),
        PrimitiveWireFormat(pluginContext, "LongArray", "[J"),
        PrimitiveWireFormat(pluginContext, "FloatArray", "[F"),
        PrimitiveWireFormat(pluginContext, "DoubleArray", "[D"),
        PrimitiveWireFormat(pluginContext, "CharArray", "[C"),

        PrimitiveWireFormat(pluginContext, "String", "T"),

        PrimitiveWireFormat(pluginContext, "UInt", "+I"),
        PrimitiveWireFormat(pluginContext, "UShort", "+S"),
        PrimitiveWireFormat(pluginContext, "UByte", "+B"),
        PrimitiveWireFormat(pluginContext, "ULong", "+J"),

        PrimitiveWireFormat(pluginContext, "UIntArray", "[+I"),
        PrimitiveWireFormat(pluginContext, "UShortArray", "[+S"),
        PrimitiveWireFormat(pluginContext, "UByteArray", "[+B"),
        PrimitiveWireFormat(pluginContext, "ULongArray", "[+J"),

        PrimitiveWireFormat(pluginContext, "Uuid", "U"),

        PolymorphicWireFormat(pluginContext),

        ClassWireFormat(pluginContext, Strings.DURATION_WIREFORMAT, "kotlin.time.Duration"),
        ClassWireFormat(pluginContext, Strings.INSTANT_WIREFORMAT, "kotlinx.datetime.Instant"),
        ClassWireFormat(pluginContext, Strings.LOCALDATETIME_WIREFORMAT, "kotlinx.datetime.LocalDateTime"),
        ClassWireFormat(pluginContext, Strings.LOCALDATE_WIREFORMAT, "kotlinx.datetime.LocalDate"),
        ClassWireFormat(pluginContext, Strings.LOCALTIME_WIREFORMAT, "kotlinx.datetime.LocalTime")

    ).associateBy { it.representedClass }.toMutableMap()

    val genericFormats = mutableMapOf(
        // issue #33 "kotlin.Array" to loadBuiltinGenericFormat(Strings.ARRAY_WIREFORMAT.asClassId),
        "kotlin.collections.List" to loadBuiltinGenericFormat(Strings.LIST_WIREFORMAT.asClassId),
        "kotlin.collections.Set" to loadBuiltinGenericFormat(Strings.SET_WIREFORMAT.asClassId),
        "kotlin.collections.Map" to loadBuiltinGenericFormat(Strings.MAP_WIREFORMAT.asClassId),
        "kotlin.collections.Pair" to loadBuiltinGenericFormat(Strings.PAIR_WIREFORMAT.asClassId)
    )

    val signatureFormats = basicFormats.values
        .associate { it.signature to SignatureWireFormat(it) }
        .toMutableMap()

    fun getSignatureFormat(targetType: IrType): SignatureWireFormat =
        typeSignature(targetType, pluginContext.adatClass).let { signatureFormats[it] ?: loadSignatureFormat(it, targetType) }

    private fun loadSignatureFormat(signature: String, irType: IrType): SignatureWireFormat {
        val typeFqName = checkNotNull(irType.classFqName) { "type without classFqName: $irType" }
        val classFqName = typeFqName.asString()

        // null and non-null formats are the same on the top level

        if (signature.endsWith('?')) {
            val nonNullSignature = signature.dropLast(1)
            val nonNullFormat = signatureFormats[nonNullSignature]
            if (nonNullFormat != null) {
                return nonNullFormat.copy(nullable = true).also { signatureFormats[signature] = it }
            }
        }

        check(irType is IrSimpleTypeImpl) { "not a simple type: $irType" }

        // Abstract classes are polymorphic by default, so those cannot use the
        // companion.

        // Without type arguments we can load the companion directly as a class format.
        // Adat classes may have type arguments, but we don't care about that as the
        // Adat plugin has to handle those.

        val isAdatClass = irType.isSubtypeOfClass(pluginContext.adatClass)
        val isAbstract = (irType.classOrNull?.owner?.modality == Modality.ABSTRACT)

        // FIXME polymorphic vs. abstract vs. adatclass
        if (isAdatClass && isAbstract) {
            return SignatureWireFormat(PolymorphicWireFormat(pluginContext)).also { signatureFormats[signature] = it }
        }

        if (irType.arguments.isEmpty() || isAdatClass) {
            val basicFormat = basicFormats[classFqName] ?: loadClassFormat(typeFqName)
            return SignatureWireFormat(basicFormat).also { signatureFormats[signature] = it }
        }

        // with type arguments we have a class and have to create an instance

        val arguments = mutableListOf<SignatureWireFormat>()

        irType.arguments.forEach {
            check(it is IrType) { "invalid type argument in : $irType $signature" }
            arguments += getSignatureFormat(it as IrType)
        }

        val genericFormat = genericFormats[classFqName] ?: loadGeneratedGenericFormat(classFqName.asClassId)

        return SignatureWireFormat(genericFormat = genericFormat, arguments = arguments)
    }

    fun loadClassFormat(typeFqName: FqName): ClassWireFormat =
        ClassWireFormat(pluginContext, typeFqName.companionClassId, typeFqName.asString())
            .also { basicFormats[typeFqName.asString()] = it }

    fun loadBuiltinGenericFormat(classId: ClassId): GenericWireFormat =
        GenericWireFormat(pluginContext, classId)

    fun loadGeneratedGenericFormat(classId: ClassId): GenericWireFormat =
        GenericWireFormat(pluginContext, classId.createNestedClassId(Name.identifier("WireFormat")))
            .also { genericFormats[classId.asString()] = it }

    fun encodeReturnValue(targetType: IrType, encoder: IrExpression, value: IrExpression): IrExpression {

        val rawInstanceFunc = if (targetType.isNullable()) {
            pluginContext.rawInstanceOrNull
        } else {
            pluginContext.rawInstance
        }

        return irCall(rawInstanceFunc, encoder, value, getSignatureFormat(targetType).buildWireFormat(this))
    }

    fun decodeReturnValue(targetType: IrType, decoder: IrExpression): IrExpression {

        val asInstanceFunc = if (targetType.isNullable()) {
            pluginContext.asInstanceOrNull
        } else {
            pluginContext.asInstance
        }

        return irCall(asInstanceFunc, decoder, getSignatureFormat(targetType).buildWireFormat(this))
    }

    fun encode(encoder: IrCall, fieldNumber: Int, fieldName: String, value: IrExpression): IrCallImpl {

        val signatureFormat = getSignatureFormat(value.type)

        if (signatureFormat.isPrimitive) {
            val encodeFunc = if (value.type.isNullable()) {
                (signatureFormat.basicFormat as PrimitiveWireFormat).encodeOrNull
            } else {
                (signatureFormat.basicFormat as PrimitiveWireFormat).encode
            }
            return irCall(encodeFunc, encoder, irConst(fieldNumber), irConst(fieldName), value)
        } else {
            val encodeFunc = if (value.type.isNullable()) {
                pluginContext.encodeInstanceOrNull
            } else {
                pluginContext.encodeInstance
            }
            return irCall(encodeFunc, encoder, irConst(fieldNumber), irConst(fieldName), value, signatureFormat.buildWireFormat(this))
        }
    }

    fun decode(fieldNumber: Int, valueParameter: IrValueParameter, decoder: () -> IrExpression): IrCallImpl {
        val fieldName = valueParameter.name.identifier

        val signatureFormat = getSignatureFormat(valueParameter.type)

        if (signatureFormat.isPrimitive) {
            val decodeFunc = if (valueParameter.type.isNullable()) {
                (signatureFormat.basicFormat as PrimitiveWireFormat).decodeOrNull
            } else {
                (signatureFormat.basicFormat as PrimitiveWireFormat).decode
            }
            return irCall(decodeFunc, decoder(), irConst(fieldNumber), irConst(fieldName))
        } else {
            val decodeFunc = if (valueParameter.type.isNullable()) {
                pluginContext.decodeInstanceOrNull
            } else {
                pluginContext.decodeInstance
            }
            return irCall(decodeFunc, decoder(), irConst(fieldNumber), irConst(fieldName), signatureFormat.buildWireFormat(this))
        }
    }
}