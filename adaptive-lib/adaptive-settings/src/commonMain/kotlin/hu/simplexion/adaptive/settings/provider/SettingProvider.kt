package hu.simplexion.adaptive.settings.provider

import hu.simplexion.adaptive.settings.model.Setting

interface SettingProvider {

    val isReadOnly : Boolean

    fun put(path : String, value : String?)

    fun get(path : String, children : Boolean = true) : List<Setting>

}