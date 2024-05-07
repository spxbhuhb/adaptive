/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatclass

import hu.simplexion.adaptive.kotlin.adat.Names
import hu.simplexion.adaptive.kotlin.adat.ir.AdatPluginContext
import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import hu.simplexion.adaptive.kotlin.common.property
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irDelegatingConstructorCall
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.irSetField
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrDelegatingConstructorCall
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.companionObject
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.statements

class OriginalConstructorTransform(
    override val pluginContext: AdatPluginContext,
    val adatClass: IrClass,
    val constructor: IrConstructor,
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    override fun visitConstructor(declaration: IrConstructor): IrStatement {
        declaration.body = DeclarationIrBuilder(pluginContext.irContext, constructor.symbol).irBlockBody {

            + irDelegatingConstructorCall(
                irBuiltIns.anyClass.constructors.first().owner
            )

            + irSetField(
                irGet(adatClass.thisReceiver!!),
                adatClass.property(Names.ADAT_VALUES).backingField !!,
                IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.arrayClass.defaultType,
                    irBuiltIns.arrayOf,
                    1, 1,
                ).apply {
                    putTypeArgument(0, irBuiltIns.anyNType)
                    putValueArgument(0,
                        IrVarargImpl(
                            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                            irBuiltIns.arrayClass.defaultType,
                            irBuiltIns.anyNType
                        ).apply {
                            for (value in constructor.valueParameters) {
                                elements += irGet(value)
                            }
                        }
                    )
                }
            )
        }
        return declaration
    }

}
