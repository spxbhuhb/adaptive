package `fun`.adaptive.runtime

import `fun`.adaptive.foundation.unsupported

/**
 * Placeholder for a frontend workspace for servers.
 */
open class NoFrontendWorkspace() : AbstractWorkspace() {
    override val application: AbstractApplication<*, *>?
        get() = unsupported()
}