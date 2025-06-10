package `fun`.adaptive.adat.descriptor.kotlin.long

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata

@Adat
class LongDefault(
    override val metadata: AdatDescriptorMetadata,
    val default : Long
) : AdatDescriptor()