package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.model.NamedItemType
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.uuid4

@Adat
class SingularWsItem(
    override val name: String,
    override val type: NamedItemType,
    val icon : GraphicsResourceSet? = null,
    val uuid : UUID<SingularWsItem> = uuid4()
) : NamedItem()