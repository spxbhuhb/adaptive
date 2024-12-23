package `fun`.adaptive.resource.image

import `fun`.adaptive.resource.Qualifier
import `fun`.adaptive.resource.ResourceFile

class ImageResource(
    path: String,
    qualifiers: Set<Qualifier>
) : ResourceFile(
    path,
    qualifiers
)