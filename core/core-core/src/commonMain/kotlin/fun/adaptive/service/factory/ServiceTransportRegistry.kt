/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.service.factory

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.internal.NamedFragmentFactory
import `fun`.adaptive.foundation.replacedByPlugin
import `fun`.adaptive.registry.Registry
import `fun`.adaptive.service.testing.DirectServiceTransport
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.PluginReference
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Json
import kotlin.reflect.KFunction

/**
 * Registry of service transport factories which supports creating new
 * [service transports](def://) from the scheme of the url.
 */
open class ServiceTransportRegistry : Registry<ServiceTransportFactory>() {

    fun add(key: FragmentKey, buildFun: ServiceTransportFactory) {
        set(key, buildFun)
    }

    fun newInstance(
        url : String,
        wireFormatProvider : WireFormatProvider,
        setupFun : suspend (ServiceCallTransport) -> Unit = {  }
    ): ServiceCallTransport {
        val scheme = url.substringBefore("://")
        val buildFun = checkNotNull(get(scheme)) { "Unknown service transport scheme $scheme" }
        return buildFun.invoke(url, wireFormatProvider, setupFun)
    }

}