package `fun`.adaptive.adat.descriptor.kotlin.integer

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.descriptor.InstanceValidationResult
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import kotlin.collections.plusAssign

@Adat
class IntMaximum(
    val maximum : Int
) : AdatDescriptor() {

    override fun validate(instance: AdatClass<*>, value : Any?, metadata : AdatPropertyMetadata, result : InstanceValidationResult) {
        value as Int
        if (value > maximum) result.failedConstraints += this
    }

}