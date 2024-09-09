package `fun`.adaptive.adat.descriptor.kotlin.integer

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.descriptor.InstanceValidationResult
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata

@Adat
class IntMinimum(
    override val metadata: AdatDescriptorMetadata,
    val minimum : Int
) : AdatDescriptor() {

    override fun validate(instance: AdatClass, value: Any?, propertyMetadata: AdatPropertyMetadata, result: InstanceValidationResult) {
        value as Int
        if (value < minimum) propertyMetadata.fail(result, this)
    }

}