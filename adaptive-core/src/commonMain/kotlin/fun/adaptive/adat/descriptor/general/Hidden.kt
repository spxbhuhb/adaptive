package `fun`.adaptive.adat.descriptor.general

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata

@Adat
class Hidden(
    override val metadata: AdatDescriptorMetadata,
    val isHidden: Boolean
) : AdatDescriptor()