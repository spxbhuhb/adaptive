package `fun`.adaptive.resource.model

class ImageResourceSet(
    name: String,
    vararg resources: ImageResource
) : ResourceFileSet<ImageResource>(
    name,
    ResourceFileType.Image,
    resources.toList()
)