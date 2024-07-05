/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.adat.descriptor

import hu.simplexion.adaptive.adat.metadata.AdatDescriptorMetadata
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetadata

class NamedDescriptorFactory(
    val name: String,
    val buildFun: (propertyMetadata: AdatPropertyMetadata, descriptorMetadata: AdatDescriptorMetadata) -> AdatDescriptorImpl
) {
    fun build(propertyMetadata: AdatPropertyMetadata, descriptorMetadata: AdatDescriptorMetadata): AdatDescriptorImpl {
        return buildFun(propertyMetadata, descriptorMetadata)
    }
}