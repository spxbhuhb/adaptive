package `fun`.adaptive.resource.model

class FileResourceSet(
    name: String,
    vararg resources: FileResource
) : ResourceFileSet<FileResource>(
    name,
    ResourceFileType.File,
    resources.toList()
)