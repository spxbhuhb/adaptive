/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.adat.descriptor

import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata

class NamedDescriptorFactory(
    val name: String,
    val buildFun: (propertyMetadata: AdatPropertyMetadata, descriptorMetadata: AdatDescriptorMetadata) -> AdatDescriptorImpl
) {
    fun build(propertyMetadata: AdatPropertyMetadata, descriptorMetadata: AdatDescriptorMetadata): AdatDescriptorImpl {
        return buildFun(propertyMetadata, descriptorMetadata)
    }
}