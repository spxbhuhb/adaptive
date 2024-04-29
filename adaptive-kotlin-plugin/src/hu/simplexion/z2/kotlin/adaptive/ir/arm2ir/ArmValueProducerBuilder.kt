package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmSupportFunctionArgument
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmValueProducer
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