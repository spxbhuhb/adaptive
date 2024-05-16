/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir.ir2arm

import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmState
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmStateVariable
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid

/**
 * Collects state variable dependencies. These may be:
 *
 * - [IrCall]: variable getter
 * - [IrGetValue]: access a parameter of the original function
 *
 */
class DependencyVisitor(
    private val closure: ArmState
) : IrElementVisitorVoid {

    var dependencies = mutableListOf<ArmStateVariable>()

    override fun visitElement(element: IrElement) {
        element.acceptChildren(this, null)
    }

    /**
     * Call to the getter.
     */
    override fun visitCall(expression: IrCall) {
        closure.firstOrNull { it.matches(expression.symbol) }?.let {
            dependencies += it
        }
        super.visitCall(expression)
    }

    /**
     * Parameter get from the original function.
     */
    override fun visitGetValue(expression: IrGetValue) {
        closure.firstOrNull { it.matches(expression.symbol) }?.let {
            dependencies += it
        }
        super.visitGetValue(expression)
    }
}