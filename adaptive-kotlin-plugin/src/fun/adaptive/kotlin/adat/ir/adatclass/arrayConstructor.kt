/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.adat.ir.adatclass

import `fun`.adaptive.kotlin.adat.ir.AdatIrBuilder
import `fun`.adaptive.kotlin.adat.ir.metadata.PropertyData
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.IrBlockBodyBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irDelegatingConstructorCall
import org.jetbrains.kotlin.ir.builders.irSetField
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrBranchImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrWhenImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.deepCopyWithSymbols
import org.jetbrains.kotlin.ir.util.superClass

fun AdatIrBuilder.arrayConstructor(
    adatClass: IrClass,
    constructor: IrConstructor,
    properties: List<PropertyData>
) {

    val primary = adatClass.constructors.first { it.isPrimary }
    if (constructor === primary) return

    constructor.body = DeclarationIrBuilder(pluginContext.irContext, constructor.symbol).irBlockBody {

        + irDelegatingConstructorCall(
            adatClass.superClass?.constructors?.first { it.valueParameters.isEmpty() } ?: irBuiltIns.anyClass.constructors.first().owner
        )

        + irCall(
            pluginContext.verifyParameters,
            dispatchReceiver = null,
            args = arrayOf(
                irGet(adatClass.thisReceiver !!),
                irGet(constructor.valueParameters.first())
            )
        )

        val values = constructor.valueParameters.first()

        for (property in properties) {
            val backingField = property.property.backingField !! // TODO handle properties without backing fields?

            + irSetField(
                receiver = irGet(adatClass.thisReceiver !!),
                backingField,
                irImplicitAs(
                    backingField.type,
                    valueOrDefault(this, values, property, primary, constructor, backingField.type)
                )
            )
        }
    }
}

private fun AdatIrBuilder.valueOrDefault(
    builder: IrBlockBodyBuilder,
    values: IrValueParameter,
    property: PropertyData,
    primary: IrConstructor,
    constructor: IrConstructor,
    type: IrType
): IrExpression {

    // gets the value from the passed array

    val call = irCall(
        pluginContext.arrayGet,
        dispatchReceiver = irGet(values),
        args = arrayOf(irConst(property.metadata.index))
    )

    // for nullable properties we can use the array value directly
    // TODO what if it is a nullable property with a default?
    if (property.metadata.isNullable) return call

    // get the parameter from the primary constructor, return with array value
    // if there is no default specified

    val defaultValue = primary.valueParameters.firstOrNull { it.name == property.property.name }?.defaultValue
    if (defaultValue == null) return call

    // here we have a non-nullable, default valued constructor parameter, have to copy the
    // default value expression and set it to the value if the array value is null


    val tmp = builder.irTemporary(call)

    val whenImpl = IrWhenImpl(
        SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
        irBuiltIns.anyType,
        IrStatementOrigin.WHEN
    )

    whenImpl.branches += IrBranchImpl(
        SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
        irEqual(irGet(tmp), irNull()),
        defaultValue.deepCopyWithSymbols(constructor).expression
    )

    whenImpl.branches += IrBranchImpl(
        SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
        irConst(true),
        irGet(tmp)
    )

    return whenImpl
}