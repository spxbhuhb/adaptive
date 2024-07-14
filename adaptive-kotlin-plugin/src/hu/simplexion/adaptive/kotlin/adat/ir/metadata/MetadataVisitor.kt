/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.adat.ir.metadata

import hu.simplexion.adaptive.adat.metadata.AdatDescriptorMetadata
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetadata
import hu.simplexion.adaptive.kotlin.adat.FqNames
import hu.simplexion.adaptive.kotlin.adat.Names
import hu.simplexion.adaptive.kotlin.adat.ir.AdatIrBuilder
import hu.simplexion.adaptive.kotlin.adat.ir.AdatPluginContext
import hu.simplexion.adaptive.kotlin.adat.ir.immutable.isImmutable
import hu.simplexion.adaptive.kotlin.wireformat.Signature
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid

class MetadataVisitor(
    override val pluginContext: AdatPluginContext,
    val adatClass: IrClass
) : IrElementVisitorVoid, AdatIrBuilder {

    val properties = mutableListOf<PropertyData>()

    var propertyIndex = 0

    override fun visitElement(element: IrElement) {
        element.acceptChildrenVoid(this)
    }

    override fun visitClass(declaration: IrClass) {
        if (declaration != adatClass) return
        super.visitClass(declaration)
    }

    override fun visitProperty(declaration: IrProperty) {
        if (declaration.name == Names.ADAT_COMPANION) return
        if (declaration.name == Names.ADAT_CONTEXT) return
        if (declaration.backingField == null) return

        val signature = Signature.typeSignature(declaration.getter !!.returnType)

        properties += PropertyData(
            declaration,
            AdatPropertyMetadata(
                declaration.name.identifier,
                propertyIndex ++,
                calculateFlags(declaration, signature),
                signature
            )
        )
    }

    fun calculateFlags(declaration: IrProperty, signature: String): Int {
        var flags = 0

        val isVal = ! declaration.isVar

        if (isVal) flags = flags or AdatPropertyMetadata.VAL
        if (isImmutable(signature)) flags = flags or AdatPropertyMetadata.IMMUTABLE_VALUE
        if (declaration.backingField?.type?.isSubtypeOfClass(pluginContext.adatClass) == true) flags = flags or AdatPropertyMetadata.ADAT_CLASS

        return flags
    }

    override fun visitFunction(declaration: IrFunction) {
        if (declaration.name != Names.DESCRIPTOR) return
        if (declaration.isFakeOverride) return
        val body = declaration.body ?: return

        for (statement in body.statements) {
            check(statement is IrCall) { "invalid descriptor statement (not a call) ${statement.dumpKotlinLike()}" }
        }
    }

    fun flattenDescriptorCalls(call: IrCall): Pair<String, List<AdatDescriptorMetadata>> {

        var propertyName: String? = null
        val result = mutableListOf<AdatDescriptorMetadata>()
        var current: IrExpression? = call

        while (current != null) {

            when (current) {

                is IrGetValue -> {
                    val owner = current.symbol.owner
                    check(owner.parent == adatClass) { "invalid descriptor property base: ${current?.dumpKotlinLike()}" }
                    propertyName = owner.name.identifier
                    current = null
                }

                is IrCall -> {
                    val owner = current.symbol.owner

                    val annotation = owner.getAnnotation(FqNames.DESCRIPTOR_EXPECT)

                    if (annotation != null) {

                        result += AdatDescriptorMetadata(
                            annotation.getAnnotationStringValue() + ":" + owner.kotlinFqName,
                            encodeDescriptorParameters(current)
                        )

                        current = current.extensionReceiver

                    } else {

                        val symbol = current.symbol.owner.correspondingPropertySymbol
                        checkNotNull(symbol) { "not a property access (1): ${call.dumpKotlinLike()}" }
                        propertyName = symbol.owner.name.identifier
                        break
                    }

                }

                else -> {
                    throw IllegalStateException("unknown access: ${call.dumpKotlinLike()}")
                }
            }
        }

        check(propertyName != null) { "not a property access (2): ${call.dumpKotlinLike()}" }

        return propertyName to result
    }

    private fun encodeDescriptorParameters(current: IrCall): String {
        check(current.valueArgumentsCount == 1) { "only single parameter descriptors are supported: ${current.dumpKotlinLike()}" }

        val const = current.getValueArgument(0)
        check(const is IrConstImpl<*>) { "only constants are supported as descriptor parameters: ${current.dumpKotlinLike()}" }

        return const.value.toString()
    }

}