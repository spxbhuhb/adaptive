package `fun`.adaptive.value.domain

import `fun`.adaptive.value.AvMarker
import `fun`.adaptive.value.AvRefLabel
import `fun`.adaptive.value.AvStatus
import `fun`.adaptive.value.model.AvTreeDef

abstract class AvValueDomainDef {

    inline fun marker(def: () -> AvMarker) = def()

    inline fun refLabel(def: () -> AvRefLabel) = def()

    inline fun status(def: () -> AvStatus) = def()

    @Suppress("NOTHING_TO_INLINE")
    inline fun tree(
        nodeMarker: AvMarker,
        childListMarker: AvMarker,
        parentRefLabel: AvRefLabel,
        childListRefLabel: AvRefLabel,
        rootListMarker: AvMarker? = null
    ) = AvTreeDef(nodeMarker, childListMarker, parentRefLabel, childListRefLabel, rootListMarker)
}