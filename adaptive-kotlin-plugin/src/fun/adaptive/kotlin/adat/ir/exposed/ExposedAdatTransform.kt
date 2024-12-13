/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.adat.ir.exposed

import `fun`.adaptive.kotlin.adat.FqNames
import `fun`.adaptive.kotlin.adat.Names
import `fun`.adaptive.kotlin.adat.ir.AdatPluginContext
import `fun`.adaptive.kotlin.common.AbstractIrBuilder
import `fun`.adaptive.kotlin.common.property
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.primaryConstructor

class ExposedAdatTransform(
    override val pluginContext: AdatPluginContext,
    private val tableClass: IrClass
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    private val adatClass = findAdatClass(tableClass)
    private val constructor = checkNotNull(adatClass.primaryConstructor) { "missing primary constructor for ${adatClass.name}" }
    private val mappings = map(ColumnVisitor(pluginContext, tableClass).columns, constructor.valueParameters)

    private fun findAdatClass(declaration: IrClass): IrClass {
        for (superType in declaration.superTypes) {
            if (superType !is IrSimpleType) continue
            if (superType.arguments.isEmpty()) continue
            val adatClass = superType.arguments.firstOrNull { it.typeOrNull?.isSubtypeOfClass(pluginContext.adatClass) == true }
            if (adatClass != null) return adatClass.typeOrFail.classOrFail.owner
        }
        error("class ${tableClass.name} has no supertype with AdatClass type argument")
    }

    private fun map(columns: MutableSet<ColumnProperty>, parameters: List<IrValueParameter>): List<Mapping> {
        val mapping = mutableListOf<Mapping>()
        val missingColumns = mutableListOf<IrValueParameter>()
        val wrongType = mutableListOf<ColumnProperty>()

        for (parameter in parameters) {
            val column = columns.firstOrNull { it.property.name == parameter.name }

            if (column == null) {
                missingColumns += parameter
                continue
            }

            if (! isTypeOk(column, parameter) && column.property.name != Names.ID) {
                wrongType += column
                continue
            }

            columns -= column
            mapping += Mapping(column.property, column.type, parameter)
        }

        check(wrongType.isEmpty() && missingColumns.isEmpty() && columns.isEmpty()) {
            "error mapping columns to constructor parameters in ${tableClass.name} : \n" +
                "  wrong type: ${wrongType.joinToString { it.property.name.toString() }}\n" +
                "  missing column: ${missingColumns.joinToString { it.name.toString() }}\n" +
                "  missing constructor parameter: ${columns.joinToString { it.property.name.toString() }}"
        }

        return mapping
    }

    fun isTypeOk(column: ColumnProperty, parameter: IrValueParameter): Boolean {
        if (column.type == parameter.type) return true

        if (! parameter.type.isSubtypeOfClass(pluginContext.commonUuid)) return false

        val nullable = parameter.type.isNullable()
        val entityId = column.type.isSubtypeOfClass(pluginContext.entityId !!)

        return when {
            entityId && ! column.type.isNullable() && ! nullable -> true
            entityId && column.type.isNullable() && nullable -> true
            column.type == pluginContext.javaUuidType && ! nullable -> true
            column.type == pluginContext.javaUuidTypeN && nullable -> true
            else -> false
        }
    }

    override fun visitFunctionNew(declaration: IrFunction): IrStatement =
        when (declaration.name) {
            Names.FROM_ROW -> transformFromRow(declaration as IrSimpleFunction)
            Names.TO_ROW -> transformToRow(declaration as IrSimpleFunction)
            else -> declaration
        }

    private fun transformFromRow(declaration: IrSimpleFunction): IrStatement {
        if (! declaration.isFakeOverride) return declaration

        declaration.isFakeOverride = false
        declaration.origin = IrDeclarationOrigin.DEFINED

        declaration.body = DeclarationIrBuilder(pluginContext.irContext, declaration.symbol).irBlockBody {

            val table = irTemporary(irImplicitAs(tableClass.defaultType, irGet(declaration.dispatchReceiverParameter !!)))
            val resultRow = declaration.valueParameters.first()

            + irReturn(
                IrConstructorCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    adatClass.defaultType,
                    constructor.symbol,
                    typeArgumentsCount = 0,
                    constructorTypeArgumentsCount = 0
                ).also { call ->
                    // mappings are generated from value parameters of the constructor, indices are the same
                    mappings.forEachIndexed { index, mapping ->

                        val value = irCall(
                            pluginContext.exposedResultRowGet !!,
                            irGet(resultRow),
                        ).also { get ->
                            get.putTypeArgument(0, mapping.columnType)
                            get.putValueArgument(0, irGetValue(mapping.column, irGet(table)))
                        }

                        call.putValueArgument(index, toCommon(mapping, value))
                    }
                }
            )
        }

        return declaration
    }

    private fun toCommon(mapping: Mapping, call: IrCallImpl): IrExpression =
        when {
            mapping.columnType.isSubtypeOfClass(pluginContext.entityId !!) -> {
                val f = if (mapping.columnType.isNullable()) pluginContext.asCommonEntityIdN else pluginContext.asCommonEntityId
                toCommon(mapping, f !!, call)
            }

            mapping.columnType == pluginContext.javaUuidType -> {
                val f = if (mapping.columnType.isNullable()) pluginContext.asCommonUuidN else pluginContext.asCommonUuid
                toCommon(mapping, pluginContext.asCommonUuid !!, call)
            }

            else -> call
        }

    private fun toCommon(mapping: Mapping, func: IrSimpleFunctionSymbol, value: IrExpression) =
        IrCallImpl(
            UNDEFINED_OFFSET, UNDEFINED_OFFSET,
            func.owner.returnType,
            func,
            1
        ).also {
            it.extensionReceiver = value
            it.putTypeArgument(0, (mapping.parameter.type as IrSimpleType).arguments[0].typeOrFail)
        }

    private fun transformToRow(declaration: IrSimpleFunction): IrStatement {
        if (! declaration.isFakeOverride) return declaration

        declaration.isFakeOverride = false
        declaration.origin = IrDeclarationOrigin.DEFINED

        val set = tableClass.declarations.first {
            it is IrSimpleFunction && it.name == Names.SET && it.hasAnnotation(FqNames.EXPOSED_ADAT_SET)
        }.symbol as IrSimpleFunctionSymbol

        declaration.body = DeclarationIrBuilder(pluginContext.irContext, declaration.symbol).irBlockBody {

            val valueParameters = declaration.valueParameters

            val table = irTemporary(irImplicitAs(tableClass.defaultType, irGet(declaration.dispatchReceiverParameter !!)))
            val updateBuilder = valueParameters.first()
            val adatInstance = valueParameters[1]

            for (mapping in mappings) {
                + irCall(
                    set,
                    irGet(table),
                ).also { set ->
                    set.putTypeArgument(0, mapping.columnType)

                    val adatProperty = adatClass.property(mapping.parameter.name)

                    set.putValueArgument(0, irGet(updateBuilder))
                    set.putValueArgument(1, toJava(mapping, irGetValue(adatProperty, irGet(adatInstance)), table))
                    set.putValueArgument(2, irGetValue(mapping.column, irGet(table)))
                }
            }
        }

        return declaration
    }

    private fun toJava(mapping: Mapping, call: IrCall, table: IrVariable): IrExpression =
        when {
            mapping.columnType.isSubtypeOfClass(pluginContext.entityId !!) -> {
                val f = if (mapping.columnType.isNullable()) pluginContext.asEntityIdN else pluginContext.asEntityId
                toJava(f !!, call, irGetValue(mapping.column, irGet(table)))
            }

            mapping.columnType.isSubtypeOfClass(pluginContext.javaUuid !!) -> {
                val f = if (mapping.columnType.isNullable()) pluginContext.asJavaUuidN else pluginContext.asJavaUuid
                toJava(f !!, call, null)
            }

            else -> call
        }

    private fun toJava(
        func: IrSimpleFunctionSymbol,
        value: IrExpression,
        valueArgument: IrExpression?
    ) =
        IrCallImpl(
            UNDEFINED_OFFSET, UNDEFINED_OFFSET,
            func.owner.returnType,
            func,
            0
        ).also {
            it.extensionReceiver = value
            if (valueArgument != null) {
                it.putValueArgument(0, valueArgument)
            }
        }

}
