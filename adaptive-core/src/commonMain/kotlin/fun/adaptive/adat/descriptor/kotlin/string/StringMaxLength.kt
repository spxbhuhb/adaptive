package `fun`.adaptive.adat.descriptor.kotlin.string

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.descriptor.InstanceValidationResult
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import kotlin.collections.plusAssign
import kotlin.math.max

@Adat
class StringMaxLength(
    val maximum : Int
) : AdatDescriptor() {

    override fun validate(instance: AdatClass<*>, value : Any?, metadata : AdatPropertyMetadata, result : InstanceValidationResult) {
        value as String
        if (value.length > maximum) result.failedConstraints += this
    }

}