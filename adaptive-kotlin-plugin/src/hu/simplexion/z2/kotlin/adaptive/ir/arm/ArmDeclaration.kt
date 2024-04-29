/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import org.jetbrains.kotlin.ir.declarations.IrDeclaration

class ArmDeclaration(
    val armClass: ArmClass,
    val irDeclaration: IrDeclaration,
    val dependencies: ArmDependencies
) : ArmElement