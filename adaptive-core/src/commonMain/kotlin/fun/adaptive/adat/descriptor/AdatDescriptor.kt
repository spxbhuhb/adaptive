package `fun`.adaptive.adat.descriptor

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata

abstract class AdatDescriptor {

    abstract val metadata : AdatDescriptorMetadata

    open fun validate(instance: AdatClass<*>, value : Any?, propertyMetadata : AdatPropertyMetadata, result : InstanceValidationResult) {
        // non-constraint descriptors may leave this function empty
    }

}