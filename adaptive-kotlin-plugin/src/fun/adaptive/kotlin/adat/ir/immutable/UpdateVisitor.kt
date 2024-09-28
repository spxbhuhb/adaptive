/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.adat.ir.immutable

import `fun`.adaptive.kotlin.adat.ir.AdatIrBuilder
import `fun`.adaptive.kotlin.adat.ir.AdatPluginContext
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.util.*
import kotlin.collections.map
import kotlin.collections.plusAssign

/**
 * Handles updates of immutable classes by replacing the update call with a version
 * that calls the store to perform the update.
 */
class UpdateVisitor(
    override val pluginContext: AdatPluginContext,
) : IrElementTransformerVoidWithContext(), AdatIrBuilder {

    //  See comments in AdatGenerationExtension

//    override fun visitCall(expression: IrCall): IrExpression {
//        if (expression.symbol != pluginContext.updateShorthand) return super.visitCall(expression)
//
//        val (getValue, path) = flattenPropertyPath(expression)
//
//        return irCall(
//            pluginContext.updateComplete,
//            dispatchReceiver = null,
//            getValue,
//            irArrayOf(path.map { irConst(it) }),
//            expression.getValueArgument(0)!! // the lambda to generate the value, as we are in the same scope, it is fine to use it directly
//        )
//    }
//
//    /**
//     * Flatten a get chain for properties. For example: `a.b.c.update {  }`.
//     * These should be like this in IR:
//     *
//     * ```text
//     * irCall                -- update
//     *   irCall              -- property access 1
//     *     ...
//     *       irCall          -- property access N
//     *         irGetValue    -- the starting variable access
//     * ```
//     */
//    fun flattenPropertyPath(expression: IrExpression): Pair<IrExpression, List<String>> {
//        check(expression is IrCall)
//
//        var current: IrStatement? = requireNotNull(expression.extensionReceiver)
//
//        val path = mutableListOf<String>()
//
//        while (current != null) {
//            when (current) {
//
//                is IrGetValue -> {
//                    check(path.isNotEmpty())
//                    return current to path
//                }
//
//                is IrCall -> {
//                    val symbol = current.symbol.owner.correspondingPropertySymbol
//                    checkNotNull(symbol) { "not a property access (1): ${expression.dump()}" }
//                    path += symbol.owner.name.identifier
//                    current = current.dispatchReceiver
//                }
//
//                else -> {
//                    throw IllegalStateException("unknown access: ${expression.dump()}")
//                }
//            }
//        }
//
//        throw IllegalStateException("invalid property access chain: ${expression.dumpKotlinLike()}")
//    }
}