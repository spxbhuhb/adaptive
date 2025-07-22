/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.common

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.buildField
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrSetFieldImpl
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.parentAsClass

/**
 * If the property is a fake override:
 *
 * - adds a backing field with type [propertyType]
 * - adds a default getter
 * - if the property is var adds a setter
 */
fun transformProperty(
    pluginContext: AbstractPluginContext,
    property: IrProperty,
    backingField: Boolean,
    value: AbstractIrBuilder.() -> IrExpression
) {
    PropertyTransform(pluginContext, property, backingField, value).transform()
}

class PropertyTransform(
    override val pluginContext: AbstractPluginContext,
    val property: IrProperty,
    val backingField: Boolean,
    val initializerOrGetter: AbstractIrBuilder.() -> IrExpression
) : AbstractIrBuilder {

    fun transform() {
        val fromPlugin = (property.origin is IrDeclarationOrigin.GeneratedByPlugin)
        if (!fromPlugin && !property.isFakeOverride) return

        property.isFakeOverride = false
        if (!fromPlugin) {
            property.origin = IrDeclarationOrigin.DEFINED
        }

        if (backingField) {
            property.backingField = irFactory.buildField {
                name = property.name
                type = property.getter!!.returnType
                origin = IrDeclarationOrigin.PROPERTY_BACKING_FIELD
                visibility = DescriptorVisibilities.PRIVATE
            }.apply {
                parent = property.parent
                initializer = irFactory.createExpressionBody(initializerOrGetter())
                correspondingPropertySymbol = property.symbol
            }
        } else {
            property.backingField = null
        }

        transformGetter(property.parentAsClass, property.getter !!, property.backingField, initializerOrGetter)

        if (property.isVar && backingField) {
            transformSetter(property.setter !!, property.backingField !!)
        }
    }

    fun transformSetter(func: IrSimpleFunction, field: IrField) {
        func.origin = IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR
        func.isFakeOverride = false

        func.replaceDispatchReceiver(property.parentAsClass.defaultType)

        func.body = DeclarationIrBuilder(irContext, func.symbol).irBlockBody {
            + IrSetFieldImpl(
                UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                field.symbol,
                irBuiltIns.unitType
            ).also {
                it.receiver = irGet(func.dispatchReceiverParameter !!)
                it.value = irGet(func.firstRegularParameter)
            }
        }
    }

}