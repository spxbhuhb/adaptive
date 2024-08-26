package `fun`.adaptive.adat.descriptor.kotlin.string

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.descriptor.InstanceValidationResult
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import kotlin.collections.plusAssign

@Adat
class StringPattern(
    val pattern: String
) : AdatDescriptor() {

    val regex = Regex(pattern)

    override fun validate(instance: AdatClass<*>, value : Any?, metadata : AdatPropertyMetadata, result : InstanceValidationResult) {
        value as String
        if (!regex.matches(value)) result.failedConstraints += this
    }

}