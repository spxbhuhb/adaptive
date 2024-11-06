package `fun`.adaptive.auto.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatCompanion

@Adat
class AutoMetadata<VT>(
    val connection : AutoConnectionInfo<VT>,
    val removedItems : List<ItemId>,
    val milestone : LamportTimestamp
)