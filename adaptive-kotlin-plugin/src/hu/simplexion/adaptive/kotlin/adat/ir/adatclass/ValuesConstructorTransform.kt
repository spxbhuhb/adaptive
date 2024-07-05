/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatclass

import hu.simplexion.adaptive.kotlin.adat.ir.AdatPluginContext
import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor

class ValuesConstructorTransform(
    override val pluginContext: AdatPluginContext,
    val adatClass: IrClass,
    val constructor: IrConstructor,
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    override fun visitConstructor(declaration: IrConstructor): IrStatement {
//        declaration.body = DeclarationIrBuilder(pluginContext.irContext, constructor.symbol).irBlockBody {
//
//            + irDelegatingConstructorCall(
//                irBuiltIns.anyClass.constructors.first().owner
//            )
//
//            + irSetField(
//                irGet(adatClass.thisReceiver!!),
//                adatClass.property(Names.ADAT_VALUES).backingField !!,
//                irGet(constructor.valueParameters.first())
//            )
//        }
        return declaration
    }

}
