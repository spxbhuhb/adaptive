/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.services.ir.consumer

import hu.simplexion.z2.kotlin.common.property
import hu.simplexion.z2.kotlin.services.Indices
import hu.simplexion.z2.kotlin.services.Names
import hu.simplexion.z2.kotlin.services.ServicesPluginKey
import hu.simplexion.z2.kotlin.services.ir.ServicesPluginContext
import hu.simplexion.z2.kotlin.services.ir.util.IrClassBaseBuilder
import hu.simplexion.z2.kotlin.services.ir.util.ServiceBuilder
import hu.simplexion.z2.kotlin.services.serviceConsumerName
import org.jetbrains.kotlin.backend.common.ir.addDispatchReceiver
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid

/**
 * Add initializers and function bodies to the consumer class.
 */
class ConsumerClassTransform(
    override val pluginContext: ServicesPluginContext,
    val interfaceClass: IrClass
) : IrElementVisitorVoid, ServiceBuilder, IrClassBaseBuilder {

    val consumerClass = interfaceClass.declarations.let {
        requireNotNull(interfaceClass.declarations.firstOrNull { it is IrClass && it.name == interfaceClass.name.serviceConsumerName }) {
            "missing consumer class for ${interfaceClass.classId}"
        } as IrClass
    }

    override val overriddenServiceFunctions = mutableListOf<IrSimpleFunctionSymbol>()

    override val serviceNames = mutableListOf<String>()

    override lateinit var serviceNameGetter: IrSimpleFunctionSymbol

    fun build() {
        collectServiceFunctions(interfaceClass)

        addServiceNameInitializer()
        addServiceCallTransportInitializer()

        for (serviceFunction in consumerClass.functions) {
            transformServiceFunction(serviceFunction)
        }
    }

    private fun addServiceNameInitializer() {
        val property = consumerClass.property(Names.SERVICE_NAME)
        val backingField = requireNotNull(property.backingField)

        backingField.initializer = irFactory.createExpressionBody(irConst(interfaceClass.kotlinFqName.asString()))
        serviceNameGetter = property.getter !!.symbol
    }

    private fun addServiceCallTransportInitializer() {
        val property = consumerClass.property(Names.SERVICE_CALL_TRANSPORT_PROPERTY)
        val backingField = requireNotNull(property.backingField)

        backingField.initializer = irFactory.createExpressionBody(irNull())
    }

    private fun transformServiceFunction(function: IrSimpleFunction) {
        val origin = function.origin as? IrDeclarationOrigin.GeneratedByPlugin

        when {
            function.isFakeOverride && function.isSuspend -> transformFakeOverrideFunction(function)
            origin?.pluginKey == ServicesPluginKey -> transformDeclaredFunction(function)
        }
    }

    private fun transformFakeOverrideFunction(function: IrSimpleFunction) {
        function.isFakeOverride = false
        function.origin = IrDeclarationOrigin.GeneratedByPlugin(ServicesPluginKey)
        function.modality = Modality.FINAL
        function.addDispatchReceiver {// replace the interface in the dispatcher with the class
            type = consumerClass.defaultType
        }
        transformDeclaredFunction(function)
    }

    fun transformDeclaredFunction(function: IrSimpleFunction) {
        function.body = DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
            + irReturn(
                pluginContext.wireFormatCache.standaloneDecode(
                    targetType = function.returnType,
                    standalone = irCall(
                        pluginContext.getWireFormatStandalone,
                        dispatchReceiver = irGet(function.dispatchReceiverParameter !!)
                    ),
                    value = irCall(
                        pluginContext.callService,
                        dispatchReceiver = irGet(function.dispatchReceiverParameter !!)
                    ).also {
                        it.putValueArgument(Indices.CALL_FUN_NAME, irConst(pluginContext.wireFormatCache.signature(function)))
                        it.putValueArgument(Indices.CALL_PAYLOAD, buildPayload(function))
                    }
                )
            )
        }
    }

    fun buildPayload(function: IrSimpleFunction): IrExpression {
        var payload = irCall(
            pluginContext.getWireFormatEncoder,
            dispatchReceiver = irGet(function.dispatchReceiverParameter !!)
        )

        val parameterCount = function.valueParameters.size

        function.valueParameters.reversed().forEachIndexed { fieldNumber, valueParameter ->
            payload = pluginContext.wireFormatCache.encode(
                payload,
                parameterCount - fieldNumber,
                valueParameter.name.identifier,
                irGet(valueParameter)
            )
        }

        return payload
    }

}
