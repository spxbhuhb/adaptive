/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.descriptor

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.descriptor.result.InstanceValidationResult
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata

interface AdatDescriptorImpl {

    val property: AdatPropertyMetadata
    val descriptor: AdatDescriptorMetadata

    fun validate(instance: AdatClass<*>, result: InstanceValidationResult)

}