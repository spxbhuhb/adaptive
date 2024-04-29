/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.base.ir.arm2ir

import hu.simplexion.adaptive.kotlin.base.ir.arm.ArmSupportFunctionArgument
import hu.simplexion.adaptive.kotlin.base.ir.arm.ArmValueProducer
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrBranch

open class ArmValueProducerBuilder(
    parent: ClassBoundIrBuilder,
    val producer: ArmValueProducer,
) : ClassBoundIrBuilder(parent), BranchBuilder {

    override fun genInvokeBranches(invokeFun: IrSimpleFunction, supportFunctionIndex: IrVariable, callingFragment: IrVariable, arguments: IrVariable): List<IrBranch> {
        return listOf(
            genInvokeBranch(
                invokeFun,
                supportFunctionIndex,
                callingFragment,
                arguments,
                ArmSupportFunctionArgument(
                    producer.armClass,
                    producer.argumentIndex,
                    producer.supportFunctionIndex,
                    producer.armClass.stateVariables,
                    producer.irExpression.function.returnType,
                    producer.irExpression,
                    producer.dependencies
                )
            )
        )
    }

}