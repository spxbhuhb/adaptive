/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.service

import `fun`.adaptive.service.factory.BasicServiceImplFactory
import `fun`.adaptive.service.factory.ServiceImplFactory
import `fun`.adaptive.service.transport.ServiceCallTransport

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
fun <T> getService(transport: ServiceCallTransport?, consumer: T? = null): T {
    checkNotNull(consumer)
    (consumer as ServiceConsumer).serviceCallTransport = transport
    return consumer
}