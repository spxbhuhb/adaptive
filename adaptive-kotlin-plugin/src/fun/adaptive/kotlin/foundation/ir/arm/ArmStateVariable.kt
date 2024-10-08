/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.arm

import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.types.IrType

interface ArmStateVariable : ArmElement {

    val armClass: ArmClass
    val indexInState: Int
    val indexInClosure: Int
    val name: String
    val type : IrType
    val isInstructions : Boolean

    fun matches(symbol: IrSymbol): Boolean

}