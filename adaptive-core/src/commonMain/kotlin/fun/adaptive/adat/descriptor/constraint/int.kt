/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.descriptor.constraint

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.descriptor.AdatDescriptorImpl
import `fun`.adaptive.adat.descriptor.DescriptorExpect
import `fun`.adaptive.adat.descriptor.result.InstanceValidationResult
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.foundation.manualImplementation

private const val namespace = "int"

@DescriptorExpect(namespace)
infix fun Int.minimum(limit: Int): Int {
    manualImplementation(limit)
}

class IntMinimum(
    override val property: AdatPropertyMetadata,
    override val descriptor: AdatDescriptorMetadata
) : AdatDescriptorImpl {

    val limit = descriptor.parameters.toInt()

    override fun validate(instance: AdatClass<*>, result: InstanceValidationResult) {
        val value = instance.getValue(property.index) as Int

        if (value < limit) result.failedConstraints += this
    }

}

@DescriptorExpect(namespace)
infix fun Int.maximum(limit: Int): Int {
    manualImplementation(limit)
}

class IntMaximum(
    override val property: AdatPropertyMetadata,
    override val descriptor: AdatDescriptorMetadata
) : AdatDescriptorImpl {

    val limit = descriptor.parameters.toInt()

    override fun validate(instance: AdatClass<*>, result: InstanceValidationResult) {
        val value = instance.getValue(property.index) as Int

        if (value > limit) result.failedConstraints += this
    }

}