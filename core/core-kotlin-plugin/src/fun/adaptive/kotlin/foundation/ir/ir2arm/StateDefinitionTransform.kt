/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.ir2arm

import `fun`.adaptive.kotlin.common.AbstractIrBuilder
import `fun`.adaptive.kotlin.foundation.ADAPTIVE_STATE_VARIABLE_LIMIT
import `fun`.adaptive.kotlin.foundation.Strings
import `fun`.adaptive.kotlin.foundation.ir.FoundationPluginContext
import `fun`.adaptive.kotlin.foundation.ir.arm.*
import `fun`.adaptive.kotlin.foundation.ir.util.AdaptiveAnnotationBasedExtension
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrParameterKind
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.*


/**
 * Transforms function parameters and top-level variable declarations into
 * state variables.
 */
class StateDefinitionTransform(
    override val pluginContext: FoundationPluginContext,
    private val armClass: ArmClass,
    val skipParameters: Int
) : AdaptiveAnnotationBasedExtension, AbstractIrBuilder {

    val names = mutableListOf<String>()

    // incremented by `register`
    var stateVariableIndex = 0

    fun IrElement.dependencies(): List<ArmStateVariable> {
        // if you modify this you have to modify ProducerTransform as well
        if (this is IrDeclaration && this.hasAnnotation(pluginContext.independentAnnotation)) {
            return emptyList()
        }
        val visitor = DependencyVisitor(armClass.stateVariables, skipLambdas = false)
        accept(visitor, null)
        return visitor.dependencies
    }

    fun transform() {
        addInstructions()
        transformParameters()
        transformStatements()
    }

    fun addInstructions() {
        ArmExternalStateVariable(
            armClass,
            stateVariableIndex,
            stateVariableIndex,
            Strings.INSTRUCTIONS,
            pluginContext.adaptiveInstructionGroupType,
            isInstructions = true,
            null
        ).apply {
            register(null)
        }
    }

    fun transformParameters() {
        var index = 0
        armClass.originalFunction.parameters.forEach { parameter ->
            if (parameter.kind != IrParameterKind.Regular) return@forEach

            // for entry points the first parameter of the function is the adapter which
            // we don't want to add to the root fragment as a state variable
            if (index++ < skipParameters) return@forEach

            // access selector function is not part of the state, it is for the plugin to know
            // which state variable to access
            // TODO add FIR checker to make sure the selector and the binding type arguments are the same

            if (parameter.type.isAccessSelector(parameter, armClass.stateVariables.lastOrNull()?.type)) {
                return@forEach
            }

            // Instructions are added before `transformParameters`. They are the first state variable by definition.

            if (parameter.isInstructions) {
                return@forEach
            }

            ArmExternalStateVariable(
                armClass,
                stateVariableIndex,
                stateVariableIndex,
                parameter.name.identifier,
                parameter.type,
                parameter.isInstructions,
                parameter.symbol
            ).apply {
                register(parameter)

                //        EXPRESSION_BODY
                //          CALL 'public final fun emptyArray <T> (): kotlin.Array<T of kotlin.emptyArray> [inline] declared in kotlin' type=kotlin.Array<`fun`.adaptive.foundation.instruction.AdaptiveInstruction> origin=null
                //            <T>: `fun`.adaptive.foundation.instruction.AdaptiveInstruction


                if (parameter.isVararg && parameter.defaultValue == null) {

                    val call: IrCall =

                        if (parameter.isInstructions) {
                            IrCallImpl(
                                UNDEFINED_OFFSET,
                                UNDEFINED_OFFSET,
                                pluginContext.adaptiveInstructionGroupType,
                                pluginContext.emptyInstructions,
                                typeArgumentsCount = 0
                            )
                        } else {
                            IrCallImpl(
                                UNDEFINED_OFFSET,
                                UNDEFINED_OFFSET,
                                irBuiltIns.arrayClass.typeWith(parameter.varargElementType !!),
                                pluginContext.kotlinSymbols.emptyArray,
                                typeArgumentsCount = 1
                            ).also {
                                it.typeArguments[0] = parameter.varargElementType !!
                            }
                        }

                    armClass.stateDefinitionStatements +=
                        ArmDefaultValueStatement(
                            indexInState,
                            call,
                            emptyList()
                        )

                } else {
                    val defaultValue = parameter.defaultValue ?: return@forEach

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

        val visitor = ProducerTransform(pluginContext, statement, armClass.stateVariables)
        val postProcess = statement.accept(visitor, null)

        if (visitor.producerCall == null) return null

        return ArmValueProducer(
            armClass,
            visitor.producerCall !!,
            visitor.producerDependencies !!,
            postProcess as IrVariable
        )
    }

    fun ArmStateVariable.register(declaration: IrDeclaration?) {

        if (declaration != null) {
            check(stateVariableIndex < ADAPTIVE_STATE_VARIABLE_LIMIT) { "maximum number of state variables is $ADAPTIVE_STATE_VARIABLE_LIMIT (with instructions)" }

            check(declaration.startOffset < armClass.boundary.startOffset) { "declaration in rendering at:\n${declaration.dumpKotlinLike()}\n${declaration.dump()}" }

            check(name !in names) { "variable shadowing is not allowed:\n${declaration.dumpKotlinLike()}\n${declaration.dump()}" }
        }

        stateVariableIndex ++
        names += name
        armClass.stateVariables += this
    }

}
