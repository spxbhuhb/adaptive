/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.service.ir.impl

import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import hu.simplexion.adaptive.kotlin.service.ir.ServicesPluginContext
import org.jetbrains.kotlin.backend.common.ir.addDispatchReceiver
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.buildField
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.impl.IrSetFieldImpl
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.kotlinFqName

class ServiceNamePropertyTransform(
    override val pluginContext: ServicesPluginContext,
    val transformedClass: IrClass,
    var property: IrProperty
) : AbstractIrBuilder {

    fun build() {
        if (! property.isFakeOverride) return

        val serviceNames = transformedClass.superTypes.mapNotNull { superType ->
            if (superType.isSubtypeOfClass(pluginContext.serviceClass) && ! superType.isSubtypeOfClass(pluginContext.serviceImplClass)) {
                superType.classFqName !!.asString()
            } else {
                null
            }
        }

        require(serviceNames.isNotEmpty()) { "${transformedClass.kotlinFqName} missing service interface (probably ': Service' is missing)" }
        require(serviceNames.size == 1) {
            "${transformedClass.kotlinFqName} you have to set `serviceName` manually when more than one service is implemented (${serviceNames.joinToString()})"
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
            initializer = irFactory.createExpressionBody(irConst(serviceNames.single()))
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