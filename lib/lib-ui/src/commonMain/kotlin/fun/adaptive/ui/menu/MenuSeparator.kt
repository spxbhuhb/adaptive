package `fun`.adaptive.ui.menu

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.utility.UUID

@Adat
class MenuSeparator<T>(
    override var role : UUID<*>? = null
) : MenuItemBase<T>()