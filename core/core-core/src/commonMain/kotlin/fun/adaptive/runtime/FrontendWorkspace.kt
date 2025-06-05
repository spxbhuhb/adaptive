package `fun`.adaptive.runtime

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.safeCall
import `fun`.adaptive.utility.safeSuspendCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class FrontendWorkspace(
    val backend: BackendAdapter,
    val backendWorkspace: BackendWorkspace,
    val scope: CoroutineScope = backend.scope,
    val transport: ServiceCallTransport = backend.transport
) : AbstractWorkspace() {

    override val application: AbstractApplication<*, *>
        get() = backendWorkspace.application

    val logger = getLogger("FrontendWorkspace")

    val frontend: AdaptiveAdapter
        get() = checkNotNull(frontendOrNull)

    var frontendOrNull: AdaptiveAdapter? = null

    /**
     * Runs [block] in the scope of the backend adapter.
     */
    fun io(block: suspend () -> Unit) {
        backend.scope.launch {
            safeSuspendCall(logger, block = block)
        }
    }

    /**
     * Runs [block] in the scope of the frontend adapter.
     */
    fun ui(block: () -> Unit) {
        scope.launch {
            safeCall(logger, block = block)
        }
    }

    open fun onSuccess() {

    }

    open fun onFail(ex: Exception) {

    }

    open fun save(
        onSuccess: () -> Unit = ::onSuccess,
        onFail: (ex : Exception) -> Unit = ::onFail,
        block: suspend () -> Unit
    ) {
        backend.scope.launch {
            try {
                block()
                scope.launch { safeCall(logger) { onSuccess() } }
            } catch (ex: Exception) {
                logger.error(ex)
                scope.launch {
                    safeCall(logger) { onFail(ex) }
                }
            }
        }
    }

}