/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.descriptor.info

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.descriptor.AdatDescriptorImpl
import `fun`.adaptive.adat.descriptor.DescriptorExpect
import `fun`.adaptive.adat.descriptor.result.InstanceValidationResult
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.foundation.manualImplementation

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