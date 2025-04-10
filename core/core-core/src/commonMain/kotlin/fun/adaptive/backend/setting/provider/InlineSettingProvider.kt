package `fun`.adaptive.backend.setting.provider

import `fun`.adaptive.backend.setting.model.Setting

/**
 * Uses a map to store and retrieve the settings. Intended use is browsers
 * where the actual settings are loaded from the backend asynchronously.
 */
class InlineSettingProvider : SettingProvider {

    val items = mutableMapOf<String, String?>()

    override var isReadOnly = false

    override fun put(path: String, value: String?) {
        items[path] = value
    }

    override fun get(path: String, children: Boolean): List<Setting> {
        if (! children) {
            return items[path]?.let { listOf(Setting(path, it)) } ?: emptyList()
        }

        val prefix = "$path/"

        return items.entries
            .filter { it.key.startsWith(path) || it.key.startsWith(prefix) }
            .map { Setting(it.key, it.value) }
    }

}