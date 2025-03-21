package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.uuid4

@Adat
class SingularWsItem(
    override val name: String,
    override val type: WsItemType,
    val uuid : UUID<SingularWsItem> = uuid4()
) : WsItem()