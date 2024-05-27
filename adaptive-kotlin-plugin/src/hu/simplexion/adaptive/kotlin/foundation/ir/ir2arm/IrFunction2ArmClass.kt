/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir.ir2arm

import hu.simplexion.adaptive.kotlin.foundation.ADAPTIVE_STATE_VARIABLE_LIMIT
import hu.simplexion.adaptive.kotlin.foundation.Strings
import hu.simplexion.adaptive.kotlin.foundation.ir.AdaptivePluginContext
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.*
import hu.simplexion.adaptive.kotlin.foundation.ir.util.*
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.*

/**
 * Transforms an original function into a [ArmClass]. This is a somewhat complex transformation.
 *
 * Calls [StateDefinitionTransform] to:
 *
 *   - convert function parameters into [ArmExternalStateVariable] instances
 *   - convert function variables are into [ArmInternalStateVariable] instances
 *
 * Transforms IR structures such as loops, branches and calls into [ArmRenderingStatement] instances.
 * The type of the statement corresponds with the language construct.
 *
 * Calls [DependencyVisitor] to build dependencies for each block.
 */
class IrFunction2ArmClass(
    override val pluginContext: AdaptivePluginContext,
    val irFunction: IrFunction,
    val isRoot: Boolean
) : AdaptiveAnnotationBasedExtension {

    lateinit var armClass: ArmClass

    var fragmentIndex = 0

    val nextFragmentIndex
        get() = fragmentIndex ++

    var supportIndex = 0

    val states: Stack<ArmState> = mutableListOf()
    val closures: Stack<ArmClosure> = mutableListOf()

    val closure: ArmClosure
        get() = closures.peek()

    fun transform(): ArmClass {
        armClass = ArmClass(pluginContext, irFunction, isRoot)

        val definitionTransform = StateDefinitionTransform(pluginContext, armClass, if (isRoot) 1 else 0).apply { transform() }
        supportIndex = definitionTransform.supportFunctionIndex

        states.push(armClass.stateVariables)
        closures.push(armClass.stateVariables)

        returnValue()

        transformBlock(armClass.originalRenderingStatements)

        armClass.rendering.sortBy { it.index }

        pluginContext.armClasses += armClass

        return armClass
    }

    fun IrElement.dependencies(): List<ArmStateVariable> {
        val visitor = DependencyVisitor(closure)
        accept(visitor, null)
        return visitor.dependencies
    }

    fun ArmRenderingStatement.add(): ArmRenderingStatement {
        armClass.rendering += this
        return this
    }

    fun withClosure(state: ArmState, transform: () -> ArmRenderingStatement?): ArmRenderingStatement? {
        states.push(state)
        closures.push(states.flatten())

        check(closure.size < ADAPTIVE_STATE_VARIABLE_LIMIT) { "maximum number of state variables in any closure is $ADAPTIVE_STATE_VARIABLE_LIMIT" }

        val result = transform()

        closures.pop()
        states.pop()

        return result
    }

    fun placeholder(statement: IrStatement) =
        ArmSequence(
            armClass,
            nextFragmentIndex,
            closure,
            statement.startOffset,
            emptyList()
        ).add()

    fun transformStatement(statement: IrStatement): ArmRenderingStatement? =
        when (statement) {

            is IrBlock -> {
                when (statement.origin) {
                    IrStatementOrigin.FOR_LOOP -> transformLoop(statement)
                    IrStatementOrigin.WHEN -> transformWhen(statement)
                    else -> transformBlock(statement.statements)
                }
            }

            is IrCall -> transformCall(statement)

            is IrWhen -> transformWhen(statement)

            is IrReturn -> transformReturn(statement)

            else -> throw IllegalStateException("invalid rendering statement: ${statement.dumpKotlinLike()}")
        }


    // ---------------------------------------------------------------------------
    // Block (may be whatever block: when, if, loop)
    // ---------------------------------------------------------------------------

    fun transformBlock(statements: List<IrStatement>): ArmRenderingStatement? {
        return if (statements.size == 1) {
            transformStatement(statements.first())
        } else {
            val sequenceIndex = nextFragmentIndex

            val sequenceState = listOf(
                ArmImplicitStateVariable(armClass, 0, closure.size, irNull()),
            )

            ArmSequence(
                armClass, sequenceIndex, closure,
                armClass.boundary.startOffset,
                statements.mapNotNull { withClosure(sequenceState) { transformStatement(it) } }
            ).add()
        }
    }

    // ---------------------------------------------------------------------------
    // For Loop
    // ---------------------------------------------------------------------------

    fun transformLoop(statement: IrBlock): ArmRenderingStatement {

        // BLOCK type=kotlin.Unit origin=FOR_LOOP
        //          VAR FOR_LOOP_ITERATOR name:tmp0_iterator type:kotlin.collections.IntIterator [val]
        //            CALL 'public open fun iterator (): kotlin.collections.IntIterator [fake_override,operator] declared in kotlin.ranges.IntRange' type=kotlin.collections.IntIterator origin=FOR_LOOP_ITERATOR
        //              $this: CALL 'public final fun rangeTo (other: kotlin.Int): kotlin.ranges.IntRange [operator] declared in kotlin.Int' type=kotlin.ranges.IntRange origin=RANGE
        //                $this: CONST Int type=kotlin.Int value=0
        //                other: CONST Int type=kotlin.Int value=10
        //          WHILE label=null origin=FOR_LOOP_INNER_WHILE
        //            condition: CALL 'public abstract fun hasNext (): kotlin.Boolean [fake_override,operator] declared in kotlin.collections.IntIterator' type=kotlin.Boolean origin=FOR_LOOP_HAS_NEXT
        //              $this: GET_VAR 'val tmp0_iterator: kotlin.collections.IntIterator [val] declared in hu.simplexion.adaptive.kotlin.plugin.successes.Basic' type=kotlin.collections.IntIterator origin=null
        //            body: BLOCK type=kotlin.Unit origin=FOR_LOOP_INNER_WHILE
        //              VAR FOR_LOOP_VARIABLE name:i type:kotlin.Int [val]
        //                CALL 'public final fun next (): kotlin.Int [operator] declared in kotlin.collections.IntIterator' type=kotlin.Int origin=FOR_LOOP_NEXT
        //                  $this: GET_VAR 'val tmp0_iterator: kotlin.collections.IntIterator [val] declared in hu.simplexion.adaptive.kotlin.plugin.successes.Basic' type=kotlin.collections.IntIterator origin=null
        //              BLOCK type=kotlin.Unit origin=null
        //                CALL 'public final fun P1 (p0: kotlin.Int): kotlin.Unit declared in hu.simplexion.adaptive.kotlin.plugin' type=kotlin.Unit origin=null
        //                  p0: GET_VAR 'val i: kotlin.Int [val] declared in hu.simplexion.adaptive.kotlin.plugin.successes.Basic' type=kotlin.Int origin=null

        check(statement.statements.size == 2)

        val irIterator = statement.statements[0]
        val loop = statement.statements[1]

        check(irIterator is IrValueDeclaration && irIterator.origin == IrDeclarationOrigin.FOR_LOOP_ITERATOR)
        check(loop is IrWhileLoop && loop.origin == IrStatementOrigin.FOR_LOOP_INNER_WHILE)

        val body = loop.body

        check(body is IrBlock && body.origin == IrStatementOrigin.FOR_LOOP_INNER_WHILE)
        check(body.statements.size == 2)

        val irLoopVariable = body.statements[0]
        val block = body.statements[1]

        check(irLoopVariable is IrVariable)
        check((block is IrBlock && block.origin == null) || block is IrCall) // TODO think for loop check details

        val iterator = transformDeclaration(irIterator)

        val loopIndex = nextFragmentIndex

        val renderingState = listOf(
            ArmExternalStateVariable(
                armClass,
                indexInState = 0,
                indexInClosure = closure.size,
                name = irLoopVariable.name.identifier,
                type = irLoopVariable.type,
                isInstructions = false,
                symbol = irLoopVariable.symbol
            )
        )

        val rendering = withClosure(renderingState) {
            transformStatement(block)
        } ?: placeholder(block)

        return ArmLoop(
            armClass,
            loopIndex,
            closure,
            statement.startOffset,
            iterator,
            rendering
        ).add()
    }

    fun transformDeclaration(declaration: IrDeclaration): ArmDeclaration =
        when (declaration) {
            is IrValueDeclaration -> ArmDeclaration(armClass, declaration, declaration.dependencies())
            else -> throw IllegalStateException("invalid declaration in rendering: ${declaration.dumpKotlinLike()}")
        }

    // ---------------------------------------------------------------------------
    // Call
    // ---------------------------------------------------------------------------

    fun transformCall(irCall: IrCall): ArmRenderingStatement? =
        when {
            irCall.isDirectAdaptiveCall -> transformDirectCall(irCall)
            irCall.isArgumentAdaptiveCall -> transformArgumentCall(irCall)
            irCall.isTransformInterfaceCall -> transformTransformCallChain(irCall)
            else -> throw IllegalStateException("non-adaptive call in rendering: ${irCall.dumpKotlinLike()}")
        }

    fun transformDirectCall(irCall: IrCall): ArmRenderingStatement {
        val armCall = ArmCall(armClass, nextFragmentIndex, closure, true, irCall, irCall.isExpectCall)
        val valueParameters = irCall.symbol.owner.valueParameters

        for (argumentIndex in 0 until irCall.valueArgumentsCount) {
            val parameter = valueParameters[argumentIndex]
            val expression = irCall.getValueArgument(argumentIndex)
            val argument = transformValueArgument(armCall, parameter.type, parameter, expression)
            if (argument != null) armCall.arguments += argument
        }

        if (armCall.arguments.any { it is ArmSupportFunctionArgument && ! it.isSuspend }) {
            armClass.hasInvokeBranch = true
            armCall.hasInvokeBranch = true
        }

        if (armCall.arguments.any { it is ArmSupportFunctionArgument && it.isSuspend }) {
            armClass.hasInvokeSuspendBranch = true
            armCall.hasInvokeSuspendBranch = true
        }

        return armCall.add()
    }

    /**
     * Transforms calls to functions passes as parameter. In the following example `block()` is the
     * argument call. In this case the IR contains a call to `kotlin.FunctionX.invoke` and
     * the parameter types are the type arguments of `invoke`.
     *
     * ```kotlin
     * @Adaptive
     * fun someFun(@Adaptive block : () -> Unit) {
     *     block()
     * }
     *
     * ```text
     * CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
     *     $this: GET_VAR 'builder: kotlin.Function0<kotlin.Unit> declared in hu.simplexion.adaptive.kotlin.base.success.inner' type=kotlin.Function0<kotlin.Unit> origin=VARIABLE_AS_FUNCTION
     * ```
     */
    fun transformArgumentCall(irCall: IrCall): ArmRenderingStatement {
        val armCall = ArmCall(armClass, nextFragmentIndex, closure, false, irCall, false)
        val arguments = (irCall.dispatchReceiver !!.type as IrSimpleTypeImpl).arguments

        for (argumentIndex in 0 until arguments.size - 1) {  // skip the return type
            val parameter = (arguments[argumentIndex] as IrSimpleTypeImpl)
            val expression = irCall.getValueArgument(argumentIndex) ?: continue
            val argument = transformValueArgument(armCall, parameter.type, null, expression)

            if (argument != null) armCall.arguments += argument
        }

        // FIXME do we need invoke branch for argument call?
        armCall.hasInvokeBranch = armCall.arguments.any { it is ArmSupportFunctionArgument && ! it.isSuspend }
        armCall.hasInvokeSuspendBranch = armCall.arguments.any { it is ArmSupportFunctionArgument && it.isSuspend }

        return armCall.add()
    }

    fun transformValueArgument(
        armCall: ArmCall,
        parameterType: IrType,
        parameter: IrValueParameter?,
        expression: IrExpression?
    ): ArmValueArgument? =
        when {
            expression == null -> {
                ArmDefaultValueArgument(
                    armClass,
                    armCall.arguments.size,
                    parameterType,
                    IrConstImpl.defaultValueForType(0, 0, pluginContext.irContext.irBuiltIns.nothingType)
                )
            }

            parameter.isAdaptive -> {
                if (expression is IrFunctionExpression) {
                    val renderingStatement = transformFragmentFactoryArgument(expression)

                    ArmFragmentFactoryArgument(
                        armClass,
                        armCall.arguments.size,
                        renderingStatement.index,
                        renderingStatement.closure,
                        parameterType,
                        expression,
                        expression.dependencies()
                    )
                } else {
                    ArmValueArgument(armClass, armCall.arguments.size, parameterType, expression, expression.dependencies())
                }
            }

            parameterType.isAccessSelector(armCall.arguments.lastOrNull()?.type) -> {
                // replaces the binding, which has to be the previous parameter
                transformAccessSelector(armCall, expression)
                // selector is not transformed into a state variable
                null
            }

            parameterType.isFunction() || parameterType.isSuspendFunction() -> {
                if (expression is IrFunctionExpression) {
                    ArmSupportFunctionArgument(
                        armClass,
                        armCall.arguments.size,
                        supportIndex ++,
                        closure,
                        parameterType,
                        expression,
                        expression.dependencies()
                    )
                } else {
                    ArmValueArgument(armClass, armCall.arguments.size, parameterType, expression, expression.dependencies())
                }
            }

            parameter.isInstructions -> {
                val detachExpressions = transformDetachExpressions(expression)
                ArmValueArgument(armClass, armCall.arguments.size, parameterType, expression, expression.dependencies(), detachExpressions)
            }

            else -> {
                ArmValueArgument(armClass, armCall.arguments.size, parameterType, expression, expression.dependencies())
            }
        }

    fun transformFragmentFactoryArgument(
        expression: IrFunctionExpression
    ): ArmRenderingStatement {
        // add the anonymous function parameters to the closure

        var stateVariableIndex = closure.size

        val innerState = expression.function.valueParameters.mapIndexed { indexInState, parameter ->
            ArmExternalStateVariable(
                armClass,
                indexInState,
                stateVariableIndex ++,
                parameter.name.identifier,
                parameter.type,
                parameter.isInstructions,
                parameter.symbol
            )
        }

        return withClosure(innerState) {
            transformBlock(expression.function.body !!.statements)
        } ?: placeholder(expression.function)
    }

    private fun transformAccessSelector(armCall: ArmCall, expression: IrExpression) {
        check(expression is IrFunctionExpression)

        val path = flattenAccessorPath(expression)
        val stateVariableName = path.last()

        val indexInClosure = armCall.closure.indexOfFirst { it.name == stateVariableName }
        val state = states.first { state -> state.indexOfFirst { it.name == stateVariableName } != - 1 }
        val indexInState = state.indexOfFirst { it.name == stateVariableName }

        val argument = ArmStateVariableBindingArgument(
            armClass,
            armCall.arguments.size - 1,
            indexInState,
            indexInClosure,
            expression.function.returnType,
            - 1,
            path.dropLast(1),
            pluginContext.adaptiveStateVariableBindingClass.defaultType,
            expression,
            expression.dependencies()
        )

        // replace the last argument which is a value argument for the binding
        armCall.arguments[armCall.arguments.size - 1] = argument
    }

    /**
     * Flatten a get chain for accessors. For example: `editor { a.b.c }`.
     * These should be like this in IR:
     *
     * ```text
     * irReturn              -- return value of the lambda
     *   irCall              -- property access 1
     *     ...
     *       irCall          -- property access N
     *         irGetValue    -- the starting variable access
     * ```
     */
    fun flattenAccessorPath(expression: IrExpression): List<String> {
        check(expression is IrFunctionExpression)

        val body = checkNotNull(expression.function.body) { "missing function body: ${expression.dump()}" }
        var current: IrStatement? = checkNotNull(body.statements.firstOrNull()) { "empty body: ${expression.dump()}" }

        val result = mutableListOf<String>()

        while (current != null) {
            when (current) {
                is IrGetValue -> {
                    check(current.symbol.owner.parent == armClass.originalFunction) { "invalid access: ${expression.dump()}" }
                    result += current.symbol.owner.name.identifier
                    current = null
                }

                is IrCall -> {
                    val symbol = current.symbol.owner.correspondingPropertySymbol
                    checkNotNull(symbol) { "not a property access (1): ${expression.dump()}" }
                    result += symbol.owner.name.identifier
                    current = current.dispatchReceiver
                }

                is IrReturn -> {
                    current = current.value
                }

                else -> {
                    throw IllegalStateException("unknown access: ${expression.dump()}")
                }
            }
        }

        check(result.isNotEmpty()) { "not a property access (2): ${expression.dump()}" }

        return result
    }

    fun transformTransformCallChain(irCall: IrCall): ArmRenderingStatement? {
        return flattenTransformCalls(irCall)
    }

    /**
     * Flatten a call chain for transforms. For example: `editor { a } readOnly true validate { }`
     *
     * These should be like this in IR:
     *
     * ```text
     * irCall                -- validate
     *   irCall              -- readOnly
     *     irCall            -- editor
     * ```
     *
     * The dispatch receiver of all calls but the last must be an `AdaptiveTransformSubject`
     */
    fun flattenTransformCalls(expression: IrExpression): ArmRenderingStatement? {
        check(expression is IrCall)

        val transforms = mutableListOf(transformTransformCall(expression))

        var current: IrCall? = expression

        while (current != null) {

            val receiver = current.dispatchReceiver
            check(receiver is IrCall) { "invalid transform chain: ${expression.dumpKotlinLike()}" }

            when {
                receiver.isDirectAdaptiveCall || receiver.isArgumentAdaptiveCall -> {
                    return transformCall(receiver).also {
                        checkNotNull(it) { "illegal transform chain: ${expression.dumpKotlinLike()}" }
                            .transforms = transforms
                    }
                }

                receiver.isTransformInterfaceCall -> {
                    transforms += transformTransformCall(receiver)
                    current = receiver
                }

                else -> {
                    error { "invalid transform chain: ${expression.dumpKotlinLike()}" }
                }
            }
        }

        error { "invalid transform chain: ${expression.dumpKotlinLike()}" }
    }

    fun transformTransformCall(irCall: IrCall): ArmTransformCall {
        return ArmTransformCall(irCall)
    }

    fun transformDetachExpressions(expression: IrExpression) : List<ArmDetachExpression> {
        check(expression is IrVararg)

        val result = mutableListOf<ArmDetachExpression>()

        expression.elements.forEachIndexed { index, element ->
            when (element) {
                is IrCall -> {
                    for (parameter in element.symbol.owner.valueParameters) {
                        if (parameter.isDetach) {
                            result +=transformDetach(element.getValueArgument(parameter.index))
                        }
                    }
                }

                is IrConstructorCall -> {
                    for (parameter in element.symbol.owner.valueParameters) {
                        if (parameter.isDetach) {
                            result +=transformDetach(element.getValueArgument(parameter.index))
                        }
                    }
                }
            }

        }

        return result
    }

    fun transformDetach(expression: IrExpression?) : ArmDetachExpression {
        check(expression != null) { "detach: cannot be defaulted" }
        check(expression is IrFunctionExpression) { "detach: non-function expression" }
        check(expression.origin == IrStatementOrigin.LAMBDA) { "detach: non-lambda expression" }

        val body = checkNotNull(expression.function.body) { "detach: missing function body" }
        check(body.statements.size == 1) { "detach: non-single-statement body" }

        val call = body.statements.first()
        check(call is IrCall) { "detach: non-call body statement" }
        check(call.isDirectAdaptiveCall) { "detach: not a direct adaptive call" }

        val armCall = transformDirectCall(call) as ArmCall

        return ArmDetachExpression(expression, armCall)
    }

    // ---------------------------------------------------------------------------
    // When
    // ---------------------------------------------------------------------------

    /**
     * Transforms a `when` with a subject variable like:
     *
     * ```kotlin
     * when (b) {
     *   // ...
     * }
     * ```
     */
    fun transformWhen(statement: IrBlock): ArmRenderingStatement {
        // TODO convert checks into non-exception throwing, but contracting functions
        check(statement.statements.size == 2)

        val subject = statement.statements[0]
        val evaluation = statement.statements[1]

        check(subject is IrVariable)
        check(evaluation is IrWhen && evaluation.origin == IrStatementOrigin.WHEN)

        return transformWhen(evaluation, subject)
    }

    fun transformWhen(statement: IrWhen, subject: IrVariable? = null): ArmRenderingStatement {

        val armSelect = ArmSelect(
            armClass,
            nextFragmentIndex,
            closure,
            statement.startOffset,
            subject?.symbol,
            subject?.let { ArmExpression(armClass, it.initializer !!, it.initializer !!.dependencies()) }
        )

        val selectState = listOf(
            ArmImplicitStateVariable(armClass, 0, closure.size, irNull()),
            ArmImplicitStateVariable(armClass, 1, closure.size + 1, irNull())
        )

        armSelect.branches += statement.branches.map { irBranch ->
            ArmBranch(
                armClass,
                ArmExpression(armClass, irBranch.condition, irBranch.dependencies()),
                withClosure(selectState) { transformStatement(irBranch.result) } ?: placeholder(irBranch.result)
            )
        }

        return armSelect.add()
    }

    // ---------------------------------------------------------------------------
    // Return
    // ---------------------------------------------------------------------------

    fun transformReturn(statement: IrStatement): ArmRenderingStatement? {
        check(statement is IrReturn)
        if (statement.type == pluginContext.irBuiltIns.unitType) return placeholder(statement)
        return null
    }

    fun returnValue() {
        val type = irFunction.returnType

        if (type == pluginContext.irContext.irBuiltIns.unitType) return

        val classSymbol = checkNotNull(type.classOrNull) { "missing class: $type in ${irFunction.name}" }

        check(type.isSubtypeOfClass(pluginContext.adaptiveTransformInterfaceClass)) { "return type is not subclass of ${Strings.ADAPTIVE_TRANSFORM_INTERFACE} in ${irFunction.name}" }
        check(classSymbol.owner.isInterface) { "$type is not an interface" }

        armClass.stateInterface = classSymbol
    }

    // ---------------------------------------------------------------------------
    // Utility
    // ---------------------------------------------------------------------------

    fun irNull() = IrConstImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        pluginContext.irBuiltIns.anyNType,
        IrConstKind.Null,
        null
    )
}