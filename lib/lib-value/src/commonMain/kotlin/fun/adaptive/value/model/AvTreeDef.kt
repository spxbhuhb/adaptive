package `fun`.adaptive.value.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.AvMarker
import `fun`.adaptive.value.AvRefLabel

@Adat
class AvTreeDef(
    val nodeMarker: AvMarker,
    val childListMarker: AvMarker,
    val parentRefLabel: AvRefLabel,
    val childListRefLabel: AvRefLabel,
    val rootListMarker: AvMarker?
)