/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.ir.ir2arm

import hu.simplexion.adaptive.kotlin.base.ADAPTIVE_STATE_VARIABLE_LIMIT
import hu.simplexion.adaptive.kotlin.base.ClassIds
import hu.simplexion.adaptive.kotlin.base.ir.AdaptivePluginContext
import hu.simplexion.adaptive.kotlin.base.ir.arm.*
import hu.simplexion.adaptive.kotlin.base.ir.util.AdaptiveNonAnnotationBasedExtension
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.deepCopyWithVariables
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.types.isClassType
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.dumpKotlinLike
import org.jetbrains.kotlin.ir.util.isFunction
import org.jetbrains.kotlin.ir.util.isSuspendFunction


/**
 * Transforms function parameters and top-level variable declarations into
 * state variables.
 */
class StateDefinitionTransform(
    override val adaptiveContext: AdaptivePluginContext,
    private val armClass: ArmClass
) : AdaptiveNonAnnotationBasedExtension {

    val names = mutableListOf<String>()

    // incremented by `register`
    var stateVariableIndex = 0

    var supportFunctionIndex = 0

    fun IrElement.dependencies(): List<ArmStateVariable> {
        val visitor = DependencyVisitor(armClass.stateVariables)
        accept(visitor, null)
        return visitor.dependencies
    }

    fun transform() {
        transformParameters()
        transformStatements()
    }

    fun transformParameters() {
        armClass.originalFunction.valueParameters.forEach { valueParameter ->

            // access selector function is not part of the state, it is for the plugin to know
            // which state variable to access
            // TODO add FIR checker to make sure the selector and the binding type arguments are the same

            if (valueParameter.type.isAccessSelector(armClass.stateVariables.lastOrNull()?.type)) return@forEach

            ArmExternalStateVariable(
                armClass,
                stateVariableIndex,
                stateVariableIndex,
                valueParameter.name.identifier,
                valueParameter.type,
                valueParameter.symbol
            ).apply {
                register(valueParameter)

                val defaultValue = valueParameter.defaultValue ?: return@forEach

                armClass.stateDefinitionStatements +=
                    ArmDefaultValueStatement(
                        indexInState,
                        defaultValue.expression.deepCopyWithVariables(),
                        defaultValue.expression.dependencies()
                    )
            }
        }
    }

    fun transformStatements() {
        armClass.originalStatements.forEach { statement ->
            when {
                statement is IrVariable -> {
                    armClass.stateDefinitionStatements +=

                        ArmInternalStateVariable(
                            armClass,
                            stateVariableIndex,
                            stateVariableIndex,
                            statement,
                            getValueProducer(statement),
                            statement.dependencies()
                        ).apply {
                            register(statement)
                        }
                }

                statement.startOffset < armClass.boundary.startOffset -> {
                    armClass.stateDefinitionStatements += ArmStateDefinitionStatement(statement, statement.dependencies())
                }

                else -> {
                    armClass.originalRenderingStatements += statement
                }
            }
        }
    }

    private fun getValueProducer(statement: IrVariable): ArmValueProducer? {
        val originalInitializer = statement.initializer!!

        if (originalInitializer !is IrCall) return null

        val calledFun = originalInitializer.symbol.owner
        val parameterCount = calledFun.valueParameters.size

        if (parameterCount < 2) return null

        val binding = calledFun.valueParameters[parameterCount - 2]
        val function = calledFun.valueParameters[parameterCount - 1]

        if (!binding.type.isClassType(ClassIds.ADAPTIVE_STATE_VARIABLE_BINDING.asSingleFqName().toUnsafe(), true)) return null
        if (!function.type.isFunction() && !function.type.isSuspendFunction()) return null

        val supportFunction = originalInitializer.getValueArgument(parameterCount - 1)!!
        if (supportFunction !is IrFunctionExpression) return null

        if (function.type.isSuspendFunction()) {
            armClass.hasInvokeSuspendBranch = true
        } else {
            armClass.hasInvokeBranch = true
        }

        return ArmValueProducer(
            armClass,
            parameterCount - 1,
            supportFunctionIndex++,
            supportFunction,
            supportFunction.dependencies()
        )
    }

    fun ArmStateVariable.register(declaration: IrDeclaration) {

        check(stateVariableIndex < ADAPTIVE_STATE_VARIABLE_LIMIT) { "maximum number of state variables is $ADAPTIVE_STATE_VARIABLE_LIMIT" }

        check(declaration.startOffset < armClass.boundary.startOffset) { "declaration in rendering at:\n${declaration.dumpKotlinLike()}\n${declaration.dump()}" }

        check(name !in names) { "variable shadowing is not allowed:\n${declaration.dumpKotlinLike()}\n${declaration.dump()}" }

        stateVariableIndex++
        names += name
        armClass.stateVariables += this
    }

}
