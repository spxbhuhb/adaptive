/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir.ir2arm

import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import hu.simplexion.adaptive.kotlin.foundation.ADAPTIVE_STATE_VARIABLE_LIMIT
import hu.simplexion.adaptive.kotlin.foundation.ir.FoundationPluginContext
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.*
import hu.simplexion.adaptive.kotlin.foundation.ir.util.AdaptiveAnnotationBasedExtension
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.deepCopyWithoutPatchingParents
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.dumpKotlinLike
import org.jetbrains.kotlin.ir.util.isVararg


/**
 * Transforms function parameters and top-level variable declarations into
 * state variables.
 */
class StateDefinitionTransform(
    override val pluginContext: FoundationPluginContext,
    private val armClass: ArmClass,
    val skipParameters : Int
) : AdaptiveAnnotationBasedExtension, AbstractIrBuilder {

    val names = mutableListOf<String>()

    // incremented by `register`
    var stateVariableIndex = 0

    fun IrElement.dependencies(): List<ArmStateVariable> {
        val visitor = DependencyVisitor(armClass.stateVariables, skipLambdas = true)
        accept(visitor, null)
        return visitor.dependencies
    }

    fun transform() {
        transformParameters()
        transformStatements()
    }

    fun transformParameters() {
        armClass.originalFunction.valueParameters.forEach { valueParameter ->

            // for entry points the first parameter of the function is the adapter which
            // we don't want to add to the root fragment as a state variable
            if (valueParameter.index < skipParameters) return@forEach

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
                valueParameter.isInstructions,
                valueParameter.symbol
            ).apply {
                register(valueParameter)

                //        EXPRESSION_BODY
                //          CALL 'public final fun emptyArray <T> (): kotlin.Array<T of kotlin.emptyArray> [inline] declared in kotlin' type=kotlin.Array<hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction> origin=null
                //            <T>: hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction


                if (valueParameter.isVararg && valueParameter.defaultValue == null) {
                    val call = IrCallImpl(
                        UNDEFINED_OFFSET,
                        UNDEFINED_OFFSET,
                        irBuiltIns.arrayClass.typeWith(valueParameter.varargElementType !!),
                        pluginContext.kotlinSymbols.emptyArray,
                        1, 0
                    ).also {
                        it.putTypeArgument(0, valueParameter.varargElementType !!)
                    }

                    armClass.stateDefinitionStatements +=
                        ArmDefaultValueStatement(
                            indexInState,
                            call,
                            emptyList()
                        )

                } else {
                    val defaultValue = valueParameter.defaultValue ?: return@forEach

                    armClass.stateDefinitionStatements +=
                        ArmDefaultValueStatement(
                            indexInState,
                            defaultValue.expression.deepCopyWithoutPatchingParents(),
                            defaultValue.expression.dependencies()
                        )
                }

            }
        }
    }

    fun transformStatements() {
        armClass.originalStatements.forEach { statement ->
            when {
                statement is IrVariable -> {
                    val producer = transformProducer(statement)

                    armClass.stateDefinitionStatements +=

                        ArmInternalStateVariable(
                            armClass,
                            stateVariableIndex,
                            stateVariableIndex,
                            producer?.postProcess ?: statement,
                            producer,
                            producer?.postProcess?.dependencies() ?: statement.dependencies() // check ProducerTransform also
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

    private fun transformProducer(statement: IrVariable): ArmValueProducer? {

        val visitor = ProducerTransform(pluginContext, armClass.stateVariables)
        val postProcess = statement.accept(visitor, null)

        if (visitor.producerCall == null) return null

        return ArmValueProducer(
            armClass,
            visitor.producerCall!!,
            visitor.producerDependencies!!,
            postProcess as IrVariable
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
