/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.service.ir.impl

import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import hu.simplexion.adaptive.kotlin.service.ir.ServicesPluginContext
import org.jetbrains.kotlin.backend.common.ir.addDispatchReceiver
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.declarations.addFunction
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.getSimpleFunction
import org.jetbrains.kotlin.name.Name

/**
 * Base class for generated companion functions.
 */
abstract class AbstractFun(
    override val pluginContext: ServicesPluginContext,
    val implClassTransform: ImplClassTransform,
    val funName: String,
    val funOverridden: IrSimpleFunctionSymbol
) : AbstractIrBuilder {

    val transformedClass = implClassTransform.transformedClass

    open val returnType: IrType = transformedClass.defaultType

    fun build() {
        val existing = transformedClass.getSimpleFunction(funName)?.owner

        when {
            existing == null -> add()
            existing.isFakeOverride -> transformFake(existing)
            else -> Unit // manually written
        }
    }

    fun add() {
        transformedClass.addFunction {
            name = Name.identifier(funName)
            returnType = this@AbstractFun.returnType
            modality = Modality.FINAL
            visibility = DescriptorVisibilities.PUBLIC
            isSuspend = false
            isFakeOverride = false
            isInline = false
            origin = IrDeclarationOrigin.DEFINED
        }.also { function ->

            function.overriddenSymbols = listOf(funOverridden)

            function.addDispatchReceiver {
                type = transformedClass.defaultType
            }

            function.addParameters()
            function.buildBody()
        }
    }

    fun transformFake(declaration: IrSimpleFunction) {
        declaration.origin = IrDeclarationOrigin.DEFINED
        declaration.isFakeOverride = false
        declaration.buildBody()
    }

    open fun IrSimpleFunction.addParameters() {

    }

    abstract fun IrSimpleFunction.buildBody()

}