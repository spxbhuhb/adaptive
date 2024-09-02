package `fun`.adaptive.adat.descriptor.kotlin.bool

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.descriptor.InstanceValidationResult
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata

class BooleanValue(
    override val metadata: AdatDescriptorMetadata,
    val expected : Boolean
) : AdatDescriptor() {

    override fun validate(instance: AdatClass<*>, value : Any?, propertyMetadata : AdatPropertyMetadata, result : InstanceValidationResult) {
        value as Boolean
        if (value != expected) propertyMetadata.fail(result, this)
    }

}