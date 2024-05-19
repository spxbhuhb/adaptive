/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatclass

import hu.simplexion.adaptive.kotlin.adat.AdatPluginKey
import hu.simplexion.adaptive.kotlin.adat.Names
import hu.simplexion.adaptive.kotlin.adat.ir.AdatPluginContext
import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import hu.simplexion.adaptive.kotlin.common.functionByName
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.getSimpleFunction

class PropertyTransform(
    override val pluginContext: AdatPluginContext,
    val adatClass: IrClass,
    val propertyIndex: Int
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        if (declaration.origin == AdatPluginKey.origin) return declaration

        declaration.backingField = null

        val getter = declaration.getter !!

        getter.origin = IrDeclarationOrigin.DEFINED

        getter.body = DeclarationIrBuilder(pluginContext.irContext, getter.symbol).irBlockBody {
            + irReturn(
                irImplicitAs(
                    getter.returnType,
                    irCall(
                        adatClass.getValueByIndex.symbol,
                        irGet(getter.dispatchReceiverParameter!!),
                        irConst(propertyIndex)
                    )
                )
            )
        }

        if (declaration.isVar) {
            val setter = declaration.setter!!

            setter.origin = IrDeclarationOrigin.DEFINED

            setter.body = DeclarationIrBuilder(pluginContext.irContext, setter.symbol).irBlockBody {
                + irCall(
                    adatClass.setValueByIndex.symbol,
                    irGet(setter.dispatchReceiverParameter!!),
                    irConst(propertyIndex),
                    irGet(setter.valueParameters[0])
                )
            }
        }

        return declaration
    }

    val IrClass.getValueByIndex
        get() = functions.first { f ->
            f.name == Names.GET_VALUE && f.valueParameters.let { it.size == 1 && it.first().type == irBuiltIns.intType }
        }

    val IrClass.setValueByIndex
        get() = functions.first { f ->
            f.name == Names.SET_VALUE
                && f.valueParameters.let {
                    it.size == 2
                        && it.first().type == irBuiltIns.intType
                        && it[1].type == irBuiltIns.anyNType
                }
        }

}
