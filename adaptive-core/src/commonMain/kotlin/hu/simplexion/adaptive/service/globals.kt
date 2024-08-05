/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.service.factory.BasicServiceImplFactory
import hu.simplexion.adaptive.service.factory.ServiceImplFactory
import hu.simplexion.adaptive.service.transport.ServiceCallTransport

lateinit var defaultServiceCallTransport: ServiceCallTransport

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
fun <T> getService(context: ServiceContext? = null, consumer: T? = null): T {
    checkNotNull(consumer)
    if (context?.transport != null) {
        (consumer as ServiceConsumer).serviceCallTransport = context.transport
    }
    return consumer
}