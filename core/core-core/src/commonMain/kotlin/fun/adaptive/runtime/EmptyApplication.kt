package `fun`.adaptive.runtime

import `fun`.adaptive.foundation.unsupported

class EmptyApplication : AbstractApplication<AbstractWorkspace, AbstractWorkspace>() {

    override val backendWorkspace: AbstractWorkspace
        get() = unsupported()

    override val frontendWorkspace: AbstractWorkspace
        get() = unsupported()

}