package hu.simplexion.adaptive.server.setting.provider

import hu.simplexion.adaptive.server.setting.model.Setting

interface SettingProvider {

    val isReadOnly : Boolean

    fun put(path : String, value : String?)

    fun get(path : String, children : Boolean = true) : List<Setting>

}