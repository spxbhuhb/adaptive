/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatcompanion

import hu.simplexion.adaptive.kotlin.adat.Names
import hu.simplexion.adaptive.kotlin.adat.Strings
import hu.simplexion.adaptive.kotlin.adat.ir.AdatPluginContext
import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import hu.simplexion.adaptive.kotlin.wireformat.Signature
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.getSimpleFunction
import org.jetbrains.kotlin.ir.util.parentAsClass

class AdatMetaDataPropertyTransform(
    override val pluginContext: AdatPluginContext,
    val companionClass: IrClass
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        val adatClass = companionClass.parentAsClass

        // Would be more elegant to use AdatClassMetaData directly, but I can't figure
        // out how to configure Gradle dependencies properly. Shadowjar is needed to
        // have coroutines and datetime but shadowjar colludes with the normal lib.
        // This will be smaller anyway.

        // So, the format I'll use:
        // <version>/<className>/propertyName1/propertyIndex1/propertySignature1/...

        // decoder of this is in AdatCompanion

        val result = mutableListOf("1",adatClass.classId!!.asFqNameString())
        var index = 0

        for (property in adatClass.declarations) {
            if (property !is IrProperty) continue
            if (property.name == Names.ADAT_VALUES || property.name == Names.ADAT_COMPANION) continue

            result += property.name.identifier
            result += index.toString()
            result += Signature.typeSignature(property.getter!!.returnType)

            index++
        }

        declaration.backingField!!.initializer = irFactory.createExpressionBody(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irCall(
                companionClass.getSimpleFunction(Strings.DECODE_METADATA)!!,
                irGet(companionClass.thisReceiver!!),
                irConst(result.joinToString("/"))
            )
        )

        return declaration
    }

}
