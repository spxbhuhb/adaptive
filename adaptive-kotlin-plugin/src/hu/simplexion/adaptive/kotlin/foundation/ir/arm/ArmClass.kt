/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir.arm

import hu.simplexion.adaptive.kotlin.foundation.ir.AdaptivePluginContext
import hu.simplexion.adaptive.kotlin.foundation.ir.ir2arm.BoundaryVisitor
import hu.simplexion.adaptive.kotlin.foundation.ir.util.adaptiveClassFqName
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.util.statements

class ArmClass(
    adaptiveContext: AdaptivePluginContext,
    val originalFunction: IrFunction,
    val isRoot : Boolean
) : ArmElement {

    val fqName = originalFunction.adaptiveClassFqName()
    val name = fqName.shortName()

    val boundary: ArmBoundary = BoundaryVisitor(adaptiveContext).findBoundary(originalFunction)

    val originalStatements = checkNotNull(originalFunction.body?.statements) { "missing function body" }

    val stateDefinitionStatements = mutableListOf<ArmStateDefinitionStatement>()
    val originalRenderingStatements = mutableListOf<IrStatement>()

    val stateVariables = mutableListOf<ArmStateVariable>()

    val rendering = mutableListOf<ArmRenderingStatement>()

    var hasInvokeBranch = false
    var hasInvokeSuspendBranch = false

    var stateInterface : IrClassSymbol? = null
}