package `fun`.adaptive.cookbook.auto.originFile_originFile

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID


@Adat
class DataItem(
    val id: UUID<DataItem>,
    val recordName : String
)