package `fun`.adaptive.adat.descriptor

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata

abstract class AdatDescriptor {

    abstract val metadata: AdatDescriptorMetadata

    open fun validate(instance: AdatClass, value: Any?, propertyMetadata: AdatPropertyMetadata, result: InstanceValidationResult) {
        // non-constraint descriptors may leave this function empty
    }

    fun isNull(value: Any?, propertyMetadata: AdatPropertyMetadata, result: InstanceValidationResult): Boolean {
        if (value == null) {
            if (! propertyMetadata.isNullable) propertyMetadata.fail(result, this)
            return true
        }
        return false
    }

}