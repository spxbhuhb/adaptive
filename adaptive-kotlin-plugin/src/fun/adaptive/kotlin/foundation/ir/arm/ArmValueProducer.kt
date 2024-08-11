/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.arm

import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrCall

open class ArmValueProducer(
    val armClass: ArmClass,
    val producerCall : IrCall,
    val producerDependencies : ArmDependencies,
    val postProcess : IrVariable
) : ArmElement