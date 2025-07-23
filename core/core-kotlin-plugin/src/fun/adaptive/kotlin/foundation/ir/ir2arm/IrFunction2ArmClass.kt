/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.ir2arm

import `fun`.adaptive.kotlin.common.*
import `fun`.adaptive.kotlin.foundation.ADAPTIVE_STATE_VARIABLE_LIMIT
import `fun`.adaptive.kotlin.foundation.FqNames
import `fun`.adaptive.kotlin.foundation.ir.FoundationPluginContext
import `fun`.adaptive.kotlin.foundation.ir.arm.*
import `fun`.adaptive.kotlin.foundation.ir.ir2arm.instruction.InnerInstructionLowering
import `fun`.adaptive.kotlin.foundation.ir.ir2arm.instruction.OuterInstructionLowering
import `fun`.adaptive.kotlin.foundation.ir.util.*
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionReferenceImplWithShape
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.types.makeNullable
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.acceptVoid

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
@OptIn(UnsafeDuringIrConstructionAPI::class)
class IrFunction2ArmClass(
    override val pluginContext: FoundationPluginContext,
    val irFunction: IrFunction,
    val isRoot: Boolean
) : AdaptiveAnnotationBasedExtension, AbstractIrBuilder {

    lateinit var armClass: ArmClass

    var fragmentIndex = 0

    val nextFragmentIndex
        get() = fragmentIndex ++

    val states: Stack<ArmState> = mutableListOf()
    val closures: Stack<ArmClosure> = mutableListOf()

    val closure: ArmClosure
        get() = closures.peek()

    val stringType = pluginContext.irBuiltIns.stringType
    val stringNType = pluginContext.irBuiltIns.stringType.makeNullable()

    fun transform(): ArmClass {
        val boundary = BoundaryVisitor(pluginContext).findBoundary(irFunction)

        armClass = ArmClass(irFunction, boundary, isRoot)

        StateDefinitionTransform(pluginContext, armClass, if (isRoot) 1 else 0).apply { transform() }

        val innerInstructionLowering = InnerInstructionLowering(pluginContext, armClass)
        val outerInstructionLowering = OuterInstructionLowering(pluginContext, armClass)

        val renderingStatements = armClass.originalRenderingStatements
            .onEach { it.acceptVoid(innerInstructionLowering) }
            .map { it.transform(outerInstructionLowering, null) as IrStatement }

        states.push(armClass.stateVariables)
        closures.push(armClass.stateVariables)

        transformBlock(renderingStatements)

        armClass.rendering.sortBy { it.index }

        pluginContext.armClasses += armClass

        return armClass
    }

    fun IrElement.dependencies(skipLambdas: Boolean = false): List<ArmStateVariable> {
        val visitor = DependencyVisitor(closure, skipLambdas)
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

            is IrTypeOperatorCall -> transformStatement(statement.removeCoercionToUnit())

            else -> throw IllegalStateException("invalid rendering statement: ${statement.dumpKotlinLike()}\n${statement.dump()}")
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
                ArmImplicitStateVariable(armClass, 1, closure.size + 1, irNull()),
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
        //              $this: GET_VAR 'val tmp0_iterator: kotlin.collections.IntIterator [val] declared in `fun`.adaptive.kotlin.plugin.successes.Basic' type=kotlin.collections.IntIterator origin=null
        //            body: BLOCK type=kotlin.Unit origin=FOR_LOOP_INNER_WHILE
        //              VAR FOR_LOOP_VARIABLE name:i type:kotlin.Int [val]
        //                CALL 'public final fun next (): kotlin.Int [operator] declared in kotlin.collections.IntIterator' type=kotlin.Int origin=FOR_LOOP_NEXT
        //                  $this: GET_VAR 'val tmp0_iterator: kotlin.collections.IntIterator [val] declared in `fun`.adaptive.kotlin.plugin.successes.Basic' type=kotlin.collections.IntIterator origin=null
        //              BLOCK type=kotlin.Unit origin=null
        //                CALL 'public final fun P1 (p0: kotlin.Int): kotlin.Unit declared in `fun`.adaptive.kotlin.plugin' type=kotlin.Unit origin=null
        //                  p0: GET_VAR 'val i: kotlin.Int [val] declared in `fun`.adaptive.kotlin.plugin.successes.Basic' type=kotlin.Int origin=null

        check(statement.statements.size == 2)

        val irIterator = statement.statements[0]
        val loop = statement.statements[1]

        check(irIterator is IrValueDeclaration && irIterator.origin == IrDeclarationOrigin.FOR_LOOP_ITERATOR)
        check(loop is IrWhileLoop && loop.origin == IrStatementOrigin.FOR_LOOP_INNER_WHILE)

        val body = loop.body

        check(body is IrBlock && body.origin == IrStatementOrigin.FOR_LOOP_INNER_WHILE)
        check(body.statements.size == 2)

        val irLoopVariable = body.statements[0]
        check(irLoopVariable is IrVariable)

        // TODO think for loop check details
        val block = body.statements[1].removeCoercionToUnit()
        check((block is IrBlock && (block.origin == null || block.origin == IrStatementOrigin.WHEN)) || block is IrCall) {
            """
            |not a block in loop
            |    ==== BLOCK ====
            |${block.dump()}
            |    ==== STATEMENT ====
            |${statement.dump()}
            |    ==== STATEMENT KOTLIN-LIKE ====
            |${statement.dumpKotlinLike()}
            """.trimMargin()
        }

        val iterator = transformDeclaration(irIterator)

        val loopIndex = nextFragmentIndex

        val renderingState = listOf(

            ArmImplicitStateVariable(armClass, 0, closure.size, irNull()),

            ArmExternalStateVariable(
                armClass,
                indexInState = 1,
                indexInClosure = closure.size + 1,
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
            irCall.isHydratedCall -> transformHydratedCall(irCall)
            irCall.isDirectAdaptiveCall -> transformDirectCall(irCall)
            irCall.isArgumentAdaptiveCall -> transformArgumentCall(irCall)
            else -> throw IllegalStateException(
                """
                |non-adaptive call in rendering:
                |    ==== SYMBOL ====
                |${irCall.symbol}
                |    ==== DISPATCH RECEIVER ====
                |${(irCall.dispatchReceiver as? IrGetValue)?.symbol?.owner?.dump()}
                |    ==== KOTLIN-LIKE ====
                |${irCall.dumpKotlinLike()}
                |    ==== IR ====
                |${irCall.dump()}
                |    ==== END ====
                """.trimMargin()
            )
        }

    /**
     * The instruction argument is present for all adaptive calls. It is paired with the first state variable
     * which is instructions by definition.
     *
     * irConst(0) is a placeholder which is replaced by an AdaptiveInstructionGroup constructor call
     * or a get object for `emptyInstructions`.
     */
    fun addInstructionsArgument(armCall: ArmCall) {
        val instructions = armClass.instructions[armCall.irCall] ?: emptyList()

        val detachExpressions = mutableListOf<ArmDetachExpression>()
        instructions.forEach { transformDetachExpression(it, detachExpressions) }

        armCall.arguments += ArmValueArgument(
            armClass,
            argumentIndex = 0,
            pluginContext.adaptiveInstructionGroupType,
            irConst(0), // placeholder
            instructions.flatMap { it.dependencies() },
            detachExpressions = detachExpressions,
            isInstructions = true,
            instructions = instructions
        )
    }

    fun transformHydratedCall(irCall: IrCall): ArmRenderingStatement {
        // hydrated calls are always expect calls
        val armCall = ArmCall(armClass, nextFragmentIndex, closure, true, irCall, isExpectCall = true)

        addInstructionsArgument(armCall)

        // transform the model argument

        val model = irCall.firstRegularArgument
        checkNotNull(model) { "invalid model in hydration call (model is null): ${irCall.dumpKotlinLike()}" }

        val argument = transformValueArgument(armCall, model.type, null, model)
        check(argument != null) { "invalid model in hydration call (model after transform is null): ${irCall.dumpKotlinLike()}" }

        armCall.arguments += argument

        // transform vararg (if present)

        val vararg = irCall.secondRegularArgument
        if (vararg == null) {
            return armCall.add()
        }

        check(vararg is IrVararg) { "invalid vararg in hydration call (vararg is null): ${irCall.dumpKotlinLike()}" }

        for ((index, element) in vararg.elements.withIndex()) {
            check(element is IrExpression) { "invalid vararg element in hydration call (element is not IrExpression): ${irCall.dumpKotlinLike()}" }

            if (index == 0) {
                addInstruction(element, armCall)
            } else {
                val argument = transformValueArgument(armCall, element.type, null, element)
                check(argument != null) { "invalid model in hydration call (model after transform is null): ${irCall.dumpKotlinLike()}" }
                armCall.arguments += argument
            }
        }

        return armCall.add()
    }


    fun transformDirectCall(irCall: IrCall): ArmRenderingStatement {
        val armCall = ArmCall(armClass, nextFragmentIndex, closure, true, irCall, irCall.isExpectCall)

        addInstructionsArgument(armCall)

        for (parameter in irCall.symbol.owner.parameters) {
            check(parameter.kind == IrParameterKind.Regular) { "invalid parameter kind: ${parameter.kind} ${irCall.dumpKotlinLike()}" }
            val expression = irCall.arguments[parameter]
            val argument = transformValueArgument(armCall, parameter.type, parameter, expression)
            if (argument != null) armCall.arguments += argument
        }

        return armCall.add()
    }

    /**
     * Transforms calls to functions passed as parameter. In the following example `block()` is the
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
     *     $this: GET_VAR 'builder: kotlin.Function0<kotlin.Unit> declared in `fun`.adaptive.kotlin.base.success.inner' type=kotlin.Function0<kotlin.Unit> origin=VARIABLE_AS_FUNCTION
     * ```
     */
    fun transformArgumentCall(irCall: IrCall): ArmRenderingStatement {
        val armCall = ArmCall(armClass, nextFragmentIndex, closure, false, irCall, false)
        val typeArguments = (irCall.dispatchReceiver !!.type as IrSimpleTypeImpl).arguments

        addInstructionsArgument(armCall)

        for (argumentIndex in 0 until typeArguments.size - 1) {  // skip the return type
            val parameter = (typeArguments[argumentIndex] as IrSimpleTypeImpl)
            val expression = irCall.nthRegularArgument(argumentIndex) ?: continue
            val argument = transformValueArgument(armCall, parameter.type, null, expression)

            if (argument != null) armCall.arguments += argument
        }

        return armCall.add()
    }

    fun transformValueArgument(
        armCall: ArmCall,
        parameterType: IrType,
        parameter: IrValueParameter?,
        expression: IrExpression?
    ): ArmValueArgument? =
        when {
            parameter.isInstructions -> {
                if (expression != null) {
                    transformInstructionsVararg(expression, armCall)
                }
                null
            }

            expression == null -> {
                ArmDefaultValueArgument(
                    armClass,
                    armCall.arguments.size,
                    parameterType,
                    IrConstImpl.defaultValueForType(0, 0, pluginContext.irContext.irBuiltIns.nothingType)
                )
            }

            parameter.isAdaptive -> {
                when (expression) {
                    is IrBlock -> transformFunctionExpressionWrapper(armCall, expression) // block with a local function to provide Unit return type
                    is IrFunctionExpression -> transformFunctionExpression(armCall, expression) // lambda
                    is IrFunctionReference -> transformFunctionReference(armCall, expression) // hard-coded function reference like `::b`
                    is IrCall -> transformFunctionReferenceProperty(armCall, expression) // function reference stored in a property
                    else -> {
                        ArmValueArgument(armClass, armCall.arguments.size, parameterType, expression, expression.dependencies())
                    }
                }
            }

            parameterType.isAccessSelector(parameter, armCall.arguments.lastOrNull()?.type) -> {
                // replaces the binding, which has to be the previous parameter
                transformAccessSelector(armCall, expression)
                // selector is not transformed into a state variable
                null
            }

            else -> {
                ArmValueArgument(armClass, armCall.arguments.size, parameterType, expression, expression.dependencies())
            }
        }

    /**
     * When the function to be called returns with AdaptiveFragment, but the function parameter returns
     * with Unit, the function reference is wrapped in a block (`leftWrapper` is the original function
     * which reference is passed):
     *
     * ```kotlin
     * { // BLOCK
     *   local fun leftWrapper() {
     *     leftWrapper()
     *   }
     *
     *   ::leftWrapper
     * }
     * ```
     */
    fun transformFunctionExpressionWrapper(
        armCall: ArmCall,
        expression: IrBlock
    ): ArmFragmentFactoryArgument {
        val localFun = expression.statements.first() as IrSimpleFunction
        val body = localFun.body as IrBlockBody
        val lastStatement = body.statements.last()

        check(lastStatement is IrCall)

        val reference = IrFunctionReferenceImplWithShape(
            expression.startOffset,
            expression.endOffset,
            pluginContext.kFunctionAdaptiveReferenceType,
            lastStatement.symbol,
            typeArgumentsCount = 0,
            valueArgumentsCount = 2,
            contextParameterCount = 0,
            hasDispatchReceiver = false,
            hasExtensionReceiver = false
        )

        return ArmFragmentFactoryArgument(
            armClass,
            armCall.arguments.size,
            - 1, // renderingStatement.index,
            emptyList(), //renderingStatement.closure,
            pluginContext.kFunctionAdaptiveReferenceType,
            reference,
            expression.dependencies()
        )
    }

    fun transformFunctionExpression(
        armCall: ArmCall,
        expression: IrFunctionExpression
    ): ArmFragmentFactoryArgument {

        // add the anonymous function parameters to the closure
        val innerState = innerState(expression.function)

        val renderingStatement = withClosure(innerState) {
            transformBlock(expression.function.body !!.statements)
        } ?: placeholder(expression.function)

        return ArmFragmentFactoryArgument(
            armClass,
            armCall.arguments.size,
            renderingStatement.index,
            renderingStatement.closure,
            pluginContext.boundFragmentFactoryType,
            expression,
            expression.dependencies()
        )
    }

    fun transformFunctionReference(
        armCall: ArmCall,
        expression: IrFunctionReference
    ): ArmFragmentFactoryArgument {

        return ArmFragmentFactoryArgument(
            armClass,
            armCall.arguments.size,
            - 1, // renderingStatement.index,
            emptyList(), //renderingStatement.closure,
            pluginContext.kFunctionAdaptiveReferenceType,
            expression,
            expression.dependencies()
        )

    }

    fun transformFunctionReferenceProperty(
        armCall: ArmCall,
        expression: IrCall
    ): ArmFragmentFactoryArgument {

        expression.type = pluginContext.adaptiveFunctionType

        return ArmFragmentFactoryArgument(
            armClass,
            armCall.arguments.size,
            - 1,
            emptyList(),
            pluginContext.adaptiveFunctionType,
            expression,
            expression.dependencies()
        )
    }

    private fun innerState(function: IrSimpleFunction): List<ArmStateVariable> {

        var stateVariableIndex = closure.size
        var indexInState = 0

        return listOf(
            ArmImplicitStateVariable(armClass, 0, stateVariableIndex ++, irNull()) // instructions
        ) + function.parameters.mapNotNull { parameter ->
            if (parameter.kind != IrParameterKind.Regular) return@mapNotNull null

            ArmExternalStateVariable(
                armClass,
                indexInState ++ + 1, // +1 for instructions
                stateVariableIndex ++,
                parameter.name.identifier,
                parameter.type,
                parameter.isInstructions,
                parameter.symbol
            )
        }
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

    /**
     * Replace the first argument (instructions by definition) with one that contains the
     * instructions passed in the vararg argument as well.
     */
    fun transformInstructionsVararg(expression: IrExpression, armCall: ArmCall) {
        check(expression is IrVararg)

        val original = armCall.arguments[0]

        // I hope casting to IrExpression is OK here. IrVarargElement is rather general, I don't know why.
        // Order is important here, vararg instructions should be first so precedence rules are applied.

        val allDependencies = expression.dependencies(skipLambdas = true) + original.dependencies
        val allInstructions = expression.elements.map { it as IrExpression } + original.instructions
        val allDetachExpressions = transformDetachExpressions(expression) + original.detachExpressions

        armCall.arguments[0] =

            ArmValueArgument(
                armClass,
                argumentIndex = 0,
                pluginContext.adaptiveInstructionGroupType,
                irConst(0), // placeholder
                allDependencies,
                detachExpressions = allDetachExpressions,
                isInstructions = true,
                instructions = allInstructions
            )
    }

    /**
     * Add the expression to the instructions. Used for hydrated calls where the first element of the
     * vararg is the instructions by definition.
     */
    fun addInstruction(expression: IrExpression, armCall: ArmCall) {

        val original = armCall.arguments[0]

        val allDependencies = expression.dependencies(skipLambdas = true) + original.dependencies
        val allInstructions = listOf(expression) + original.instructions // keep precedence!

        armCall.arguments[0] =

            ArmValueArgument(
                armClass,
                argumentIndex = 0,
                pluginContext.adaptiveInstructionGroupType,
                irConst(0), // placeholder
                allDependencies,
                detachExpressions = original.detachExpressions,
                isInstructions = true,
                instructions = allInstructions
            )
    }

    fun transformDetachExpressions(expression: IrExpression): List<ArmDetachExpression> {
        check(expression is IrVararg)

        val result = mutableListOf<ArmDetachExpression>()

        expression.elements.forEach { element ->
            transformDetachExpression(element as IrExpression, result)
        }

        return result
    }

    fun transformDetachExpression(expression: IrExpression, result: MutableList<ArmDetachExpression>) {
        when (expression) {
            is IrCall -> transformDetach(expression.symbol.owner.parameters, expression, result)
            is IrConstructorCall -> transformDetach(expression.symbol.owner.parameters, expression, result)
        }
    }

    fun transformDetach(valueParameters: List<IrValueParameter>, accessExpression: IrMemberAccessExpression<*>, result: MutableList<ArmDetachExpression>) {
        valueParameters.forEachIndexed { index, parameter ->
            if (parameter.isDetach) {
                result += transformDetach(accessExpression.arguments[parameter])

                val nameIndex = parameter.indexInParameters - 1

                if (nameIndex >= 0 && valueParameters[nameIndex].hasAnnotation(FqNames.DETACH_NAME)) {
                    val nameParam = valueParameters[nameIndex]
                    check(nameParam.type == stringType || nameParam.type == stringNType) { "@DetachName annotation on a non-String parameter" }
                    val nameArg = accessExpression.arguments[nameIndex]
                    if (nameArg != null) return@forEachIndexed

                    // FIXME I'm pretty sure I'm not sure if this is right
                    accessExpression.arguments[nameIndex] =
                        IrConstImpl.string(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, pluginContext.irBuiltIns.stringType, result.last().armCall.target.asString())
                }
            }
        }
    }

    fun transformDetach(expression: IrExpression?): ArmDetachExpression {
        check(expression != null) { "detach: cannot be defaulted" }
        check(expression is IrFunctionExpression) { "detach: non-function expression" }
        check(expression.origin == IrStatementOrigin.LAMBDA) { "detach: non-lambda expression" }

        val body = checkNotNull(expression.function.body) { "detach: missing function body" }
        check(body.statements.size == 1) { "detach: non-single-statement body" }

        val call = body.statements.first().removeCoercionToUnit()
        check(call is IrCall) { "detach: non-call body statement ${call.dumpKotlinLike()}" }
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
            ArmImplicitStateVariable(armClass, 1, closure.size + 1, irNull()),
            ArmImplicitStateVariable(armClass, 2, closure.size + 2, irNull())
        )

        armSelect.branches += statement.branches.mapNotNull { irBranch ->
            val branchResult = irBranch.result

            if (branchResult is IrCallImpl) {
                val owner = branchResult.symbol.owner
                if (owner.name.asString() == "noWhenBranchMatchedException") return@mapNotNull null
            }

            ArmBranch(
                armClass,
                ArmExpression(armClass, irBranch.condition, irBranch.dependencies()),
                withClosure(selectState) { transformStatement(branchResult) } ?: placeholder(branchResult)
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

}