package hu.simplexion.z2.kotlin.adaptive.ir.arm

import org.jetbrains.kotlin.ir.IrStatement

open class ArmStateDefinitionStatement(
    val irStatement: IrStatement,
    val dependencies : ArmDependencies
) : ArmElement