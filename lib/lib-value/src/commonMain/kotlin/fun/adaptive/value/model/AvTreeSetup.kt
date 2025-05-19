package `fun`.adaptive.value.model

import `fun`.adaptive.value.AvMarker
import `fun`.adaptive.value.AvRefLabel

class AvTreeSetup(
    val nodeMarker: AvMarker,
    val childListMarker: AvMarker,
    val parentRefLabel: AvRefLabel,
    val childListRefLabel: AvRefLabel
)