/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.types.IrType

/**
 * Fake state variable created for structural blocks (sequence, select, loop).
 */
class ArmImplicitStateVariable(
    override val armClass: ArmClass,
    override val indexInState: Int,
    override val indexInClosure: Int,
    irVariable: IrStatement
) : ArmStateDefinitionStatement(irVariable, emptyList()), ArmStateVariable {

    override val name: String
        get() = "<implicit>"

    override val type: IrType
        get() = throw IllegalStateException("should not be called")

    override fun matches(symbol: IrSymbol): Boolean = false

}