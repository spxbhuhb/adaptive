package `fun`.adaptive.adat.descriptor.kotlin.bool

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.descriptor.InstanceValidationResult
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata

class BooleanValue(
    val expected : Boolean
) : AdatDescriptor() {

    override fun validate(instance: AdatClass<*>, value : Any?, metadata : AdatPropertyMetadata, result : InstanceValidationResult) {
        value as Boolean
        if (value != expected) result.failedConstraints += this
    }

}