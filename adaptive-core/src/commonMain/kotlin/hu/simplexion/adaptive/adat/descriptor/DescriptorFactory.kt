/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.descriptor

import hu.simplexion.adaptive.adat.metadata.AdatDescriptorMetadata
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetadata
import hu.simplexion.adaptive.registry.Registry

open class DescriptorFactory : Registry<NamedDescriptorFactory>() {

    fun add(
        key: String,
        buildFun: (propertyMetadata: AdatPropertyMetadata, descriptorMetadata: AdatDescriptorMetadata) -> AdatDescriptorImpl
    ) {
        set(key, NamedDescriptorFactory(key, buildFun))
    }

    fun newInstance(
        key: String,
        propertyMetadata: AdatPropertyMetadata,
        descriptorMetadata: AdatDescriptorMetadata
    ): AdatDescriptorImpl {
        return checkNotNull(get(key)) { "Unknown descriptor type: $key, known descriptor types: ${entries.keys}" }
            .build(propertyMetadata, descriptorMetadata)
    }

}