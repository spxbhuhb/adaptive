package `fun`.adaptive.grove.sheet

import `fun`.adaptive.foundation.value.storeFor

class SheetViewContext {
    val focusedView = storeFor<SheetViewController?> { null }
}