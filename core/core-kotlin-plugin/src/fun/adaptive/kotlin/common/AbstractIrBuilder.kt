/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.common

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.InternalSymbolFinderAPI
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.IrBlockBodyBuilder
import org.jetbrains.kotlin.ir.builders.declarations.buildReceiverParameter
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrValueSymbol
import org.jetbrains.kotlin.ir.symbols.impl.IrValueParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.getPrimitiveArrayElementType
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames
import org.jetbrains.kotlin.util.OperatorNameConventions

interface AbstractIrBuilder {

    val pluginContext: AbstractPluginContext

    val irContext
        get() = pluginContext.irContext

    val irFactory
        get() = irContext.irFactory

    val irBuiltIns
        get() = irContext.irBuiltIns

    // --------------------------------------------------------------------------------------------------------
    // Classes
    // --------------------------------------------------------------------------------------------------------

    fun IrClass.thisReceiver(): IrValueParameter =
        irFactory.createValueParameter(
            SYNTHETIC_OFFSET,
            SYNTHETIC_OFFSET,
            origin = IrDeclarationOrigin.INSTANCE_RECEIVER,
            kind = IrParameterKind.DispatchReceiver,
            name = SpecialNames.THIS,
            symbol = IrValueParameterSymbolImpl(),
            type = IrSimpleTypeImpl(symbol, false, emptyList(), emptyList()),
            varargElementType = null,
            isCrossinline = false,
            isNoinline = false,
            isHidden = false,
            isAssignable = false
        ).also {
            it.parent = this
            this.thisReceiver = it
        }

    // --------------------------------------------------------------------------------------------------------
    // Properties
    // --------------------------------------------------------------------------------------------------------

    fun transformGetter(irClass: IrClass, getter: IrSimpleFunction, field: IrField?, value: (AbstractIrBuilder.() -> IrExpression)? = null) {

        getter.isFakeOverride = false

        getter.origin = if (field != null) {
            IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR
        } else {
            IrDeclarationOrigin.DEFINED
        }

        getter.replaceDispatchReceiver(irClass.defaultType)

        val returnValue =
            if (field != null) {
                IrGetFieldImpl(
                    UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                    field.symbol,
                    field.type,
                    IrGetValueImpl(
                        UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                        getter.dispatchReceiverParameter !!.type,
                        getter.dispatchReceiverParameter !!.symbol
                    )
                )
            } else {
                checkNotNull(value)()
            }

        getter.body = DeclarationIrBuilder(irContext, getter.symbol).irBlockBody {
            + irReturn(
                returnValue
            )
        }
    }

    fun irGetValue(irProperty: IrProperty, receiver: IrExpression?): IrCall =
        IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irProperty.getter !!.returnType,
            irProperty.getter !!.symbol,
            typeArgumentsCount = 0,
            origin = IrStatementOrigin.GET_PROPERTY
        ).apply {
            arguments[0] = receiver
        }

    fun irSetValue(irProperty: IrProperty, value: IrExpression, receiver: IrExpression?): IrCall =
        IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irProperty.getter !!.returnType,
            irProperty.setter !!.symbol,
            typeArgumentsCount = 0
        ).apply {
            arguments[0] = receiver
            arguments[1] = value
        }

    // --------------------------------------------------------------------------------------------------------
    // Functions
    // --------------------------------------------------------------------------------------------------------

    fun IrSimpleFunction.replaceDispatchReceiver(type : IrType) {
        parameters = parameters.map {
            if (it.kind != IrParameterKind.DispatchReceiver) {
                it
            } else {
                buildReceiverParameter {
                    this.type = type
                }
            }
        }
    }

    fun IrFunction.irBlockBody(builder: IrBlockBodyBuilder.() -> Unit) =
        DeclarationIrBuilder(pluginContext.irContext, symbol).irBlockBody(startOffset, endOffset, builder)

    // --------------------------------------------------------------------------------------------------------
    // IR Basics
    // --------------------------------------------------------------------------------------------------------

    fun irConst(value: Long): IrConst = IrConstImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        irContext.irBuiltIns.longType,
        IrConstKind.Long,
        value
    )

    fun irConst(value: Int): IrConst = IrConstImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        irContext.irBuiltIns.intType,
        IrConstKind.Int,
        value
    )

    fun irConst(value: String): IrConst = IrConstImpl(
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

    fun irGetField(receiver: IrExpression, field: IrField): IrGetFieldImpl {
        return IrGetFieldImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            field.symbol,
            field.type,
            receiver,
            origin = IrStatementOrigin.GET_PROPERTY
        )
    }

    fun irGetObject(symbol: IrClassSymbol) = IrGetObjectValueImpl(
        SYNTHETIC_OFFSET,
        SYNTHETIC_OFFSET,
        IrSimpleTypeImpl(symbol, false, emptyList(), emptyList()),
        symbol
    )

    fun irIf(condition: IrExpression, body: IrExpression): IrExpression {
        return IrWhenImpl(
            UNDEFINED_OFFSET,
            UNDEFINED_OFFSET,
            irContext.irBuiltIns.unitType,
            origin = IrStatementOrigin.IF,
            branches = listOf(IrBranchImpl(condition, body))
        )
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
            lhs,
            rhs
        )
    }

    fun irOr(lhs: IrExpression, rhs: IrExpression): IrCallImpl {
        val int = irContext.irBuiltIns.intType
        return irCall(
            int.binaryOperator(OperatorNameConventions.OR, int),
            lhs,
            rhs
        )
    }

    fun irEqual(lhs: IrExpression, rhs: IrExpression): IrExpression {
        return irCall(
            this.irContext.irBuiltIns.eqeqSymbol,
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
        dispatchReceiver: IrExpression?,
        vararg args: IrExpression
    ): IrCallImpl {
        return IrCallImpl(
            UNDEFINED_OFFSET,
            UNDEFINED_OFFSET,
            symbol.owner.returnType,
            symbol as IrSimpleFunctionSymbol,
            symbol.owner.typeParameters.size
        ).also { call ->
            var index = 0
            if (dispatchReceiver != null) call.arguments[index++] = dispatchReceiver
            args.forEach { arg ->
                call.arguments[index++] = arg
            }
        }
    }

    // --------------------------------------------------------------------------------------------------------
    // Misc
    // --------------------------------------------------------------------------------------------------------

    val String.name: Name
        get() = Name.identifier(this)

//    val IrType.isList
//        get() = isSubtypeOfClass(pluginContext.listClass)

    fun <T> IrType.ifBoolean(result: () -> T): T? =
        if (isBoolean()) result() else null

    fun <T> IrType.ifByteArray(result: () -> T): T? {
        return if (getPrimitiveArrayElementType() == irBuiltIns.byteType.getPrimitiveType()) {
            result()
        } else {
            null
        }
    }

    fun irArrayOf(elements: List<IrVarargElement>): IrCall {
        val type = irBuiltIns.arrayClass.typeWith(irBuiltIns.stringType)

        return IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            type,
            irBuiltIns.arrayOf,
            typeArgumentsCount = 1
        ).apply {
            typeArguments[0] = irBuiltIns.stringType
            arguments[0] = IrVarargImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                type,
                irBuiltIns.stringType
            ).also {
                it.elements += elements
            }
        }
    }

    @OptIn(InternalSymbolFinderAPI::class)
    fun irIntArrayOf(values: Iterable<Int>): IrCall =
        IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.intArray.defaultType,
            irContext.irBuiltIns.symbolFinder.findFunctions(Name.identifier("intArrayOf")).single(),
            typeArgumentsCount = 0
        ).apply {
            arguments[0] =
                IrVarargImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.intArray.defaultType,
                    irBuiltIns.intType
                ).apply {
                    elements += values.map { irConst(it) }
                }
        }

}