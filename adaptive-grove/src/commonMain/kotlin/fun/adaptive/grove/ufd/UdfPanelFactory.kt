package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.grove.sheet.fragment.sheet

class UdfPanelFactory : FoundationFragmentFactory() {
    init {
        add("grove:ufd:palette", ::palette)
        add("grove:ufd:structure", ::structure)
        add("grove:ufd:instructions", ::instructions)
        add("grove:ufd:sheet", ::sheet)
    }
}