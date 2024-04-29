/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.ir.ir2arm

import hu.simplexion.adaptive.kotlin.base.ir.AdaptivePluginContext
import hu.simplexion.adaptive.kotlin.base.ir.arm.ArmBoundary
import hu.simplexion.adaptive.kotlin.base.ir.util.AdaptiveNonAnnotationBasedExtension
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrReturn
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid

/**
 * Finds the boundary between state definition and rendering parts of an adaptive function.
 * The boundary is the `startOffset` of the first rendering statement.
 * Entry point is [findBoundary].
 */
class BoundaryVisitor(
    override val adaptiveContext: AdaptivePluginContext
) : IrElementVisitorVoid, AdaptiveNonAnnotationBasedExtension {

    var found: Boolean = false

    fun findBoundary(declaration: IrFunction): ArmBoundary {

        declaration.body?.statements?.let { statements ->

            statements.forEachIndexed { index, irStatement ->
                if (irStatement is IrReturn) {
                    found = true
                } else {
                    irStatement.acceptVoid(this)
                }
                if (found) return ArmBoundary(irStatement.startOffset, index)
            }

            return ArmBoundary(declaration.endOffset, statements.size)
        }

        throw IllegalStateException("function has no body (maybe it's an expression function)")
    }

    override fun visitElement(element: IrElement) {
        if (found) return
        element.acceptChildren(this, null)
    }

    override fun visitCall(expression: IrCall) {
        when {
            expression.isDirectAdaptiveCall -> found = true
            expression.isArgumentAdaptiveCall -> found = true
            else -> super.visitCall(expression)
        }
    }
}
