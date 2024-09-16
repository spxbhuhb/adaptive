package `fun`.adaptive.cookbook.auto.autoFolder_autoList

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID


@Adat
class MasterDataItem(
    val id: UUID<MasterDataItem>,
    val recordName : String
)