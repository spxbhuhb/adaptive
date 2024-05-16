/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir.arm

import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.types.IrType

/**
 * A fake state variable created for support function transformation.
 */
class ArmSupportStateVariable(
    override val armClass: ArmClass,
    override val indexInState: Int,
    override val indexInClosure: Int,
    val irVariable: IrValueParameter
) : ArmStateDefinitionStatement(irVariable, emptyList()), ArmStateVariable {

    override val name = irVariable.name.identifier

    override val type: IrType
        get() = irVariable.type

    override fun matches(symbol: IrSymbol): Boolean = (symbol == irVariable.symbol)

}