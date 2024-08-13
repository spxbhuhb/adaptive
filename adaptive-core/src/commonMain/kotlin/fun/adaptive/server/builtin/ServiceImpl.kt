/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.server.builtin

import `fun`.adaptive.service.ServiceBase
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.manualOrPlugin
import `fun`.adaptive.utility.pluginGenerated
import `fun`.adaptive.wireformat.WireFormatDecoder

interface ServiceImpl<T : ServiceImpl<T>> : ServiceBase, ServerFragmentImpl {

    /**
     *  The transport from [serviceContext].
     */
    override var serviceCallTransport: ServiceCallTransport?
        get() = serviceContext.transport
        set(value) {
            manualOrPlugin("serviceCallTransport", value)
        }

    /**
     * Context of a service call. Set by `dispatch` when the call goes through it.
     */
    val serviceContext: ServiceContext
        get() = manualOrPlugin("serviceContext")

    /**
     * The internal version of this service implementation. The context is [ServiceContext] with
     * `UUID.NIL` context id.
     */
    val internal: T
        get() = newInstance(ServiceContext(UUID.nil()))

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