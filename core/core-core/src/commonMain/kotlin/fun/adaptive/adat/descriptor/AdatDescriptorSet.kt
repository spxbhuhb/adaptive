package `fun`.adaptive.adat.descriptor

import `fun`.adaptive.adat.metadata.AdatPropertyMetadata

/**
 * `AdatCompanion` stores a list of [AdatDescriptorSet] in `adatDescriptors`.
 */
class AdatDescriptorSet(
    val property : AdatPropertyMetadata,
    val descriptors : List<AdatDescriptor>
)