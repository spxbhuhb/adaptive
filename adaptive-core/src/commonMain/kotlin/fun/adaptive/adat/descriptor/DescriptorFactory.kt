/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.descriptor

import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.registry.Registry

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