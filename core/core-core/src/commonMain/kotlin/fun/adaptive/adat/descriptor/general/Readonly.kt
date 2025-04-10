package `fun`.adaptive.adat.descriptor.general

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata

@Adat
class Readonly(
    override val metadata: AdatDescriptorMetadata,
    val isReadonly: Boolean
) : AdatDescriptor()