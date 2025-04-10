package `fun`.adaptive.app

import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.workspace.Workspace

class WsBrowserClientBuilder {

    val modules = mutableListOf<AppModule<Workspace>>()

    fun module(moduleFun: () -> AppModule<Workspace>) {
        modules += moduleFun()
    }
}