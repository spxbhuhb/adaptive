/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatclass

import hu.simplexion.adaptive.kotlin.adat.ir.AdatIrBuilder
import hu.simplexion.adaptive.kotlin.adat.ir.metadata.PropertyData
import hu.simplexion.adaptive.wireformat.signature.parseSignature
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irDelegatingConstructorCall
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.expressions.IrConstKind
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.deepCopyWithSymbols

fun AdatIrBuilder.emptyConstructor(
    adatClass: IrClass,
    constructor: IrConstructor,
    properties: List<PropertyData>
) {

    val primary = adatClass.constructors.first { it.isPrimary }
    if (constructor === primary) return // this should never happen as we use the plugin key to call this function

    val remaining = properties.toMutableSet()

    constructor.body = DeclarationIrBuilder(pluginContext.irContext, constructor.symbol).irBlockBody {

        + irDelegatingConstructorCall(
            irBuiltIns.anyClass.constructors.first().owner
        )

        for (parameter in primary.valueParameters) {

            val propertyData = properties.firstOrNull { it.property.name == parameter.name } ?: continue

            val defaultValue = parameter.defaultValue

            irSetValue(
                propertyData.property,
                (defaultValue?.deepCopyWithSymbols(primary) as IrExpression?) ?: sensibleDefault(propertyData),
                irGet(adatClass.thisReceiver !!),
            )

            remaining -= propertyData
        }
    }
}

private fun AdatIrBuilder.sensibleDefault(propertyData: PropertyData): IrExpression {

    val type = parseSignature(propertyData.metadata.signature)

    val name = type.name
    val generics = type.generics
    val length = name.length

    return when {

        generics.isNotEmpty() -> {
            TODO()
        }

        length > 3 -> {
            TODO()
        }

        else -> {

            when (length) {
                1 -> when (name) {
                    "T" -> irConst("")
                    "Z" -> irConst(false)
                    "I" -> irConst(0)
                    "U" -> {
                        val typeArg = propertyData.property.getter !!.returnType

                        IrConstructorCallImpl(
                            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                            typeArg,
                            pluginContext.commonUuidPrimary,
                            1, 0, 0
                        ).apply {
                            putTypeArgument(0, typeArg)
                        }
                    }

                    "J" -> irConst(0L)
                    "D" -> IrConstImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irBuiltIns.doubleType, IrConstKind.Double, 0.0)
                    "S" -> IrConstImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irBuiltIns.shortType, IrConstKind.Short, 0)
                    "B" -> IrConstImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irBuiltIns.byteType, IrConstKind.Byte, 0)
                    "F" -> IrConstImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irBuiltIns.floatType, IrConstKind.Float, 0f)
                    "C" -> IrConstImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irBuiltIns.charType, IrConstKind.Char, '\u0000')
                    else -> TODO("add collection types to sensible defaults: ${propertyData.metadata.signature}")
                }

                2 -> when (name) {

                    "[Z" -> newEmptyArray(irBuiltIns.booleanType)
                    "[I" -> newEmptyArray(irBuiltIns.intType)
                    "[S" -> newEmptyArray(irBuiltIns.shortType)
                    "[B" -> newEmptyArray(irBuiltIns.byteType)
                    "[J" -> newEmptyArray(irBuiltIns.longType)
                    "[F" -> newEmptyArray(irBuiltIns.floatType)
                    "[D" -> newEmptyArray(irBuiltIns.doubleType)
                    "[C" -> newEmptyArray(irBuiltIns.charType)

                    "+I" -> TODO()
                    "+S" -> TODO()
                    "+B" -> TODO()
                    "+J" -> TODO()
                    else -> TODO("add collection types to sensible defaults: ${propertyData.metadata.signature}")
                }

                else -> when (name) {
                    "[+I" -> TODO()
                    "[+S" -> TODO()
                    "[+B" -> TODO()
                    "[+J" -> TODO()
                    else -> TODO("add collection types to sensible defaults: ${propertyData.metadata.signature}")
                }
            }
        }
    }
}

private fun AdatIrBuilder.newEmptyArray(type: IrType) =
    IrCallImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        irBuiltIns.arrayClass.typeWith(type),
        pluginContext.emptyArray,
        1, 0
    ).also {
        it.putTypeArgument(0, type)
    }