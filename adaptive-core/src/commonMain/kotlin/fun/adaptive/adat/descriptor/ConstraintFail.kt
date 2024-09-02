package `fun`.adaptive.adat.descriptor

import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata

data class ConstraintFail(
    val property: AdatPropertyMetadata?,
    val descriptorMetadata: AdatDescriptorMetadata
)