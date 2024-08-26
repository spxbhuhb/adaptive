package `fun`.adaptive.adat.descriptor

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata

abstract class AdatDescriptor {

    open fun validate(instance: AdatClass<*>, value : Any?, metadata : AdatPropertyMetadata, result : InstanceValidationResult) {
        // non-constraint descriptors may leave this function empty
    }

}