package `fun`.adaptive.ui.fragment.structural

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AuiAdapter

class AuiPrimaryPopup(
    adapter: AuiAdapter,
    parent: AdaptiveFragment?,
    index: Int,
) : AbstractClickPopup(adapter, parent, index, "click")