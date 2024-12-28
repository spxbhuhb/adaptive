package `fun`.adaptive.resource.image

import `fun`.adaptive.resource.ResourceFileSet
import `fun`.adaptive.resource.ResourceTypeQualifier

class ImageResourceSet(
    name: String,
    vararg resources: ImageResource
) : ResourceFileSet<ImageResource>(
    name,
    ResourceTypeQualifier.Image,
    resources.toList()
)