/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.base.ir.arm2ir

import hu.simplexion.adaptive.kotlin.base.Indices
import hu.simplexion.adaptive.kotlin.base.Strings
import hu.simplexion.adaptive.kotlin.base.ir.AdaptivePluginContext
import hu.simplexion.adaptive.kotlin.base.ir.arm.ArmEntryPoint
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.util.*

class ArmEntryPointBuilder(
    context: AdaptivePluginContext,
    val entryPoint: ArmEntryPoint
) : ClassBoundIrBuilder(context) {

    fun entryPointBody() {
        val function = entryPoint.irFunction
        irClass = pluginContext.irClasses[entryPoint.armClass.fqName]!!

        function.body = DeclarationIrBuilder(irContext, function.symbol).irBlockBody {

            val adapter = irTemporary(
                irImplicitAs(
                    classBoundAdapterType,
                    irGet(function.valueParameters.first())
                )
            )

            val instance = IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irClass.defaultType,
                irClass.constructors.single().symbol,
                0, 0,
                Indices.ADAPTIVE_GENERATED_FRAGMENT_ARGUMENT_COUNT
            ).also { call ->
                call.putValueArgument(Indices.ADAPTIVE_FRAGMENT_ADAPTER, irGet(adapter))
                call.putValueArgument(Indices.ADAPTIVE_FRAGMENT_PARENT, irNull())
                call.putValueArgument(Indices.ADAPTIVE_FRAGMENT_INDEX, irConst(0))
            }

            val root = irTemporary(instance).also { it.parent = function }

            + irCall(
                pluginContext.adaptiveAdapterClass.getPropertySetter(Strings.ROOT_FRAGMENT)!!,
                dispatchReceiver = irGet(adapter),
                args = arrayOf(
                    irGet(root)
                )
            )

            + irCall(
                pluginContext.create,
                dispatchReceiver = irGet(root)
            )

            + irCall(
                pluginContext.mount,
                dispatchReceiver = irGet(root),
                args = arrayOf(
                    irCall(
                        pluginContext.adaptiveAdapterClass.getPropertyGetter(Strings.ROOT_BRIDGE) !!.owner.symbol,
                        dispatchReceiver = irGet(adapter)
                    )
                )
            )
        }

    }
}
