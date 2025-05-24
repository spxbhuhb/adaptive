package `fun`.adaptive.grove.doc.model

import `fun`.adaptive.value.model.AvTreeSetup

val docTreeSetup = AvTreeSetup(
    nodeMarker = "doc",                     // Marks nodes in the tree (e.g., a documentation entry)
    childListMarker = "doc-children",       // Marks values that hold child references
    rootListMarker = "doc-roots",           // Optional, value that holds top-level nodes
    parentRefLabel = "docParentRef",        // Label for parent reference
    childListRefLabel = "docChildrenRef"    // Label pointing to the reference list of children
)