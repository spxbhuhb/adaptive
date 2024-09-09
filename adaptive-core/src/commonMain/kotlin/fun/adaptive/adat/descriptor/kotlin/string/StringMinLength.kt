package `fun`.adaptive.adat.descriptor.kotlin.string

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.descriptor.InstanceValidationResult
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata

@Adat
class StringMinLength(
    override val metadata: AdatDescriptorMetadata,
    val minimum : Int
) : AdatDescriptor() {

    override fun validate(instance: AdatClass, value: Any?, propertyMetadata: AdatPropertyMetadata, result: InstanceValidationResult) {
        value as String
        if (value.length < minimum) propertyMetadata.fail(result, this)
    }

}