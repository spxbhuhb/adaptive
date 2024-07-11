/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatcompanion

import hu.simplexion.adaptive.kotlin.adat.ir.AdatIrBuilder
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.parentAsClass

fun AdatIrBuilder.newInstanceEmpty(
    companionClass: IrClass,
    newInstanceFunction: IrFunction
) {

    newInstanceFunction.body = DeclarationIrBuilder(pluginContext.irContext, newInstanceFunction.symbol).irBlockBody {
        + irReturn(
            IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                newInstanceFunction.returnType,
                companionClass.parentAsClass.constructors.first { it.valueParameters.isEmpty() }.symbol,
                0, 0, 0
            )
        )
    }

}