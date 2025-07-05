package `fun`.adaptive.ui.fragment.structural

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AuiBrowserAdapter

class AuiPrimaryPopup(
    adapter: AuiBrowserAdapter,
    parent: AdaptiveFragment?,
    index: Int,
) : AbstractClickPopup(adapter, parent, index, "click")