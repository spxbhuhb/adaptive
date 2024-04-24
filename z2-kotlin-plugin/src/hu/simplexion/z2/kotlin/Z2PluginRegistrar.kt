package hu.simplexion.z2.kotlin

import hu.simplexion.z2.kotlin.services.fir.ServicesDeclarationGenerator
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar


class Z2PluginRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        + ::ServicesDeclarationGenerator
    }
}