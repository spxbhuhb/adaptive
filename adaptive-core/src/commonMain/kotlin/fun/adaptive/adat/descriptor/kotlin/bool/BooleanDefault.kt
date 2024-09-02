package `fun`.adaptive.adat.descriptor.kotlin.bool

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata

@Adat
class BooleanDefault(
    override val metadata: AdatDescriptorMetadata,
    val default : Boolean
) : AdatDescriptor()