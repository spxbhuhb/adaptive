/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.descriptor.info

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.descriptor.AdatDescriptorImpl
import hu.simplexion.adaptive.adat.descriptor.DescriptorExpect
import hu.simplexion.adaptive.adat.descriptor.result.InstanceValidationResult
import hu.simplexion.adaptive.adat.metadata.AdatDescriptorMetadata
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetadata
import hu.simplexion.adaptive.foundation.manualImplementation

private const val namespace = "int"

@DescriptorExpect(namespace)
infix fun Int.default(min: Int): Int {
    manualImplementation(min)
}

class IntDefault(
    override val property: AdatPropertyMetadata,
    override val descriptor: AdatDescriptorMetadata
) : AdatDescriptorImpl {

    val value = descriptor.parameters.toInt()

    override fun validate(instance: AdatClass<*>, result: InstanceValidationResult) = Unit

}