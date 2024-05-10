/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.ir.util

import hu.simplexion.adaptive.kotlin.base.Names
import hu.simplexion.adaptive.kotlin.base.ir.AdaptivePluginContext
import org.jetbrains.kotlin.backend.jvm.codegen.isExtensionFunctionType
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.isFunctionOrKFunction

interface AdaptiveNonAnnotationBasedExtension {

    val adaptiveContext: AdaptivePluginContext

    val IrType.isAdaptive: Boolean
        get() {
            if (! isExtensionFunctionType) return false
            if (this !is IrSimpleTypeImpl) return false
            val receiver = arguments[0]
            if (receiver !is IrType) return false
            return receiver.isSubtypeOfClass(adaptiveContext.adaptiveNamespaceClass)
        }

    val IrCall.isDelegated: Boolean
        get() = symbol.owner.hasAnnotation(adaptiveContext.delegatedClass)

    val IrCall.isDirectAdaptiveCall : Boolean
        get() = (symbol.owner.extensionReceiverParameter?.let { it.type == adaptiveContext.adaptiveNamespaceClass.defaultType } ?: false)

    val IrCall.isArgumentAdaptiveCall: Boolean
        get() = symbol.owner.name == Names.KOTLIN_INVOKE && dispatchReceiver !!.type.isAdaptive // TODO better check for kotlin invoke

    val IrCall.isTransformInterfaceCall: Boolean
        get() = dispatchReceiver !!.type.isSubtypeOfClass(adaptiveContext.adaptiveTransformInterfaceClass)

    fun IrType.isAccessSelector(previousType: IrType?): Boolean {
        if (previousType == null) return false
        if (! isFunctionOrKFunction()) return false
        return previousType.isSubtypeOfClass(adaptiveContext.adaptiveStateVariableBindingClass)
    }

}