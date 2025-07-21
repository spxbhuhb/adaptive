/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.service.ir.impl

import `fun`.adaptive.kotlin.common.firstRegularParameter
import `fun`.adaptive.kotlin.common.functionByName
import `fun`.adaptive.kotlin.common.property
import `fun`.adaptive.kotlin.service.Names
import `fun`.adaptive.kotlin.service.Strings
import `fun`.adaptive.kotlin.service.ir.ServicesPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.defaultType

class NewInstance(
   val pluginContext: ServicesPluginContext,
    val implClassTransform: ImplClassTransform,
) {
    fun build() {
        val transformedClass = implClassTransform.transformedClass
        val functionSymbol = transformedClass.functionByName { Strings.NEW_INSTANCE }
        val function = functionSymbol.owner

        function.body = DeclarationIrBuilder(pluginContext.irContext, functionSymbol).irBlockBody {

            val instance = irTemporary(
                IrConstructorCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    transformedClass.defaultType,
                    implClassTransform.constructor.symbol,
                    typeArgumentsCount = 0,
                    constructorTypeArgumentsCount = 0
                ).also {
                    it.arguments[0] = irGet(function.firstRegularParameter)
                }
            )

            val property = transformedClass.property(Names.FRAGMENT)

            + implClassTransform.irSetValue(
                property,
                implClassTransform.irGetValue(property, irGet(function.dispatchReceiverParameter !!)),
                irGet(instance)
            )

            + irReturn(
                irGet(instance)
            )
        }
    }

}