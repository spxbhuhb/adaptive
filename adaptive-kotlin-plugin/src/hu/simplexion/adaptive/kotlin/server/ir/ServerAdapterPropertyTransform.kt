/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.server.ir

import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import hu.simplexion.adaptive.kotlin.common.property
import hu.simplexion.adaptive.kotlin.server.Names
import org.jetbrains.kotlin.backend.common.ir.addDispatchReceiver
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.buildField
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.impl.IrSetFieldImpl
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.kotlinFqName

class ServerAdapterPropertyTransform(
    override val pluginContext: ServerPluginContext,
    var irClass: IrClass
) : AbstractIrBuilder {

    val property = irClass.property(Names.SERVER_ADAPTER_PROPERTY)

    fun transform() {
        if (! property.isFakeOverride) return

        property.isFakeOverride = false
        property.origin = IrDeclarationOrigin.DEFINED

        property.backingField = irFactory.buildField {
            name = property.name
            type = pluginContext.serverAdapterType
            origin = IrDeclarationOrigin.PROPERTY_BACKING_FIELD
            visibility = DescriptorVisibilities.PRIVATE
        }.apply {
            parent = property.parent
            initializer = irFactory.createExpressionBody(irNull())
            correspondingPropertySymbol = property.symbol
        }

        transformGetter(irClass, property.getter !!, property.backingField !!)
        transformSetter(property.setter !!, property.backingField !!)
    }

    fun transformSetter(func: IrSimpleFunction, field: IrField) {
        func.origin = IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR
        func.isFakeOverride = false

        func.addDispatchReceiver {
            type = irClass.defaultType
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