/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.foundation.ir.arm2ir

import `fun`.adaptive.kotlin.common.firstRegularParameter
import `fun`.adaptive.kotlin.foundation.Strings
import `fun`.adaptive.kotlin.foundation.ir.FoundationPluginContext
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmEntryPoint
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.getPropertySetter

class ArmEntryPointBuilder(
    context: FoundationPluginContext,
    val entryPoint: ArmEntryPoint
) : ClassBoundIrBuilder(context, entryPoint.armClass) {

    fun entryPointBody() {
        val function = entryPoint.irFunction
        irClass = pluginContext.irClasses[entryPoint.armClass.fqName]!!

        function.body = DeclarationIrBuilder(irContext, function.symbol).irBlockBody {

            + irClass

            val adapter = irTemporary(
                irImplicitAs(
                    pluginContext.adaptiveAdapterType,
                    irGet(function.firstRegularParameter)
                )
            )

            val instance = IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irClass.defaultType,
                irClass.constructors.single().symbol,
                0, 0
            ).also { call ->
                call.arguments[0] = irGet(adapter)
                call.arguments[1] = irNull()
                call.arguments[2] = irConst(0)
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
                dispatchReceiver = irGet(root)
            )
        }

    }
}
