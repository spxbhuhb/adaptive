package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.Names
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.arm.*
import hu.simplexion.z2.kotlin.util.property
import org.jetbrains.kotlin.backend.common.ir.addDispatchReceiver
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.*
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irSetField
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.transformStatement
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.makeNullable
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.util.OperatorNameConventions

open class ClassBoundIrBuilder(
    val pluginContext: AdaptivePluginContext
) {

    constructor(parent: ClassBoundIrBuilder) : this(parent.pluginContext) {
        this.irClass = parent.irClass
    }

    lateinit var irClass: IrClass

    val irContext
        get() = pluginContext.irContext

    val irFactory
        get() = irContext.irFactory

    val irBuiltIns
        get() = irContext.irBuiltIns

    val classBoundBridgeType: IrTypeParameter
        get() = irClass.typeParameters.first()

    val classBoundFragmentType: IrType
        get() = pluginContext.adaptiveFragmentClass.typeWith(classBoundBridgeType.defaultType)

    val classBoundNullableFragmentType: IrType
        get() = classBoundFragmentType.makeNullable()

    val classBoundClosureType: IrType
        get() = pluginContext.adaptiveClosureClass.typeWith(classBoundBridgeType.defaultType)

    val classBoundSupportFunctionType: IrType
        get() = pluginContext.adaptiveSupportFunctionClass.typeWith(classBoundBridgeType.defaultType)

    val classBoundFragmentFactoryType: IrType
        get() = pluginContext.adaptiveFragmentFactoryClass.typeWith(classBoundBridgeType.defaultType)

    val classBoundAdapterType: IrType
        get() = pluginContext.adaptiveAdapterClass.typeWith(classBoundBridgeType.defaultType)

    // --------------------------------------------------------------------------------------------------------
    // Build, patch and invoke helpers
    // --------------------------------------------------------------------------------------------------------

    fun irConstructorCallFromBuild(
        buildFun: IrSimpleFunction,
        target: FqName,
        classSymbol: IrClassSymbol = pluginContext.irClasses[target]?.symbol ?: pluginContext.classSymbol(target),
        argumentCount: Int = Indices.ADAPTIVE_GENERATED_FRAGMENT_ARGUMENT_COUNT
    ): IrConstructorCallImpl {

        val constructorCall =
            IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                classSymbol.defaultType,
                classSymbol.constructors.single(),
                typeArgumentsCount = 1, // bridge type
                constructorTypeArgumentsCount = 0,
                argumentCount
            )

        constructorCall.putTypeArgument(Indices.ADAPTIVE_FRAGMENT_TYPE_INDEX_BRIDGE, classBoundBridgeType.defaultType)

        constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_ADAPTER, irGetValue(irClass.property(Names.ADAPTER), irGet(buildFun.dispatchReceiverParameter !!)))
        constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_PARENT, irGet(buildFun.valueParameters[Indices.BUILD_PARENT]))
        constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_INDEX, irGet(buildFun.valueParameters[Indices.BUILD_DECLARATION_INDEX]))

        return constructorCall
    }

    fun irFragmentFactoryFromPatch(patchFun: IrSimpleFunction, index: Int): IrExpression {
        val constructorCall =
            IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                classBoundFragmentType,
                pluginContext.adaptiveFragmentFactoryConstructor,
                typeArgumentsCount = 1, // bridge type
                constructorTypeArgumentsCount = 0,
                Indices.ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_COUNT
            )

        constructorCall.putTypeArgument(Indices.ADAPTIVE_FRAGMENT_TYPE_INDEX_BRIDGE, classBoundBridgeType.defaultType)

        constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_DECLARING_FRAGMENT, irGet(patchFun.dispatchReceiverParameter !!))
        constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_DECLARATION_INDEX, irConst(index))

        return constructorCall
    }

    fun irSetDescendantStateVariable(patchFun: IrSimpleFunction, stateVariableIndex: Int, value: IrExpression) =
        IrCallImpl(
            SYNTHETIC_OFFSET,
            SYNTHETIC_OFFSET,
            irBuiltIns.unitType,
            pluginContext.setStateVariable,
            typeArgumentsCount = 0,
            valueArgumentsCount = Indices.SET_STATE_VARIABLE_ARGUMENT_COUNT
        ).also { call ->

            call.dispatchReceiver = irGet(patchFun.valueParameters.first())

            call.putValueArgument(
                Indices.SET_STATE_VARIABLE_INDEX,
                irConst(stateVariableIndex)
            )

            call.putValueArgument(
                Indices.SET_STATE_VARIABLE_VALUE,
                value
            )
        }

    fun irSetStateVariable(patchFun: IrSimpleFunction, stateVariableIndex: Int, value: IrExpression) =
        IrCallImpl(
            SYNTHETIC_OFFSET,
            SYNTHETIC_OFFSET,
            irBuiltIns.unitType,
            pluginContext.setStateVariable,
            typeArgumentsCount = 0,
            valueArgumentsCount = Indices.SET_STATE_VARIABLE_ARGUMENT_COUNT
        ).also { call ->

            call.dispatchReceiver = irGet(patchFun.dispatchReceiverParameter !!)

            call.putValueArgument(
                Indices.SET_STATE_VARIABLE_INDEX,
                irConst(stateVariableIndex)
            )

            call.putValueArgument(
                Indices.SET_STATE_VARIABLE_VALUE,
                value
            )
        }

    fun irGetThisStateVariable(patchFun: IrSimpleFunction, stateVariableIndex: Int) =
        IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.anyNType,
            pluginContext.getThisClosureVariable,
            0,
            Indices.GET_CLOSURE_VARIABLE_ARGUMENT_COUNT
        ).also {

            it.dispatchReceiver = irGet(patchFun.dispatchReceiverParameter !!)

            it.putValueArgument(
                Indices.GET_CLOSURE_VARIABLE_INDEX,
                irConst(stateVariableIndex)
            )
        }

    fun genInvokeBranch(
        invokeFun: IrSimpleFunction,
        supportFunctionIndex: IrVariable,
        callingFragment: IrVariable,
        arguments: IrVariable,
        armSupportFunctionArgument: ArmSupportFunctionArgument
    ): IrBranch =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irEqual(
                irGet(supportFunctionIndex),
                irConst(armSupportFunctionArgument.supportFunctionIndex)
            ),
            genInvokeBranchBody(invokeFun, callingFragment, arguments, armSupportFunctionArgument)
        )

    private fun genInvokeBranchBody(
        invokeFun: IrSimpleFunction,
        callingFragment: IrVariable,
        arguments: IrVariable,
        armSupportFunctionArgument: ArmSupportFunctionArgument
    ): IrExpression {
        val functionToTransform = (armSupportFunctionArgument.irExpression as IrFunctionExpression).function
        val originalClosure = armSupportFunctionArgument.supportFunctionClosure

        val transformClosure =
            originalClosure + functionToTransform.valueParameters.mapIndexed { indexInState, parameter ->
                ArmSupportStateVariable(
                    armSupportFunctionArgument.armClass,
                    indexInState,
                    originalClosure.size + indexInState,
                    parameter
                )
            }

        return IrBlockImpl(
            functionToTransform.startOffset,
            functionToTransform.endOffset,
            functionToTransform.returnType
        ).apply {
            val transform = SupportFunctionTransform(this@ClassBoundIrBuilder, transformClosure, { irGet(invokeFun.dispatchReceiverParameter !!) }, callingFragment, arguments)

            functionToTransform.body !!.statements.forEach {
                statements += it.transformStatement(transform)
            }
        }
    }

    fun IrExpression.transformCreateStateAccess(closure: ArmClosure, irGetFragment: () -> IrExpression): IrExpression =
        transform(StateAccessTransform(this@ClassBoundIrBuilder, closure, pluginContext.getCreateClosureVariable, false, irGetFragment), null)

    fun IrStatement.transformThisStateAccess(closure: ArmClosure, transformInvoke: Boolean = true, irGetFragment: () -> IrExpression): IrExpression =
        transform(StateAccessTransform(this@ClassBoundIrBuilder, closure, pluginContext.getThisClosureVariable, transformInvoke, irGetFragment), null) as IrExpression

    fun ArmDependencies.toDirtyMask(): IrExpression {
        var mask = 0
        this.forEach { mask = mask or (1 shl it.indexInClosure) }
        return irConst(mask)
    }

    fun stateVariableType(variable: ArmStateVariable): IrType =
        if (variable.type.isFunction()) classBoundSupportFunctionType else variable.type

    // --------------------------------------------------------------------------------------------------------
    // Properties
    // --------------------------------------------------------------------------------------------------------

    /**
     * Adds a property to [irClass].
     */
    fun addIrProperty(
        inName: Name,
        inType: IrType,
        inIsVar: Boolean,
        inInitializer: IrExpression? = null,
        overridden: List<IrPropertySymbol>? = null
    ): IrProperty {

        val irField = irFactory.buildField {
            name = inName
            type = inType
            origin = IrDeclarationOrigin.PROPERTY_BACKING_FIELD
            visibility = DescriptorVisibilities.PRIVATE
        }.apply {
            parent = irClass
            initializer = inInitializer?.let { irFactory.createExpressionBody(it) }
        }

        val irProperty = irClass.addProperty {
            name = inName
            isVar = true
        }.apply {
            parent = irClass
            backingField = irField
            overridden?.let { overriddenSymbols = it }
            addDefaultGetter(irClass, irBuiltIns)
        }

        if (inIsVar) {
            irProperty.addDefaultSetter(irField)
        }

        return irProperty
    }

    fun IrProperty.addDefaultSetter(irField: IrField) {

        setter = irFactory.buildFun {

            origin = IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR
            name = Name.identifier("set-" + irField.name.identifier)
            visibility = DescriptorVisibilities.PUBLIC
            modality = Modality.FINAL
            returnType = irBuiltIns.unitType

        }.also {

            it.parent = parent
            it.correspondingPropertySymbol = symbol

            val receiver = it.addDispatchReceiver {
                type = parentAsClass.defaultType
            }

            val value = it.addValueParameter {
                name = Name.identifier("set-?")
                type = irField.type
            }

            it.body = DeclarationIrBuilder(irContext, this.symbol).irBlockBody {
                + irSetField(
                    receiver = irGet(receiver),
                    field = irField,
                    value = irGet(value)
                )
            }
        }
    }

    fun IrProperty.irSetField(value: IrExpression, receiver: IrExpression): IrSetFieldImpl {
        return IrSetFieldImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            backingField !!.symbol,
            receiver,
            value,
            irBuiltIns.unitType
        )
    }

    fun irGetValue(irProperty: IrProperty, receiver: IrExpression?): IrCall =
        IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irProperty.getter !!.returnType,
            irProperty.getter !!.symbol,
            0, 0,
            origin = IrStatementOrigin.GET_PROPERTY
        ).apply {
            dispatchReceiver = receiver
        }

    fun irSetValue(irProperty: IrProperty, value: IrExpression, receiver: IrExpression?): IrCall =
        IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irProperty.backingField !!.type,
            irProperty.setter !!.symbol,
            0, 1
        ).apply {
            dispatchReceiver = receiver
            putValueArgument(0, value)
        }

    // --------------------------------------------------------------------------------------------------------
    // IR Basics
    // --------------------------------------------------------------------------------------------------------

    fun irConst(value: Long): IrConst<Long> = IrConstImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        irContext.irBuiltIns.longType,
        IrConstKind.Long,
        value
    )

    fun irConst(value: Int): IrConst<Int> = IrConstImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        irContext.irBuiltIns.intType,
        IrConstKind.Int,
        value
    )

    fun irConst(value: String): IrConst<String> = IrConstImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        irContext.irBuiltIns.stringType,
        IrConstKind.String,
        value
    )

    fun irConst(value: Boolean) = IrConstImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        irContext.irBuiltIns.booleanType,
        IrConstKind.Boolean,
        value
    )

    fun irNull() = IrConstImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        irContext.irBuiltIns.anyNType,
        IrConstKind.Null,
        null
    )

    fun irGet(type: IrType, symbol: IrValueSymbol, origin: IrStatementOrigin?): IrExpression {
        return IrGetValueImpl(
            UNDEFINED_OFFSET,
            UNDEFINED_OFFSET,
            type,
            symbol,
            origin
        )
    }

    fun irGet(variable: IrValueDeclaration, origin: IrStatementOrigin? = null): IrExpression {
        return irGet(variable.type, variable.symbol, origin)
    }

    fun irIf(condition: IrExpression, body: IrExpression): IrExpression {
        return IrIfThenElseImpl(
            UNDEFINED_OFFSET,
            UNDEFINED_OFFSET,
            irContext.irBuiltIns.unitType,
            origin = IrStatementOrigin.IF
        ).also {
            it.branches.add(
                IrBranchImpl(condition, body)
            )
        }
    }

    fun irImplicitAs(toType: IrType, argument: IrExpression): IrTypeOperatorCallImpl {
        return IrTypeOperatorCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            toType,
            IrTypeOperator.IMPLICIT_CAST,
            toType,
            argument
        )
    }

    // --------------------------------------------------------------------------------------------------------
    // Logic
    // --------------------------------------------------------------------------------------------------------

    fun irAnd(lhs: IrExpression, rhs: IrExpression): IrCallImpl {
        return irCall(
            lhs.type.binaryOperator(OperatorNameConventions.AND, rhs.type),
            null,
            lhs,
            null,
            rhs
        )
    }

    fun irOr(lhs: IrExpression, rhs: IrExpression): IrCallImpl {
        val int = irContext.irBuiltIns.intType
        return irCall(
            int.binaryOperator(OperatorNameConventions.OR, int),
            null,
            lhs,
            null,
            rhs
        )
    }

    fun irEqual(lhs: IrExpression, rhs: IrExpression): IrExpression {
        return irCall(
            this.irContext.irBuiltIns.eqeqSymbol,
            null,
            null,
            null,
            lhs,
            rhs
        )
    }

    fun irNot(value: IrExpression): IrExpression {
        return irCall(
            irContext.irBuiltIns.booleanNotSymbol,
            dispatchReceiver = value
        )
    }

    fun irNotEqual(lhs: IrExpression, rhs: IrExpression): IrExpression {
        return irNot(irEqual(lhs, rhs))
    }

    fun irOrOr(lhs: IrExpression, rhs: IrExpression): IrExpression {
        return IrWhenImpl(
            UNDEFINED_OFFSET,
            UNDEFINED_OFFSET,
            origin = IrStatementOrigin.OROR,
            type = irContext.irBuiltIns.booleanType,
            branches = listOf(
                IrBranchImpl(
                    UNDEFINED_OFFSET,
                    UNDEFINED_OFFSET,
                    condition = lhs,
                    result = irConst(true)
                ),
                IrElseBranchImpl(
                    UNDEFINED_OFFSET,
                    UNDEFINED_OFFSET,
                    condition = irConst(true),
                    result = rhs
                )
            )
        )
    }

    // --------------------------------------------------------------------------------------------------------
    // Operators
    // --------------------------------------------------------------------------------------------------------

    fun IrType.binaryOperator(name: Name, paramType: IrType): IrFunctionSymbol =
        irContext.symbols.getBinaryOperator(name, this, paramType)

    // --------------------------------------------------------------------------------------------------------
    // Calls
    // --------------------------------------------------------------------------------------------------------

    fun irCall(
        symbol: IrFunctionSymbol,
        origin: IrStatementOrigin? = null,
        dispatchReceiver: IrExpression? = null,
        extensionReceiver: IrExpression? = null,
        vararg args: IrExpression
    ): IrCallImpl {
        return IrCallImpl(
            UNDEFINED_OFFSET,
            UNDEFINED_OFFSET,
            symbol.owner.returnType,
            symbol as IrSimpleFunctionSymbol,
            symbol.owner.typeParameters.size,
            symbol.owner.valueParameters.size,
            origin
        ).also {
            if (dispatchReceiver != null) it.dispatchReceiver = dispatchReceiver
            if (extensionReceiver != null) it.extensionReceiver = extensionReceiver
            args.forEachIndexed { index, arg ->
                it.putValueArgument(index, arg)
            }
        }
    }

    // --------------------------------------------------------------------------------------------------------
    // Trace
    // --------------------------------------------------------------------------------------------------------

//    fun irTrace(point: String, parameters: List<IrExpression>): IrStatement {
//        return irTrace(irThisReceiver(), point, parameters)
//    }
//
//    fun irTrace(function: IrFunction, point: String, parameters: List<IrExpression>): IrStatement {
//        return irTrace(irGet(function.dispatchReceiverParameter !!), point, parameters)
//    }
//
//    fun irTrace(fragment: IrExpression, point: String, parameters: List<IrExpression>): IrStatement {
//        return irTraceDirect(irGetValue(airClass.adapter, fragment), point, parameters)
//    }
//
//    /**
//     * @param dispatchReceiver The `AdaptiveAdapter` instance to use for the trace.
//     */
//    fun irTraceDirect(dispatchReceiver: IrExpression, point: String, parameters: List<IrExpression>): IrStatement {
//        return IrCallImpl(
//            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
//            irBuiltIns.unitType,
//            pluginContext.adaptiveAdapterTrace,
//            typeArgumentsCount = 0,
//            Indices.ADAPTIVE_TRACE_ARGUMENT_COUNT,
//        ).also {
//            it.dispatchReceiver = dispatchReceiver
//            it.putValueArgument(Indices.ADAPTIVE_TRACE_ARGUMENT_NAME, irConst(irClass.name.identifier))
//            it.putValueArgument(Indices.ADAPTIVE_TRACE_ARGUMENT_POINT, irConst(point))
//            it.putValueArgument(Indices.ADAPTIVE_TRACE_ARGUMENT_DATA, buildTraceVarArg(parameters))
//        }
//    }
//
//    fun buildTraceVarArg(parameters: List<IrExpression>): IrExpression {
//        return IrVarargImpl(
//            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
//            irBuiltIns.arrayClass.typeWith(irBuiltIns.anyNType),
//            pluginContext.adaptiveFragmentType,
//        ).also { vararg ->
//            parameters.forEach {
//                vararg.addElement(it)
//            }
//        }
//    }

    // --------------------------------------------------------------------------------------------------------
    // Misc
    // --------------------------------------------------------------------------------------------------------

    val String.function: IrFunction
        get() = irClass.declarations.first { it is IrFunction && it.name.asString() == this } as IrFunction

    val String.name: Name
        get() = Name.identifier(this)

}