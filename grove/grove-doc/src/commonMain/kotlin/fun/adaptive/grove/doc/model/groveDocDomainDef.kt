package `fun`.adaptive.grove.doc.model

import `fun`.adaptive.value.domain.AvValueDomainDef

val groveDocDomain
    inline get() = GroveDocValueDomainDef

object GroveDocValueDomainDef : AvValueDomainDef() {

    val subProject = marker { "subProject" }
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

    val exampleGroup = marker { "example-group" }
    val groveDocToc = marker { "grove-doc-toc" }
    val guide = marker { "guide" }
    val definition = marker { "definition" }
}