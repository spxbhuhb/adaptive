package `fun`.adaptive.ui.mpw.example.tree

import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.domain.AvValueDomainDef

typealias ExampleValue = AvValue<ExampleTreeValueSpec>

internal val avDomain
    inline get() = TreePaneExampleDomainDef

/**
 * This domain defines a value tree with all the necessary
 * markers and reference labels.
 */
object TreePaneExampleDomainDef : AvValueDomainDef() {

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