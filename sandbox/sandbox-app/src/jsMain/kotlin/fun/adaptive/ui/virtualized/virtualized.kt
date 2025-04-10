package `fun`.adaptive.ui.virtualized

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.foundation.internal.cleanStateMask
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment

@AdaptiveExpect("aui")
fun <T> virtualized(
    model: VirtualizationModel<T>,
    @Adaptive
    _fixme_adaptive_itemFun: (index: Int, item: T) -> Unit
) {
    manualImplementation(model, _fixme_adaptive_itemFun)
}