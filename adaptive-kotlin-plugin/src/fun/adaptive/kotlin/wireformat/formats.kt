/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.wireformat

import `fun`.adaptive.kotlin.common.AbstractIrBuilder
import `fun`.adaptive.kotlin.common.asClassId
import `fun`.adaptive.kotlin.common.functionByName
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.isSubclassOf
import org.jetbrains.kotlin.name.ClassId

data class SignatureWireFormat(
    val basicFormat: BasicWireFormat? = null,
    val genericFormat: GenericWireFormat? = null,
    val nullable: Boolean = false,
    val arguments: List<SignatureWireFormat> = emptyList()
) {
    val isPrimitive
        get() = basicFormat != null && basicFormat is PrimitiveWireFormat

    fun buildWireFormat(builder: AbstractIrBuilder): IrExpression =
        when {
            basicFormat != null -> builder.irGetObject(basicFormat.classWireFormat)
            genericFormat != null -> genericFormat.buildWireFormat(builder, arguments)
            else -> throw IllegalStateException("unknown wireformat")
        }
}

class GenericWireFormat(
    val pluginContext: WireFormatPluginContext,
    classId: ClassId
) {
    val constructor: IrConstructorSymbol

    init {
        val symbol = checkNotNull(pluginContext.irContext.referenceClass(classId)) { "missing class: ${classId.asFqNameString()}" }
        check(symbol.owner.isSubclassOf(pluginContext.wireFormatClass.owner)) { "class ${classId.asFqNameString()} does not implement ${ClassIds.WIREFORMAT}" }

        val typeParameterCount = symbol.owner.typeParameters.size
        constructor = symbol.constructors.first { it.owner.valueParameters.size == typeParameterCount }
    }

    fun buildWireFormat(builder: AbstractIrBuilder, arguments: List<SignatureWireFormat>): IrExpression =
        IrConstructorCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            pluginContext.wireFormatClass.defaultType, // TODO generic wire format: should pass proper types?
            constructor,
            typeArgumentsCount = 0,
            constructorTypeArgumentsCount = 0,
            valueArgumentsCount = arguments.size
        ).also {
            arguments.forEachIndexed { index, argument ->
                it.putValueArgument(
                    index,
                    IrConstructorCallImpl(
                        SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                        pluginContext.wireFormatTypeArgument.defaultType,
                        pluginContext.wireFormatTypeArgument.constructors.first(),
                        typeArgumentsCount = 0,
                        constructorTypeArgumentsCount = 0,
                        valueArgumentsCount = 2
                    ).also {
                        it.putValueArgument(0, argument.buildWireFormat(builder))
                        it.putValueArgument(1, builder.irConst(argument.nullable))
                    }
                )
            }
        }
}

interface BasicWireFormat {
    val classWireFormat: IrClassSymbol
    val representedClass: String
    val signature: String
}

class PrimitiveWireFormat(
    pluginContext: WireFormatPluginContext,
    builtinFormatShortName: String, // Int for IntWireFormat
    override val signature: String
) : BasicWireFormat {
    val encode: IrSimpleFunctionSymbol // int
    val decode: IrSimpleFunctionSymbol // int

    val encodeOrNull: IrSimpleFunctionSymbol // intOrNull
    val decodeOrNull: IrSimpleFunctionSymbol // intOrNull

    override val representedClass = checkNotNull(Signature.reversedShorthands[signature]) { "built-in signature mismatch, this is an error in the plugin, please open an issue" }
    override val classWireFormat: IrClassSymbol

    init {
        with(pluginContext) {
            val lcName = builtinFormatShortName.take(1).lowercase() + builtinFormatShortName.drop(1)
            val ucName = builtinFormatShortName.take(1).uppercase() + builtinFormatShortName.drop(1)

            val className = Strings.BUILTIN_PACKAGE + "." + ucName + "WireFormat"
            val classId = className.asClassId

            encode = wireFormatEncoder.functionByName { lcName }
            decode = wireFormatDecoder.functionByName { lcName }

            encodeOrNull = wireFormatEncoder.functionByName { "${lcName}OrNull" }
            decodeOrNull = wireFormatDecoder.functionByName { "${lcName}OrNull" }

            val symbol = checkNotNull(pluginContext.irContext.referenceClass(classId)) { "missing class: ${classId.asFqNameString()}" }
            check(symbol.owner.isSubclassOf(pluginContext.wireFormatClass.owner)) { "class ${classId.asFqNameString()} does not implement ${ClassIds.WIREFORMAT}" }

            classWireFormat = symbol
        }
    }
}

class ClassWireFormat(
    pluginContext: WireFormatPluginContext,
    formatClassId: ClassId,
    override val representedClass: String
) : BasicWireFormat {

    override val signature = "L$representedClass;"

    constructor(
        pluginContext: WireFormatPluginContext,
        formatClassName: String,
        signatureClassName: String
    ) : this(pluginContext, formatClassName.asClassId, signatureClassName)

    override val classWireFormat: IrClassSymbol

    init {
        val symbol = checkNotNull(pluginContext.irContext.referenceClass(formatClassId)) { "missing class: ${formatClassId.asFqNameString()}" }
        check(symbol.owner.isSubclassOf(pluginContext.wireFormatClass.owner)) { "class ${formatClassId.asFqNameString()} does not implement ${ClassIds.WIREFORMAT}" }

        classWireFormat = symbol
    }

}

class PolymorphicWireFormat(
    pluginContext: WireFormatPluginContext
) : BasicWireFormat {

    override val representedClass = "*"
    override val signature = "*"

    override val classWireFormat = checkNotNull(pluginContext.irContext.referenceClass(ClassIds.POLYMORPHIC_WIREFORMAT))

}