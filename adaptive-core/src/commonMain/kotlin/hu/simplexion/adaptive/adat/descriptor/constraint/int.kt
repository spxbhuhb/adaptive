/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.descriptor.constraint

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.descriptor.AdatDescriptorImpl
import hu.simplexion.adaptive.adat.descriptor.DescriptorExpect
import hu.simplexion.adaptive.adat.descriptor.result.InstanceValidationResult
import hu.simplexion.adaptive.adat.metadata.AdatDescriptorMetadata
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetadata
import hu.simplexion.adaptive.foundation.manualImplementation

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