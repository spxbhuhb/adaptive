/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin

import hu.simplexion.adaptive.kotlin.adat.fir.AdatDeclarationGenerator
import hu.simplexion.adaptive.kotlin.adat.fir.AdatSupertypeGenerator
import hu.simplexion.adaptive.kotlin.fqnameaware.fir.FqNameAwareDeclarationGenerator
import hu.simplexion.adaptive.kotlin.service.fir.ServicesDeclarationGenerator
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar


class AdaptivePluginRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        + ::ServicesDeclarationGenerator
        + ::FqNameAwareDeclarationGenerator
        + ::AdatDeclarationGenerator
        + ::AdatSupertypeGenerator
    }
}