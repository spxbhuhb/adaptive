/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.ir.util

import hu.simplexion.adaptive.kotlin.base.Names
import hu.simplexion.adaptive.kotlin.base.ir.AdaptivePluginContext
import org.jetbrains.kotlin.backend.jvm.codegen.isExtensionFunctionType
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.isFunctionOrKFunction

interface AdaptiveAnnotationBasedExtension {

    val pluginContext: AdaptivePluginContext

    val IrFunction.isAdaptive: Boolean
        get() = symbol.owner.hasAnnotation(pluginContext.adaptiveNamespaceClass)

    val IrType.isAdaptive: Boolean
        get() {
            if (! isExtensionFunctionType) return false
            if (this !is IrSimpleTypeImpl) return false
           return this.hasAnnotation(pluginContext.adaptiveNamespaceClass)
        }

    val IrCall.isDelegated: Boolean
        get() = symbol.owner.hasAnnotation(pluginContext.delegatedClass)

    val IrCall.isDirectAdaptiveCall : Boolean
        get() = symbol.owner.hasAnnotation(pluginContext.adaptiveNamespaceClass)

    val IrCall.isArgumentAdaptiveCall: Boolean
        get() = symbol.owner.name == Names.KOTLIN_INVOKE && dispatchReceiver !!.type.isAdaptive // TODO better check for kotlin invoke

    val IrCall.isTransformInterfaceCall: Boolean
        get() = dispatchReceiver !!.type.isSubtypeOfClass(pluginContext.adaptiveTransformInterfaceClass)

    fun IrType.isAccessSelector(previousType: IrType?): Boolean {
        if (previousType == null) return false
        if (! isFunctionOrKFunction()) return false
        return previousType.isSubtypeOfClass(pluginContext.adaptiveStateVariableBindingClass)
    }

}