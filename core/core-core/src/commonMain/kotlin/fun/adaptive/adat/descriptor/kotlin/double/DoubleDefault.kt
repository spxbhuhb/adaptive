package `fun`.adaptive.adat.descriptor.kotlin.double

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata

@Adat
class DoubleDefault(
    override val metadata: AdatDescriptorMetadata,
    val default : Double
) : AdatDescriptor()