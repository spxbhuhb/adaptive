/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.descriptor

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.descriptor.result.InstanceValidationResult
import hu.simplexion.adaptive.adat.metadata.AdatDescriptorMetadata
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetadata

interface AdatDescriptorImpl {

    val property: AdatPropertyMetadata
    val descriptor: AdatDescriptorMetadata

    fun validate(instance: AdatClass<*>, result: InstanceValidationResult)

}