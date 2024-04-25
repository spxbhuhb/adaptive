/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.services.ir.consumer

import hu.simplexion.z2.kotlin.common.AbstractIrBuilder
import hu.simplexion.z2.kotlin.services.Names
import hu.simplexion.z2.kotlin.services.ir.ServicesPluginContext
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.parentOrNull

/**
 * Transform `getService<T>()` into `getService<T>(T.Consumer())`.
 */
class GetConsumerTransform(
    override val pluginContext: ServicesPluginContext
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    override fun visitCall(expression: IrCall): IrExpression {
        if (expression.symbol == pluginContext.getService) {
            expression.putValueArgument(0, newInstance(checkNotNull(expression.getTypeArgument(0))))
        }

        return super.visitCall(expression)
    }

    fun newInstance(type: IrType): IrExpression {

        val interfaceClassFqName = requireNotNull(type.classFqName)
        val interfaceClassParent = requireNotNull(interfaceClassFqName.parentOrNull())
        val interfaceClassName = interfaceClassFqName.shortName()
        val consumerClassId = ClassId(interfaceClassParent, interfaceClassName).createNestedClassId(Names.CONSUMER)

        val consumerClass = requireNotNull(irContext.referenceClass(consumerClassId)) {
            "missing consumer class (should be generated, this is a plugin error): $consumerClassId"
        }

        return IrConstructorCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            type,
            consumerClass.constructors.first { it.owner.isPrimary },
            0, 0, 0
        )
    }

}