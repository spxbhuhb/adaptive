package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object UfdPanelFactory : FoundationFragmentFactory() {
    init {
        add("grove:ufd:palette", ::ufdPalette)
        add("grove:ufd:structure", ::ufdStructure)
        add("grove:ufd:instructions", ::ufdInstructions)
        add("grove:ufd:center", ::ufdCenter)
    }
}