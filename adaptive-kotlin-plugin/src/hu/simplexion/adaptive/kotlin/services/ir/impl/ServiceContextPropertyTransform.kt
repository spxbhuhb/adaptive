/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.services.ir.impl

import hu.simplexion.adaptive.kotlin.services.ir.ServicesPluginContext
import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.builders.declarations.buildField
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrExpressionBody

class ServiceContextPropertyTransform(
    override val pluginContext: ServicesPluginContext,
    val implClassTransform: ImplClassTransform,
    var property: IrProperty
) : AbstractIrBuilder {

    fun build() {
        if (! property.isFakeOverride) return

        property.isFakeOverride = false
        property.origin = IrDeclarationOrigin.DEFINED

        property.backingField = irFactory.buildField {
            name = property.name
            type = pluginContext.serviceContextType
            origin = IrDeclarationOrigin.PROPERTY_BACKING_FIELD
            visibility = DescriptorVisibilities.PRIVATE
        }.apply {
            parent = property.parent
            initializer = buildInitializer()
            correspondingPropertySymbol = property.symbol
        }

        transformGetter(implClassTransform.transformedClass, property.getter !!, property.backingField !!)
    }

    fun buildInitializer(): IrExpressionBody =
        irFactory.createExpressionBody(irGet(implClassTransform.constructor.valueParameters.first()))

}