/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.services.ir.impl

import hu.simplexion.z2.kotlin.services.ir.ServicesPluginContext
import hu.simplexion.z2.kotlin.services.ir.util.ServiceBuilder
import hu.simplexion.z2.kotlin.common.AbstractIrBuilder
import org.jetbrains.kotlin.backend.common.ir.addDispatchReceiver
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.buildField
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.impl.IrSetFieldImpl
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.kotlinFqName

class ServiceNamePropertyTransform(
    override val pluginContext: ServicesPluginContext,
    val serviceBuilder: ServiceBuilder,
    val transformedClass: IrClass,
    var property: IrProperty
) : AbstractIrBuilder {

    fun build() {
        if (! property.isFakeOverride) return

        require(serviceBuilder.serviceNames.isNotEmpty()) { "${transformedClass.kotlinFqName} missing service interface (probably ': Service' is missing)" }
        require(serviceBuilder.serviceNames.size == 1) {
            "${transformedClass.kotlinFqName} you have to set `serviceName` manually when more than one service is implemented (${serviceBuilder.serviceNames.joinToString()})"
        }

        property.isFakeOverride = false
        property.origin = IrDeclarationOrigin.DEFINED

        property.backingField = irFactory.buildField {
            name = property.name
            type = irBuiltIns.stringType
            origin = IrDeclarationOrigin.PROPERTY_BACKING_FIELD
            visibility = DescriptorVisibilities.PRIVATE
        }.apply {
            parent = property.parent
            initializer = irFactory.createExpressionBody(irConst(serviceBuilder.serviceNames.first()))
            correspondingPropertySymbol = property.symbol
        }

        transformGetter(transformedClass, property.getter !!, property.backingField !!)
        transformSetter(property.setter !!, property.backingField !!)
    }

    fun transformSetter(func: IrSimpleFunction, field: IrField) {
        func.origin = IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR
        func.isFakeOverride = false

        func.addDispatchReceiver {
            type = transformedClass.defaultType
        }

        func.body = DeclarationIrBuilder(irContext, func.symbol).irBlockBody {
            + IrSetFieldImpl(
                UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                field.symbol,
                irBuiltIns.unitType
            ).also {
                it.receiver = irGet(func.dispatchReceiverParameter !!)
                it.value = irGet(func.valueParameters.first())
            }
        }
    }
}