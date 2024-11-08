package `fun`.adaptive.auto.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatCompanion

@Adat
class AutoMetadata<T>(
    val connection: AutoConnectionInfo<T>,
    val removedItems: List<ItemId>,
    val milestone: LamportTimestamp,
)