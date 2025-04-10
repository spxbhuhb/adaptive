package `fun`.adaptive.auto.model

import `fun`.adaptive.adat.Adat

@Adat
class AutoMetadata(
    val connection: AutoConnectionInfo<out Any?>?,
    val removedItems: List<ItemId>?,
    val milestone: LamportTimestamp?
)