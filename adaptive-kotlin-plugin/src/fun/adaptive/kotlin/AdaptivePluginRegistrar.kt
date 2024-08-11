/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin

import `fun`.adaptive.kotlin.adat.fir.AdatDeclarationGenerator
import `fun`.adaptive.kotlin.adat.fir.AdatSupertypeGenerator
import `fun`.adaptive.kotlin.service.fir.ServicesDeclarationGenerator
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class AdaptivePluginRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        + ::ServicesDeclarationGenerator
        + ::AdatDeclarationGenerator
        + ::AdatSupertypeGenerator
    }
}