/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.manual

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
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.properties

class AdaptiveActualTransform(
    private val pluginContext: FoundationPluginContext
) : IrElementTransformerVoidWithContext() {

    // original:
    //
    // PROPERTY name:p1 visibility:public modality:FINAL [delegated,val]
    //  FIELD PROPERTY_DELEGATE name:p1$delegate type:kotlin.properties.ReadWriteProperty<fun.adaptive.foundation.AdaptiveFragment, kotlin.Int> visibility:private [final]
    //    EXPRESSION_BODY
    //      CALL 'protected final fun stateVariable <T> (): kotlin.properties.ReadWriteProperty<fun.adaptive.foundation.AdaptiveFragment, T of stuff.AdaptiveTest.stateVariable> [fake_override] declared in stuff.AdaptiveTest' type=kotlin.properties.ReadWriteProperty<fun.adaptive.foundation.AdaptiveFragment, kotlin.Int> origin=null
    //        <T>: kotlin.Int
    //        $this: GET_VAR '<this>: stuff.AdaptiveTest declared in stuff.AdaptiveTest' type=stuff.AdaptiveTest origin=null
    //  FUN DELEGATED_PROPERTY_ACCESSOR name:<get-p1> visibility:public modality:FINAL <> ($this:stuff.AdaptiveTest) returnType:kotlin.Int
    //    correspondingProperty: PROPERTY name:p1 visibility:public modality:FINAL [delegated,val]
    //    $this: VALUE_PARAMETER name:<this> type:stuff.AdaptiveTest
    //    BLOCK_BODY
    //      RETURN type=kotlin.Nothing from='public final fun <get-p1> (): kotlin.Int declared in stuff.AdaptiveTest'
    //        CALL 'public abstract fun getValue (thisRef: T of kotlin.properties.ReadWriteProperty, property: kotlin.reflect.KProperty<*>): V of kotlin.properties.ReadWriteProperty [operator] declared in kotlin.properties.ReadWriteProperty' type=kotlin.Int origin=null
    //          $this: GET_FIELD 'FIELD PROPERTY_DELEGATE name:p1$delegate type:kotlin.properties.ReadWriteProperty<fun.adaptive.foundation.AdaptiveFragment, kotlin.Int> visibility:private [final] declared in stuff.AdaptiveTest' type=kotlin.properties.ReadWriteProperty<fun.adaptive.foundation.AdaptiveFragment, kotlin.Int> origin=null
    //            receiver: GET_VAR '<this>: stuff.AdaptiveTest declared in stuff.AdaptiveTest.<get-p1>' type=stuff.AdaptiveTest origin=null
    //          thisRef: GET_VAR '<this>: stuff.AdaptiveTest declared in stuff.AdaptiveTest.<get-p1>' type=stuff.AdaptiveTest origin=null
    //          property: PROPERTY_REFERENCE 'public final p1: kotlin.Int [delegated,val] declared in stuff.AdaptiveTest' field=null getter='public final fun <get-p1> (): kotlin.Int declared in stuff.AdaptiveTest' setter=null type=kotlin.reflect.KProperty1<stuff.AdaptiveTest, kotlin.Int> origin=PROPERTY_REFERENCE_FOR_DELEGATE

    // transformed:
    //
    // PROPERTY name:p1 visibility:public modality:FINAL [var]
    //  FUN name:<get-p1> visibility:public modality:FINAL <> ($this:stuff.AdaptiveTest) returnType:kotlin.Int
    //    correspondingProperty: PROPERTY name:p1 visibility:public modality:FINAL [var]
    //    $this: VALUE_PARAMETER name:<this> type:stuff.AdaptiveTest
    //    BLOCK_BODY
    //      RETURN type=kotlin.Nothing from='public final fun <get-p1> (): kotlin.Int declared in stuff.AdaptiveTest'
    //        CALL 'public final fun get <T> (index: kotlin.Int): T of stuff.AdaptiveTest.get [inline,fake_override] declared in stuff.AdaptiveTest' type=kotlin.Int origin=null
    //          <T>: kotlin.Int
    //          $this: GET_VAR '<this>: stuff.AdaptiveTest declared in stuff.AdaptiveTest.<get-p1>' type=stuff.AdaptiveTest origin=null
    //          index: CONST Int type=kotlin.Int value=1
    //  FUN name:<set-p1> visibility:public modality:FINAL <> ($this:stuff.AdaptiveTest, v:kotlin.Int) returnType:kotlin.Unit
    //    correspondingProperty: PROPERTY name:p1 visibility:public modality:FINAL [var]
    //    $this: VALUE_PARAMETER name:<this> type:stuff.AdaptiveTest
    //    VALUE_PARAMETER name:v index:0 type:kotlin.Int
    //    BLOCK_BODY
    //      RETURN type=kotlin.Nothing from='public final fun <set-p1> (v: kotlin.Int): kotlin.Unit declared in stuff.AdaptiveTest'
    //        CALL 'public final fun set (index: kotlin.Int, value: kotlin.Any?): kotlin.Unit [fake_override] declared in stuff.AdaptiveTest' type=kotlin.Unit origin=null
    //          $this: GET_VAR '<this>: stuff.AdaptiveTest declared in stuff.AdaptiveTest.<set-p1>' type=stuff.AdaptiveTest origin=null
    //          index: CONST Int type=kotlin.Int value=1
    //          value: GET_VAR 'v: kotlin.Int declared in stuff.AdaptiveTest.<set-p1>' type=kotlin.Int origin=null

    override fun visitClassNew(declaration: IrClass): IrStatement {
        if (! declaration.hasAnnotation(ClassIds.ADAPTIVE_ACTUAL)) return declaration

        var index = 0 // index 0 is reserved for instructions

        for (property in declaration.properties) {
            if (! property.isDelegated) continue
            val expression = property.backingField?.initializer?.expression ?: continue
            if (expression !is IrCall) continue
            if (expression.symbol.owner.name != Names.STATE_VARIABLE) continue

            property.isDelegated = false
            property.backingField = null
            transformGetter(property, index)
            transformSetter(property, index)

            index ++
        }

        declaration.constructors.forEach { it.transform(AdaptiveActualStateSizeTransform(pluginContext, index), null) }

        return declaration
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

}
