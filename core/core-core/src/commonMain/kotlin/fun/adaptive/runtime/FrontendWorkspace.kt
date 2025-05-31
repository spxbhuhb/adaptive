package `fun`.adaptive.runtime

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.service.transport.ServiceCallTransport
import kotlinx.coroutines.CoroutineScope

open class FrontendWorkspace(
    val backend: BackendAdapter,
    val backendWorkspace : BackendWorkspace,
    val scope: CoroutineScope = backend.scope,
    val transport: ServiceCallTransport = backend.transport
) : AbstractWorkspace() {

    val frontend: AdaptiveAdapter
        get() = checkNotNull(frontendOrNull)

    var frontendOrNull: AdaptiveAdapter? = null

}