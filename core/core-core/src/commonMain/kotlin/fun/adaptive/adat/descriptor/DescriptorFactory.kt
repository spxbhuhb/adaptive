/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.descriptor

import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.registry.Registry

open class DescriptorFactory : Registry<(AdatDescriptorMetadata) -> AdatDescriptor>() {

    fun add(
        key: String,
        buildFun: (descriptorMetadata: AdatDescriptorMetadata) -> AdatDescriptor
    ) {
        set(key, buildFun)
    }

    fun newInstance(
        key: String,
        descriptorMetadata: AdatDescriptorMetadata
    ): AdatDescriptor {
        val buildFun = checkNotNull(get(key)) { "Unknown descriptor type: $key, known descriptor types: ${entries.keys}" }
        return buildFun(descriptorMetadata)
    }

}