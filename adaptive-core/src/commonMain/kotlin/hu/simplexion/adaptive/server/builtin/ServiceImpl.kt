/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.builtin

import hu.simplexion.adaptive.service.BasicServiceContext
import hu.simplexion.adaptive.service.ServiceBase
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.defaultServiceCallTransport
import hu.simplexion.adaptive.service.transport.ServiceCallTransport
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.utility.manualOrPlugin
import hu.simplexion.adaptive.utility.overrideManually
import hu.simplexion.adaptive.utility.pluginGenerated
import hu.simplexion.adaptive.wireformat.WireFormatDecoder

interface ServiceImpl<T : ServiceImpl<T>> : ServiceBase, ServerFragmentImpl {

    /**
     * Context of a service call. Set by `dispatch` when the call goes through it.
     */
    val serviceContext: ServiceContext
        get() = manualOrPlugin("serviceContext")

    /**
     * The internal version of this service implementation. The context is [BasicServiceContext] with
     * `UUID.NIL` context id.
     */
    val internal: T
        get() = newInstance(BasicServiceContext(UUID.nil()))

    /**
     * Create a new instance of the given service with the given context.
     */
    operator fun invoke(context: ServiceContext): T = newInstance(context)

    fun newInstance(serviceContext: ServiceContext): T =
        manualOrPlugin("newInstance")

    /**
     * Called by the generated dispatch when the requested function name is unknown.
     */
    fun unknownFunction(funName: String): Nothing {
        throw IllegalStateException("unknown function: $funName")
    }

    /**
     * Called by service transports to execute a service call. Actual code of this function is generated
     * by the plugin.
     */
    suspend fun dispatch(funName: String, payload: WireFormatDecoder<*>): ByteArray {
        pluginGenerated()
    }

}