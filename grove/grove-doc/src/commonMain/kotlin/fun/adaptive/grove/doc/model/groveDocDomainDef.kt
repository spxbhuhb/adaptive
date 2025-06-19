package `fun`.adaptive.grove.doc.model

import `fun`.adaptive.value.domain.AvValueDomainDef

val groveDocDomain
    inline get() = GroveDocValueDomainDef

object GroveDocValueDomainDef : AvValueDomainDef() {

    val project = marker { "project" }
    val group = marker { "group" }

    val node = marker { "grove-doc-node" }
    val childList = marker { "grove-doc-children" }
    val rootList = marker { "grove-doc-roots" }

    val parentRef = refLabel { "groveDocParentRef" }
    val childListRef = refLabel { "groveDocChildrenRef" }

    val treeDef = tree(
        nodeMarker = node,
        childListMarker = childList,
        rootListMarker = rootList,
        parentRefLabel = parentRef,
        childListRefLabel = childListRef
    )

}