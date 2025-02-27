package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object UfdPaneFactory : FoundationFragmentFactory() {
    init {
        add(ufdPalettePaneKey, ::ufdPalette)
        add(ufdComponentsPaneKey, ::ufdComponents)
        add(ufdInstructionsPaneKey, ::ufdInstructions)
        add(ufdStatePaneKey, ::ufdState)
        add(ufdCenterPaneKey, ::ufdCenter)
    }
}