/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.foundation.ir.export

import org.jetbrains.kotlin.ir.declarations.IrClass

class AdaptiveActualClass(
    val namespace: String,
    val irClass: IrClass
) {
    val key : String
        get() = namespace + ":" + irClass.name.asString().removePrefix("Adaptive")
}