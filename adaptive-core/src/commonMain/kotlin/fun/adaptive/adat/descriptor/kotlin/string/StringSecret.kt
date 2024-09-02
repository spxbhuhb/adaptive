package `fun`.adaptive.adat.descriptor.kotlin.string

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata

@Adat
class StringSecret(
    override val metadata: AdatDescriptorMetadata,
    val isSecret : Boolean
) : AdatDescriptor()