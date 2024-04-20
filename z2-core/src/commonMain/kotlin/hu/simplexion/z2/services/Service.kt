package hu.simplexion.z2.services

import hu.simplexion.z2.services.transport.ServiceCallTransport
import hu.simplexion.z2.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider

interface Service {

    /**
     * Name of the service. You can change this field at anytime in case you
     * need multiple destinations for the same service API.
     *
     * Overridden by the plugin with:
     *
     * ```kotlin
     * override var serviceName : String = "<fully qualified name of the service class>"
     * ```
     */
    var serviceName: String
        get() = placeholder() // so we don't have to override in the interface that extends Service
        set(value) = placeholder()

    /**
     * The call transport to use when calling a service function. You can change this
     * field to use different call transport than the default. When null calls use
     * [defaultServiceCallTransport].
     *
     * Overridden by the plugin with:
     *
     * ```kotlin
     * override var serviceCallTransport : ServiceCallTransport? = null
     * ```
     */
    var serviceCallTransport: ServiceCallTransport?
        get() = placeholder() // so we don't have to override in the interface that extends Service
        set(value) = placeholder()

    fun serviceCallTransportOrDefault(): ServiceCallTransport =
        serviceCallTransport ?: defaultServiceCallTransport

    val wireFormatBuilder
        get() = defaultWireFormatProvider.messageBuilder()

    val wireFormatStandalone
        get() = defaultWireFormatProvider.standalone()

}