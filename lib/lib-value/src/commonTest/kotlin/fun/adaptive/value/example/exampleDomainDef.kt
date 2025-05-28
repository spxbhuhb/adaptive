package `fun`.adaptive.value.example

import `fun`.adaptive.value.domain.AvValueDomainDef

internal val avDomain
    inline get() = ExampleDomainDef

/**
 * This domain defines a value tree with all the necessary
 * markers and reference labels.
 */
object ExampleDomainDef : AvValueDomainDef() {

    val node = marker { "example-node" }
    val childList = marker { "example-children" }
    val rootList = marker { "example-roots" }

    val parentRef = refLabel { "exampleParentRef" }
    val childListRef = refLabel { "exampleChildrenRef" }

    val treeDef = tree(
        nodeMarker = node,
        childListMarker = childList,
        rootListMarker = rootList,
        parentRefLabel = parentRef,
        childListRefLabel = childListRef
    )

}