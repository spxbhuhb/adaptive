/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.descriptor.result

import hu.simplexion.adaptive.adat.descriptor.AdatDescriptorImpl

class InstanceValidationResult {

    val isValid: Boolean
        get() = failedConstraints.isEmpty()

    val failedConstraints = mutableListOf<AdatDescriptorImpl>()

}