/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.services.ir.consumer

import hu.simplexion.z2.kotlin.common.AbstractIrBuilder
import hu.simplexion.z2.kotlin.common.functionByName
import hu.simplexion.z2.kotlin.common.property
import hu.simplexion.z2.kotlin.common.propertyGetter
import hu.simplexion.z2.kotlin.services.Names
import hu.simplexion.z2.kotlin.services.ServicesPluginKey
import hu.simplexion.z2.kotlin.services.Strings
import hu.simplexion.z2.kotlin.services.ir.ServicesPluginContext
import hu.simplexion.z2.kotlin.services.ir.util.IrClassBaseBuilder
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid

/**
 * Add initializers and function bodies to the consumer class.
 */
class ConsumerClassTransform(
    override val pluginContext: ServicesPluginContext,
    val interfaceClass: IrClass
) : IrElementVisitorVoid, AbstractIrBuilder, IrClassBaseBuilder {

    val consumerClass = interfaceClass.declarations.let {
        requireNotNull(interfaceClass.declarations.firstOrNull { it is IrClass && it.name == Names.CONSUMER }) {
            "missing consumer class for ${interfaceClass.classId}"
        } as IrClass
    }


    fun build() {
        addServiceNameInitializer()
        addServiceCallTransportInitializer()

        for (serviceFunction in consumerClass.functions) {
            val origin = serviceFunction.origin as? IrDeclarationOrigin.GeneratedByPlugin
            if (origin?.pluginKey != ServicesPluginKey) continue

            transformServiceFunction(serviceFunction)
        }

        if (pluginContext.options.dumpKotlinLike) {
            pluginContext.debug("KOTLIN LIKE") { "\n\n" + consumerClass.dumpKotlinLike(KotlinLikeDumpOptions(printFakeOverridesStrategy = FakeOverridesStrategy.NONE)) }
        }
    }

    private fun addServiceNameInitializer() {
        val property = consumerClass.property(Names.FQ_NAME)
        val backingField = requireNotNull(property.backingField)

        backingField.initializer = irFactory.createExpressionBody(irConst(interfaceClass.kotlinFqName.asString()))
    }

    private fun addServiceCallTransportInitializer() {
        val property = consumerClass.property(Names.SERVICE_CALL_TRANSPORT_PROPERTY)
        val backingField = requireNotNull(property.backingField)

        backingField.initializer = irFactory.createExpressionBody(irNull())
    }

    fun transformServiceFunction(function: IrSimpleFunction) {
        function.body = DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
            + irReturn(
                pluginContext.wireFormatCache.standaloneDecode(
                    targetType = function.returnType,
                    standalone = irCall(
                        consumerClass.propertyGetter { Strings.WIREFORMAT_STANDALONE_PROPERTY },
                        irGet(function.dispatchReceiverParameter !!)
                    ),
                    value = irCall(
                        consumerClass.functionByName { Strings.CALL_SERVICE },
                        irGet(function.dispatchReceiverParameter !!),
                        irConst(pluginContext.wireFormatCache.signature(function)),
                        buildPayload(function)
                    )
                )
            )
        }
    }

    fun buildPayload(function: IrSimpleFunction): IrExpression {
        var payload = irCall(
            consumerClass.propertyGetter { Strings.WIREFORMAT_ENCODER_PROPERTY },
            dispatchReceiver = irGet(function.dispatchReceiverParameter !!)
        ).also { it.origin = IrStatementOrigin.GET_PROPERTY }

        val parameterCount = function.valueParameters.size

        function.valueParameters.forEachIndexed { fieldNumber, valueParameter ->
            payload = pluginContext.wireFormatCache.encode(
                payload,
                fieldNumber + 1,
                valueParameter.name.identifier,
                irGet(valueParameter)
            )
        }

        return payload
    }

}
