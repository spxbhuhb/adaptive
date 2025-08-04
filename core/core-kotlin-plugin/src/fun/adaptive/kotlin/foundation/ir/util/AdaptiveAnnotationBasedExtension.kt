/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.util

import `fun`.adaptive.kotlin.foundation.Names
import `fun`.adaptive.kotlin.foundation.Strings
import `fun`.adaptive.kotlin.foundation.ir.FoundationPluginContext
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.getArrayElementType
import org.jetbrains.kotlin.ir.types.isArray
import org.jetbrains.kotlin.ir.util.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.isFunctionOrKFunction

interface AdaptiveAnnotationBasedExtension {

    val pluginContext: FoundationPluginContext

    val IrFunction.isAdaptive: Boolean
        get() = symbol.owner.hasAnnotation(pluginContext.adaptiveClass)

    val IrValueParameter?.isAdaptive: Boolean
        // FIXME remove hard-coded _fixme_adaptive_content and other annotation-issue hacks
        // KT-74337 Local Delegated properties don't preserve their annotations and don't show up in reflection
        get() {
            if (this == null) return false
            if (this.name.isSpecial) return false
            if (this.name.identifier.startsWith("_fixme_")) return true
            if (this.name.identifier.startsWith("_KT_74337_")) return true
            return this.type.hasAnnotation(pluginContext.adaptiveClass)
        }

    val IrValueParameter?.isInstructions: Boolean
        get() = (this != null
            && name.identifier == Strings.INSTRUCTIONS
            && type.isArray()
            && type.getArrayElementType(pluginContext.irBuiltIns).isSubtypeOfClass(pluginContext.adaptiveInstructionClass)
            )

    val IrValueParameter?.isDetach: Boolean
        get() = this?.hasAnnotation(pluginContext.adaptiveDetachClass) == true

    val IrCall.isExpectCall: Boolean
        get() = symbol.owner.hasAnnotation(pluginContext.adaptiveExpectClass)

    val IrCall.isHydratedCall: Boolean
        get() = symbol.owner.hasAnnotation(pluginContext.hydratedAnnotation)

    val IrCall.isDirectAdaptiveCall: Boolean
        get() = symbol.owner.hasAnnotation(pluginContext.adaptiveClass) || symbol.owner.hasAnnotation(pluginContext.adaptiveExpectClass)

    val IrCall.isArgumentAdaptiveCall: Boolean
        get() = symbol.owner.name == Names.KOTLIN_INVOKE && dispatchReceiver?.let {
            // expect annotation on a parameter is meaningless, so we don't have to check it here
            it is IrGetValue && it.symbol.owner.type.hasAnnotation(pluginContext.adaptiveClass)
        } == true

    fun IrType.isAccessSelector(parameter: IrValueParameter?, previousType: IrType?): Boolean {
        if (parameter == null) return false
        if (! parameter.hasAnnotation(pluginContext.propertySelectorAnnotation)) return false

        check(previousType != null) { "no variable binding before the selector: ${parameter.dump()}" }
        check(isFunctionOrKFunction()) { "selector is not a function:  ${parameter.dump()}" }
        check(previousType.isSubtypeOfClass(pluginContext.adaptiveStateVariableBindingClass)) { "no variable binding before the selector:  ${parameter.dump()}" }

        return true
    }

    fun IrType.isInstruction(pluginContext: FoundationPluginContext) =
        isSubtypeOfClass(pluginContext.adaptiveInstructionClass) ||
            (isArray() && getArrayElementType(pluginContext.irBuiltIns).isSubtypeOfClass(pluginContext.adaptiveInstructionClass))

}