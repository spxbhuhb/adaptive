/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.ir2arm

import `fun`.adaptive.kotlin.foundation.ir.FoundationPluginContext
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmBoundary
import `fun`.adaptive.kotlin.foundation.ir.util.AdaptiveAnnotationBasedExtension
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrReturn
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.ir.visitors.IrVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid

/**
 * Finds the boundary between state definition and rendering parts of an adaptive function.
 * The boundary is the `startOffset` of the first rendering statement.
 * Entry point is [findBoundary].
 */
class BoundaryVisitor(
    override val pluginContext: FoundationPluginContext
) : IrVisitorVoid(), AdaptiveAnnotationBasedExtension {

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
            expression.isHydratedCall -> found = true
            expression.isDirectAdaptiveCall -> found = true
            expression.isArgumentAdaptiveCall -> found = true
            else -> super.visitCall(expression)
        }
    }
}
