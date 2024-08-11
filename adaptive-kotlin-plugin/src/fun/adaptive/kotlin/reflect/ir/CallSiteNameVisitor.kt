/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.reflect.ir

import `fun`.adaptive.kotlin.common.AbstractIrBuilder
import `fun`.adaptive.kotlin.reflect.FqNames
import `fun`.adaptive.kotlin.reflect.Names
import `fun`.adaptive.kotlin.reflect.Strings
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.declarations.IrDeclarationWithName
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.types.isString
import org.jetbrains.kotlin.ir.util.fqNameWhenAvailable
import org.jetbrains.kotlin.ir.util.hasAnnotation

class CallSiteNameVisitor(
    override val pluginContext: ReflectPluginContext,
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    override fun visitCall(expression: IrCall): IrExpression {
        val func = expression.symbol.owner
        if (! func.hasAnnotation(FqNames.CALL_SITE_NAME_ANNOTATION)) {
            return super.visitCall(expression)
        }

        val valueParameters = func.valueParameters
        val callSiteName = valueParameters.firstOrNull { it.name == Names.CALL_SITE_NAME_PARAMETER }

        check(callSiteName != null) { "missing 'callSiteName' parameter in ${func.fqNameWhenAvailable ?: "<anonymous>"}" }
        check(callSiteName.type.isString()) { "'callSiteName' is not a String in ${func.fqNameWhenAvailable ?: "<anonymous>"}" }

        if (expression.getValueArgument(callSiteName.index) != null) {
            return super.visitCall(expression)
        }

        if (currentScope == null) {
            expression.putValueArgument(callSiteName.index, irConst(Strings.UNKNOWN))
            return super.visitCall(expression)
        }

        var name: String? = null

        for (scope in allScopes.reversed()) {
            val element = scope.irElement
            if (element !is IrDeclarationWithName) continue

            name = element.fqNameWhenAvailable?.asString() ?: continue
            break
        }


        expression.putValueArgument(
            callSiteName.index,
            irConst(name ?: Strings.UNKNOWN)
        )

        return super.visitCall(expression)
    }
}
