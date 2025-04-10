package `fun`.adaptive.backend.setting.provider

import `fun`.adaptive.backend.setting.model.Setting

interface SettingProvider {

    val isReadOnly : Boolean

    fun put(path : String, value : String?)

    fun get(path : String, children : Boolean = true) : List<Setting>

}