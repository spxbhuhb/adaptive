package `fun`.adaptive.resource.image

import `fun`.adaptive.resource.ResourceFileSet
import `fun`.adaptive.resource.ResourceFileType

class ImageResourceSet(
    name: String,
    vararg resources: ImageResource
) : ResourceFileSet<ImageResource>(
    name,
    ResourceFileType.Image,
    resources.toList()
)