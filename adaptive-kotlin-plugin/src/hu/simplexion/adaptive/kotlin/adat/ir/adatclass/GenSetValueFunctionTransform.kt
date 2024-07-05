/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatclass

import hu.simplexion.adaptive.kotlin.adat.ir.AdatPluginContext
import hu.simplexion.adaptive.kotlin.adat.ir.metadata.PropertyData
import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irWhen
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.impl.IrBranchImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

class GenSetValueFunctionTransform(
    override val pluginContext: AdatPluginContext,
    val adatClass: IrClass,
    val properties: List<PropertyData>,
    val function: IrFunction
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    fun transform() {
        function.transform(this, null)
    }

    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        function as IrSimpleFunction

        function.body = DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
            + irWhen(
                irBuiltIns.unitType,
                properties.mapNotNull { transformProperty(it) } // FIXME throw exception on else
            )
        }

        return declaration
    }

    fun transformProperty(property: PropertyData) =
        if (property.property.isVar) {
            IrBranchImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irEqual(irGet(function.valueParameters.first()), irConst(property.metadata.index)),
                irSetValue(
                    property.property,
                    irImplicitAs(
                        property.property.getter !!.returnType,
                        irGet(function.valueParameters[1])
                    ),
                    irGet(function.dispatchReceiverParameter !!)
                )
            )
        } else {
            null
        }
}
