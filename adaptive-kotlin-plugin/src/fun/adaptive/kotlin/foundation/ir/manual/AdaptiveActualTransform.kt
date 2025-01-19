/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.manual

import `fun`.adaptive.kotlin.common.property
import `fun`.adaptive.kotlin.foundation.ClassIds
import `fun`.adaptive.kotlin.foundation.Names
import `fun`.adaptive.kotlin.foundation.ir.FoundationPluginContext
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.properties

class AdaptiveActualTransform(
    private val pluginContext: FoundationPluginContext
) : IrElementTransformerVoidWithContext() {

    override fun visitClassNew(declaration: IrClass): IrStatement {
        if (! declaration.hasAnnotation(ClassIds.ADAPTIVE_ACTUAL)) return declaration

        val propertyMap = transformProperties(declaration)

        transformConstructors(declaration, propertyMap.size)

        transformHaveToPatch(declaration, propertyMap)

        return declaration
    }

    fun transformProperties(declaration: IrClass): Map<IrSimpleFunctionSymbol, Int> {

        var index = 0 // index 0 is reserved for instructions
        val propertyMap = mutableMapOf<IrSimpleFunctionSymbol, Int>()

        for (property in declaration.properties) {
            if (! property.isDelegated) continue
            val expression = property.backingField?.initializer?.expression ?: continue
            if (expression !is IrCall) continue
            if (expression.symbol.owner.name != Names.STATE_VARIABLE) continue

            property.isDelegated = false
            property.backingField = null
            transformGetter(property, index)
            transformSetter(property, index)

            propertyMap[property.getter!!.symbol] = index
            index ++
        }

        return propertyMap
    }

    fun transformGetter(property: IrProperty, index: Int) {
        val getter = property.getter ?: return

        getter.origin = IrDeclarationOrigin.DEFINED

        getter.body = DeclarationIrBuilder(pluginContext.irContext, getter.symbol).irBlockBody {
            + irReturn(
                irCall(
                    pluginContext.get,
                    getter.returnType,
                    typeArgumentsCount = 1,
                    valueArgumentsCount = 1
                ).also { call ->
                    call.dispatchReceiver = irGet(getter.dispatchReceiverParameter !!)
                    call.putTypeArgument(0, getter.returnType)
                    call.putValueArgument(0, irInt(index))
                }
            )
        }
    }

    fun transformSetter(property: IrProperty, index: Int) {
        val setter = property.setter ?: return

        setter.body = DeclarationIrBuilder(pluginContext.irContext, setter.symbol).irBlockBody {
            + irReturn(
                irCall(
                    pluginContext.set,
                    property.getter !!.returnType,
                    typeArgumentsCount = 0,
                    valueArgumentsCount = 1
                ).also { call ->
                    call.dispatchReceiver = irGet(setter.dispatchReceiverParameter !!)
                    call.putValueArgument(0, irInt(index))
                    call.putValueArgument(1, irGet(setter.valueParameters.first()))
                }
            )
        }
    }

    fun transformConstructors(declaration : IrClass, stateSize: Int) {
        declaration.constructors.forEach {
            it.transform(
                StateSizeTransform(pluginContext, stateSize),
                null
            )
        }
    }

    private fun transformHaveToPatch(declaration: IrClass, propertyMap: Map<IrSimpleFunctionSymbol, Int>) {
        val functions = declaration.functions

        val haveToPatches = functions.filter { it.name == Names.HAVE_TO_PATCH }
        val haveToPatchMask = haveToPatches.single { it.valueParameters.size == 2 }
        val haveToPatchVariable = haveToPatches.single { it.valueParameters.size == 1 }

        val dirtyMask = declaration.property(Names.DIRTY_MASK)

        declaration.functions.forEach {
            it.transform(
                HaveToPatchTransform(
                    pluginContext,
                    dirtyMask,
                    haveToPatchVariable.symbol,
                    haveToPatchMask.symbol,
                    propertyMap
                ),
                null
            )
        }
    }


}
