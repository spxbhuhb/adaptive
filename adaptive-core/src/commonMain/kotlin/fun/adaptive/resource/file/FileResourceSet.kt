package `fun`.adaptive.resource.file

import `fun`.adaptive.resource.ResourceFileSet
import `fun`.adaptive.resource.ResourceFileType

class FileResourceSet(
    name: String,
    vararg resources: FileResource
) : ResourceFileSet<FileResource>(
    name,
    ResourceFileType.File,
    resources.toList()
)