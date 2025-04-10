package `fun`.adaptive.runtime

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.service.transport.ServiceCallTransport
import kotlinx.coroutines.CoroutineScope

open class ClientWorkspace(
    val backend: BackendAdapter,
    val scope: CoroutineScope = backend.scope,
    val transport: ServiceCallTransport = backend.transport
) : AbstractWorkspace()