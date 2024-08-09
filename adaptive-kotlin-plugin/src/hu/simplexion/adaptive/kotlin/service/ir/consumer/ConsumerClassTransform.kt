/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.service.ir.consumer

import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import hu.simplexion.adaptive.kotlin.common.functionByName
import hu.simplexion.adaptive.kotlin.common.property
import hu.simplexion.adaptive.kotlin.service.Names
import hu.simplexion.adaptive.kotlin.service.ServicesPluginKey
import hu.simplexion.adaptive.kotlin.service.Strings
import hu.simplexion.adaptive.kotlin.service.ir.ServicesPluginContext
import hu.simplexion.adaptive.kotlin.service.ir.util.IrClassBaseBuilder
import hu.simplexion.adaptive.kotlin.wireformat.Signature
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.util.*

/**
 * Add initializers and function bodies to the consumer class.
 */
class ConsumerClassTransform(
    override val pluginContext: ServicesPluginContext
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder, IrClassBaseBuilder {

    lateinit var interfaceClass: IrClass

    val consumerClass by lazy {
        requireNotNull(
            interfaceClass.declarations.firstOrNull { it is IrClass && it.name == Names.CONSUMER }
        ) { "missing consumer class for ${interfaceClass.classId}" } as IrClass
    }

    override fun visitClassNew(declaration: IrClass): IrStatement {
        check(! ::interfaceClass.isInitialized)

        interfaceClass = declaration

        addServiceNameInitializer()
        addServiceCallTransportInitializer()

        for (serviceFunction in consumerClass.functions) {
            val origin = serviceFunction.origin as? IrDeclarationOrigin.GeneratedByPlugin
            if (origin?.pluginKey != ServicesPluginKey) continue

            transformServiceFunction(serviceFunction)
        }

        return declaration
    }

    private fun addServiceNameInitializer() {
        val property = consumerClass.property(Names.SERVICE_NAME)
        val backingField = requireNotNull(property.backingField)

        backingField.initializer = irFactory.createExpressionBody(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irConst(interfaceClass.kotlinFqName.asString())
        )
    }

    private fun addServiceCallTransportInitializer() {
        val property = consumerClass.property(Names.SERVICE_CALL_TRANSPORT_PROPERTY)
        val backingField = requireNotNull(property.backingField)

        backingField.initializer = irFactory.createExpressionBody(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irNull()
        )
    }

    fun transformServiceFunction(function: IrSimpleFunction) {
        function.body = DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
            + irReturn(
                pluginContext.wireFormatCache.decodeReturnValue(
                    targetType = function.returnType,
                    decoder = irCall(
                        consumerClass.functionByName { Strings.WIREFORMAT_DECODER },
                        irGet(function.dispatchReceiverParameter !!),
                        irCall(
                            consumerClass.functionByName { Strings.CALL_SERVICE },
                            irGet(function.dispatchReceiverParameter !!),
                            irConst(Signature.functionSignature(function)),
                            buildPayload(function)
                        )
                    )
                )
            )
        }
    }

    fun buildPayload(function: IrSimpleFunction): IrExpression {
        var payload = irCall(
            consumerClass.functionByName { Strings.WIREFORMAT_ENCODER },
            dispatchReceiver = irGet(function.dispatchReceiverParameter !!)
        ).also { it.origin = IrStatementOrigin.GET_PROPERTY }

        if (function.valueParameters.isEmpty()) return payload

        payload = irCall(
            pluginContext.wireFormatCache.pluginContext.pseudoInstanceStart,
            dispatchReceiver = payload
        )

        function.valueParameters.forEachIndexed { fieldNumber, valueParameter ->
            payload = pluginContext.wireFormatCache.encode(
                payload,
                fieldNumber + 1,
                valueParameter.name.identifier,
                irGet(valueParameter)
            )
        }

        payload = irCall(
            pluginContext.wireFormatCache.pluginContext.pseudoInstanceEnd,
            dispatchReceiver = payload
        )

        return payload
    }

}
