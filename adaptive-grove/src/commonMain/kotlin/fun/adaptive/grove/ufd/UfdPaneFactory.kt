package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object UfdPaneFactory : FoundationFragmentFactory() {
    init {
        add("grove:ufd:palette", ::ufdPalette)
        add("grove:ufd:components", ::ufdComponents)
        add("grove:ufd:instructions", ::ufdInstructions)
        add("grove:ufd:center", ::ufdCenter)
    }
}