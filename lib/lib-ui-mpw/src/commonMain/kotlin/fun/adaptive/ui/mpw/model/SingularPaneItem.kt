package `fun`.adaptive.ui.mpw.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.model.NamedItemType
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.uuid4

@Adat
class SingularPaneItem(
    override val name: String,
    override val type: NamedItemType,
    val icon : GraphicsResourceSet? = null,
    val uuid : UUID<SingularPaneItem> = uuid4()
) : NamedItem()