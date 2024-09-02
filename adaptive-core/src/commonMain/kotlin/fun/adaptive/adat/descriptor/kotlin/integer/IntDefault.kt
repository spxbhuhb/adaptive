package `fun`.adaptive.adat.descriptor.kotlin.integer

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata

@Adat
class IntDefault(
    override val metadata: AdatDescriptorMetadata,
    val default : Int
) : AdatDescriptor()