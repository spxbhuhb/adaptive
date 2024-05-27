/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir.util

import hu.simplexion.adaptive.kotlin.foundation.Names
import hu.simplexion.adaptive.kotlin.foundation.Strings
import hu.simplexion.adaptive.kotlin.foundation.ir.AdaptivePluginContext
import org.jetbrains.kotlin.backend.jvm.codegen.isExtensionFunctionType
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.getArrayElementType
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.types.isArray
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.isFunctionOrKFunction

interface AdaptiveAnnotationBasedExtension {

    val pluginContext: AdaptivePluginContext

    val IrFunction.isAdaptive: Boolean
        get() = symbol.owner.hasAnnotation(pluginContext.adaptiveClass)

    val IrType.isAdaptive: Boolean
        get() {
            if (! isExtensionFunctionType) return false
            if (this !is IrSimpleTypeImpl) return false
            return this.hasAnnotation(pluginContext.adaptiveClass)
        }

    val IrValueParameter?.isAdaptive: Boolean
        get() = this?.hasAnnotation(pluginContext.adaptiveClass) ?: false

    val IrValueParameter?.isInstructions: Boolean
        get() = (this != null
            && name.identifier == Strings.INSTRUCTIONS
            && type.isArray()
            && type.getArrayElementType(pluginContext.irBuiltIns).isSubtypeOfClass(pluginContext.adaptiveInstructionClass)
            )

    val IrValueParameter?.isDetach: Boolean
        get() = this?.hasAnnotation(pluginContext.adaptiveDetachClass) ?: false

    val IrCall.isExpectCall: Boolean
        get() = symbol.owner.hasAnnotation(pluginContext.adaptiveExpectClass)

    val IrCall.isDirectAdaptiveCall: Boolean
        get() = symbol.owner.hasAnnotation(pluginContext.adaptiveClass) || symbol.owner.hasAnnotation(pluginContext.adaptiveExpectClass)

    val IrCall.isArgumentAdaptiveCall: Boolean
        get() = symbol.owner.name == Names.KOTLIN_INVOKE && dispatchReceiver?.let {
            // expect annotation on a parameter is meaningless, so we don't have to check it here
            it is IrGetValue && it.symbol.owner.hasAnnotation(pluginContext.adaptiveClass)
        } ?: false

    val IrCall.isTransformInterfaceCall: Boolean
        get() = dispatchReceiver !!.type.isSubtypeOfClass(pluginContext.adaptiveTransformInterfaceClass)

    fun IrType.isAccessSelector(previousType: IrType?): Boolean {
        if (previousType == null) return false
        if (! isFunctionOrKFunction()) return false
        return previousType.isSubtypeOfClass(pluginContext.adaptiveStateVariableBindingClass)
    }

}