package `fun`.adaptive.cookbook.auto.originFolder_originList

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID


@Adat
class MasterDataItem(
    val id: UUID<MasterDataItem>,
    val recordName : String
)