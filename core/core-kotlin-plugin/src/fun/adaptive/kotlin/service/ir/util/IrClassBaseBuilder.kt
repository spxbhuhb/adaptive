/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.service.ir.util

import `fun`.adaptive.kotlin.common.AbstractIrBuilder
import `fun`.adaptive.kotlin.service.ir.ServicesPluginContext
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.declarations.IrClassBuilder
import org.jetbrains.kotlin.ir.builders.declarations.UNDEFINED_PARAMETER_INDEX
import org.jetbrains.kotlin.ir.builders.declarations.addConstructor
import org.jetbrains.kotlin.ir.builders.declarations.buildClass
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.impl.IrDelegatingConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrInstanceInitializerCallImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrAnonymousInitializerSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrValueParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.name.SpecialNames

interface IrClassBaseBuilder : AbstractIrBuilder {

    fun constructor(klass: IrClass): IrConstructor =

        klass.addConstructor {
            isPrimary = true
            returnType = klass.defaultType
        }.apply {
            parent = klass

            val safeContext = pluginContext as ServicesPluginContext

            body = irFactory.createBlockBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET).apply {

                statements += IrDelegatingConstructorCallImpl(
                    SYNTHETIC_OFFSET,
                    SYNTHETIC_OFFSET,
                    safeContext.serviceImplType,
                    safeContext.serviceImplConstructor,
                    typeArgumentsCount = 0
                )

                statements += IrInstanceInitializerCallImpl(
                    SYNTHETIC_OFFSET,
                    SYNTHETIC_OFFSET,
                    klass.symbol,
                    irBuiltIns.unitType
                )
            }
        }

    private fun initializer(klass: IrClass): IrAnonymousInitializer =

        irFactory.createAnonymousInitializer(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            origin = IrDeclarationOrigin.DEFINED,
            symbol = IrAnonymousInitializerSymbolImpl(),
            isStatic = false
        ).apply {
            parent = klass
        }
}

