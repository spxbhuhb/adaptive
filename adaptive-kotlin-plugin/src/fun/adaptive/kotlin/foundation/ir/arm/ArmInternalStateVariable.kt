/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.arm

import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ArmInternalStateVariableBuilder
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ClassBoundIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.types.IrType

class ArmInternalStateVariable(
    override val armClass: ArmClass,
    override val indexInState: Int,
    override val indexInClosure: Int,
    val irVariable: IrVariable,
    val producer: ArmValueProducer?,
    dependencies: ArmDependencies
) : ArmStateDefinitionStatement(irVariable, dependencies), ArmStateVariable {

    override val name = irVariable.name.identifier

    override val type: IrType
        get() = irVariable.type

    override val isInstructions = false

    override fun matches(symbol: IrSymbol): Boolean = (symbol == irVariable.symbol)

    fun builder(parent: ClassBoundIrBuilder) =
        ArmInternalStateVariableBuilder(parent, this)

}