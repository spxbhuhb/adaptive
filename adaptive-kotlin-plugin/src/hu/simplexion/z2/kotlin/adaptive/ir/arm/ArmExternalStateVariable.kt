/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.types.IrType

class ArmExternalStateVariable(
    override val armClass: ArmClass,
    override val indexInState: Int,
    override val indexInClosure: Int,
    override val name: String,
    override val type : IrType,
    val symbol : IrSymbol
) : ArmStateVariable {

    override fun matches(symbol: IrSymbol): Boolean = (symbol == this.symbol)

}