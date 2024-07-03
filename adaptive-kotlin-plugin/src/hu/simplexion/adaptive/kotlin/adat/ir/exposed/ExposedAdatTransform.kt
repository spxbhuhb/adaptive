/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.exposed

import hu.simplexion.adaptive.kotlin.adat.Names
import hu.simplexion.adaptive.kotlin.adat.ir.AdatPluginContext
import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.*

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

            if (column.type != parameter.type) {
                wrongType += column
                continue
            }

            columns -= column
            mapping += Mapping(column.property, column.type, parameter)
        }

        check(missingColumns.isEmpty()) {
            "missing column for constructor parameters ${missingColumns.joinToString { it.name.toString() }} in ${tableClass.name}"
        }

        check(columns.isEmpty()) {
            "missing constructor parameter for columns ${columns.joinToString { it.property.name.toString() }} in ${tableClass.name}"
        }

        check(wrongType.isEmpty()) {
            "invalid type for columns ${wrongType.joinToString { it.property.name.toString() }} in ${tableClass.name}"
        }

        return mapping
    }

    override fun visitFunctionNew(declaration: IrFunction): IrStatement =
        when (declaration.name) {
            Names.FROM_ROW -> transformFromRow(declaration)
            Names.TO_ROW -> transformToRow(declaration)
            else -> declaration
        }

    private fun transformFromRow(declaration: IrFunction): IrStatement {
        if (! declaration.isFakeOverride) return declaration

        declaration.body = DeclarationIrBuilder(pluginContext.irContext, declaration.symbol).irBlockBody {
            + irReturn(
                IrConstructorCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    adatClass.defaultType,
                    constructor.symbol,
                    typeArgumentsCount = 0,
                    constructorTypeArgumentsCount = 0,
                    valueArgumentsCount = mappings.size
                ).also { call ->
                    // mappings are generated from value parameters of the constructor, indices are the same
                    mappings.forEachIndexed { index, mapping ->

                        val value = irCall(
                            pluginContext.exposedResultRowGet !!,
                            irGet(declaration.valueParameters.first()),
                        ).also { get ->
                            get.putTypeArgument(0, mapping.columnType)
                            get.putValueArgument(0, irGetValue(mapping.column, irGet(declaration.dispatchReceiverParameter !!)))
                        }

                        call.putValueArgument(index, toCommon(value))
                    }
                }
            )
        }

        return declaration
    }

    private fun toCommon(call: IrCallImpl): IrExpression? {
        when (call.type) {
            pluginContext.uuidType -> toCommonUuid(call)
            else -> call
        }
    }

    private fun transformToRow(declaration: IrFunction): IrStatement {
        if (! declaration.isFakeOverride) return declaration

        return declaration
    }


}
