package `fun`.adaptive.resource.file

import `fun`.adaptive.resource.Qualifier
import `fun`.adaptive.resource.ResourceFile

class FileResource(
    path: String,
    qualifiers: Set<Qualifier>
) : ResourceFile(
    path,
    qualifiers
)