package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object UfdPaneFactory : FoundationFragmentFactory() {
    init {
        add(UfdWsContext.PALETTE_TOOL_KEY, ::ufdPalette)
        add(UfdWsContext.COMPONENTS_TOOL_KEY, ::ufdComponents)
        add(UfdWsContext.INSTRUCTIONS_TOOL_KEY, ::ufdInstructions)
        add(UfdWsContext.STATE_TOOL_KEY, ::ufdState)
        add(UfdWsContext.CONTENT_PANE_KEY, ::ufdCenter)
    }
}