package hu.simplexion.z2.services

import hu.simplexion.z2.services.factory.BasicServiceImplFactory
import hu.simplexion.z2.services.factory.ServiceImplFactory
import hu.simplexion.z2.services.transport.LocalServiceCallTransport
import hu.simplexion.z2.services.transport.ServiceCallTransport
import hu.simplexion.z2.utility.UUID

var defaultServiceCallTransport: ServiceCallTransport = LocalServiceCallTransport()

val defaultServiceImplFactory: ServiceImplFactory = BasicServiceImplFactory()

/**
 * Get a service consumer for the interface, specified by the type parameter.
 *
 * **You should NOT pass the [consumer] parameter! It is set by the compiler plugin.**
 *
 * ```kotlin
 * val clicks = getService<ClickApi>()
 * ```
 */
fun <T : Service> getService(consumer: T? = null): T {
    checkNotNull(consumer)
    return consumer
}

/**
 * Get a typed data instance fom the service context.
 *
 * @throws  ClassCastException  If the instance is not of the class [T].
 */
inline operator fun <reified T> ServiceContext.get(uuid: UUID<T>): T? {
    return data.let { it[uuid] } as? T
}

/**
 * Put a data instance into the service context.
 */
operator fun <T> ServiceContext.set(uuid: UUID<T>, value: T) {
    data.let { it[uuid] = value }
}