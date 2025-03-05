package `fun`.adaptive.resource.document

import `fun`.adaptive.resource.Qualifier
import `fun`.adaptive.resource.ResourceFile

class DocumentResource(
    path: String,
    qualifiers: Set<Qualifier>
) : ResourceFile(
    path,
    qualifiers
)