package `fun`.adaptive.adat.descriptor.kotlin.double

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.descriptor.InstanceValidationResult
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata

@Adat
class DoubleMaximum(
    override val metadata: AdatDescriptorMetadata,
    val maximum : Double
) : AdatDescriptor() {

    override fun validate(instance: AdatClass, value: Any?, propertyMetadata: AdatPropertyMetadata, result: InstanceValidationResult) {
        if (isNull(value, propertyMetadata, result)) return
        value as Double
        if (value > maximum) propertyMetadata.fail(result, this)
    }

}