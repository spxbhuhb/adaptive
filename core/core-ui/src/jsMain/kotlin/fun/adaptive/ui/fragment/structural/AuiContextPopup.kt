package `fun`.adaptive.ui.fragment.structural

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AuiBrowserAdapter

class AuiContextPopup(
    adapter: AuiBrowserAdapter,
    parent: AdaptiveFragment?,
    index: Int,
) : AbstractClickPopup(adapter, parent, index, "contextmenu")